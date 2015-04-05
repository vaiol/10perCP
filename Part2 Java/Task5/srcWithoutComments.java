package org.apache.lucene.index;



import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;




public class TieredMergePolicy extends MergePolicy {
  
  public static final double DEFAULT_NO_CFS_RATIO = 0.1;
  
  private int maxMergeAtOnce = 10;
  private long maxMergedSegmentBytes = 5*1024*1024*1024L;
  private int maxMergeAtOnceExplicit = 30;

  private long floorSegmentBytes = 2*1024*1024L;
  private double segsPerTier = 10.0;
  private double forceMergeDeletesPctAllowed = 10.0;
  private double reclaimDeletesWeight = 2.0;

  
  public TieredMergePolicy() {
    super(DEFAULT_NO_CFS_RATIO, MergePolicy.DEFAULT_MAX_CFS_SEGMENT_SIZE);
  }

  
  public TieredMergePolicy setMaxMergeAtOnce(int v) {
    if (v < 2) {
      throw new IllegalArgumentException("maxMergeAtOnce must be > 1 (got " + v + ")");
    }
    maxMergeAtOnce = v;
    return this;
  }

  
  public int getMaxMergeAtOnce() {
    return maxMergeAtOnce;
  }

    
  
  public TieredMergePolicy setMaxMergeAtOnceExplicit(int v) {
    if (v < 2) {
      throw new IllegalArgumentException("maxMergeAtOnceExplicit must be > 1 (got " + v + ")");
    }
    maxMergeAtOnceExplicit = v;
    return this;
  }

  
  public int getMaxMergeAtOnceExplicit() {
    return maxMergeAtOnceExplicit;
  }

  
  public TieredMergePolicy setMaxMergedSegmentMB(double v) {
    if (v < 0.0) {
      throw new IllegalArgumentException("maxMergedSegmentMB must be >=0 (got " + v + ")");
    }
    v *= 1024 * 1024;
    maxMergedSegmentBytes = (v > Long.MAX_VALUE) ? Long.MAX_VALUE : (long) v;
    return this;
  }

  
  public double getMaxMergedSegmentMB() {
    return maxMergedSegmentBytes/1024/1024.;
  }

   
  public TieredMergePolicy setReclaimDeletesWeight(double v) {
    if (v < 0.0) {
      throw new IllegalArgumentException("reclaimDeletesWeight must be >= 0.0 (got " + v + ")");
    }
    reclaimDeletesWeight = v;
    return this;
  }

  
  public double getReclaimDeletesWeight() {
    return reclaimDeletesWeight;
  }

  
  public TieredMergePolicy setFloorSegmentMB(double v) {
    if (v <= 0.0) {
      throw new IllegalArgumentException("floorSegmentMB must be >= 0.0 (got " + v + ")");
    }
    v *= 1024 * 1024;
    floorSegmentBytes = (v > Long.MAX_VALUE) ? Long.MAX_VALUE : (long) v;
    return this;
  }

  
  public double getFloorSegmentMB() {
    return floorSegmentBytes/(1024*1024.);
  }

   
  public TieredMergePolicy setForceMergeDeletesPctAllowed(double v) {
    if (v < 0.0 || v > 100.0) {
      throw new IllegalArgumentException("forceMergeDeletesPctAllowed must be between 0.0 and 100.0 inclusive (got " + v + ")");
    }
    forceMergeDeletesPctAllowed = v;
    return this;
  }

  
  public double getForceMergeDeletesPctAllowed() {
    return forceMergeDeletesPctAllowed;
  }

  
  public TieredMergePolicy setSegmentsPerTier(double v) {
    if (v < 2.0) {
      throw new IllegalArgumentException("segmentsPerTier must be >= 2.0 (got " + v + ")");
    }
    segsPerTier = v;
    return this;
  }

  
  public double getSegmentsPerTier() {
    return segsPerTier;
  }

  private class SegmentByteSizeDescending implements Comparator<SegmentCommitInfo> {
    @Override
    public int compare(SegmentCommitInfo o1, SegmentCommitInfo o2) {
      try {
        final long sz1 = size(o1);
        final long sz2 = size(o2);
        if (sz1 > sz2) {
          return -1;
        } else if (sz2 > sz1) {
          return 1;
        } else {
          return o1.info.name.compareTo(o2.info.name);
        }
      } catch (IOException ioe) {
        throw new RuntimeException(ioe);
      }
    }
  }

  
  protected static abstract class MergeScore {
    
    protected MergeScore() {
    }
    
    
    abstract double getScore();

    
    abstract String getExplanation();
  }

  @Override
  public MergeSpecification findMerges(MergeTrigger mergeTrigger, SegmentInfos infos) throws IOException {
    if (verbose()) {
      message("findMerges: " + infos.size() + " segments");
    }
    if (infos.size() == 0) {
      return null;
    }
    final Collection<SegmentCommitInfo> merging = writer.get().getMergingSegments();
    final Collection<SegmentCommitInfo> toBeMerged = new HashSet<SegmentCommitInfo>();

    final List<SegmentCommitInfo> infosSorted = new ArrayList<SegmentCommitInfo>(infos.asList());
    Collections.sort(infosSorted, new SegmentByteSizeDescending());

        long totIndexBytes = 0;
    long minSegmentBytes = Long.MAX_VALUE;
    for(SegmentCommitInfo info : infosSorted) {
      final long segBytes = size(info);
      if (verbose()) {
        String extra = merging.contains(info) ? " [merging]" : "";
        if (segBytes >= maxMergedSegmentBytes/2.0) {
          extra += " [skip: too large]";
        } else if (segBytes < floorSegmentBytes) {
          extra += " [floored]";
        }
        message("  seg=" + writer.get().segString(info) + " size=" + String.format(Locale.ROOT, "%.3f", segBytes/1024/1024.) + " MB" + extra);
      }

      minSegmentBytes = Math.min(segBytes, minSegmentBytes);
            totIndexBytes += segBytes;
    }

            int tooBigCount = 0;
    while (tooBigCount < infosSorted.size() && size(infosSorted.get(tooBigCount)) >= maxMergedSegmentBytes/2.0) {
      totIndexBytes -= size(infosSorted.get(tooBigCount));
      tooBigCount++;
    }

    minSegmentBytes = floorSize(minSegmentBytes);

        long levelSize = minSegmentBytes;
    long bytesLeft = totIndexBytes;
    double allowedSegCount = 0;
    while(true) {
      final double segCountLevel = bytesLeft / (double) levelSize;
      if (segCountLevel < segsPerTier) {
        allowedSegCount += Math.ceil(segCountLevel);
        break;
      }
      allowedSegCount += segsPerTier;
      bytesLeft -= segsPerTier * levelSize;
      levelSize *= maxMergeAtOnce;
    }
    int allowedSegCountInt = (int) allowedSegCount;

    MergeSpecification spec = null;

        while(true) {

      long mergingBytes = 0;

                        final List<SegmentCommitInfo> eligible = new ArrayList<SegmentCommitInfo>();
      for(int idx = tooBigCount; idx<infosSorted.size(); idx++) {
        final SegmentCommitInfo info = infosSorted.get(idx);
        if (merging.contains(info)) {
          mergingBytes += info.sizeInBytes();
        } else if (!toBeMerged.contains(info)) {
          eligible.add(info);
        }
      }

      final boolean maxMergeIsRunning = mergingBytes >= maxMergedSegmentBytes;

      if (verbose()) {
        message("  allowedSegmentCount=" + allowedSegCountInt + " vs count=" + infosSorted.size() + " (eligible count=" + eligible.size() + ") tooBigCount=" + tooBigCount);
      }

      if (eligible.size() == 0) {
        return spec;
      }

      if (eligible.size() >= allowedSegCountInt) {

                MergeScore bestScore = null;
        List<SegmentCommitInfo> best = null;
        boolean bestTooLarge = false;
        long bestMergeBytes = 0;

                for(int startIdx = 0;startIdx <= eligible.size()-maxMergeAtOnce; startIdx++) {

          long totAfterMergeBytes = 0;

          final List<SegmentCommitInfo> candidate = new ArrayList<SegmentCommitInfo>();
          boolean hitTooLarge = false;
          for(int idx = startIdx;idx<eligible.size() && candidate.size() < maxMergeAtOnce;idx++) {
            final SegmentCommitInfo info = eligible.get(idx);
            final long segBytes = size(info);

            if (totAfterMergeBytes + segBytes > maxMergedSegmentBytes) {
              hitTooLarge = true;
                                                                                                  continue;
            }
            candidate.add(info);
            totAfterMergeBytes += segBytes;
          }

          final MergeScore score = score(candidate, hitTooLarge, mergingBytes);
          if (verbose()) {
            message("  maybe=" + writer.get().segString(candidate) + " score=" + score.getScore() + " " + score.getExplanation() + " tooLarge=" + hitTooLarge + " size=" + String.format(Locale.ROOT, "%.3f MB", totAfterMergeBytes/1024./1024.));
          }

                                        if ((bestScore == null || score.getScore() < bestScore.getScore()) && (!hitTooLarge || !maxMergeIsRunning)) {
            best = candidate;
            bestScore = score;
            bestTooLarge = hitTooLarge;
            bestMergeBytes = totAfterMergeBytes;
          }
        }
        
        if (best != null) {
          if (spec == null) {
            spec = new MergeSpecification();
          }
          final OneMerge merge = new OneMerge(best);
          spec.add(merge);
          for(SegmentCommitInfo info : merge.segments) {
            toBeMerged.add(info);
          }

          if (verbose()) {
            message("  add merge=" + writer.get().segString(merge.segments) + " size=" + String.format(Locale.ROOT, "%.3f MB", bestMergeBytes/1024./1024.) + " score=" + String.format(Locale.ROOT, "%.3f", bestScore.getScore()) + " " + bestScore.getExplanation() + (bestTooLarge ? " [max merge]" : ""));
          }
        } else {
          return spec;
        }
      } else {
        return spec;
      }
    }
  }

  
  protected MergeScore score(List<SegmentCommitInfo> candidate, boolean hitTooLarge, long mergingBytes) throws IOException {
    long totBeforeMergeBytes = 0;
    long totAfterMergeBytes = 0;
    long totAfterMergeBytesFloored = 0;
    for(SegmentCommitInfo info : candidate) {
      final long segBytes = size(info);
      totAfterMergeBytes += segBytes;
      totAfterMergeBytesFloored += floorSize(segBytes);
      totBeforeMergeBytes += info.sizeInBytes();
    }

                            final double skew;
    if (hitTooLarge) {
                              skew = 1.0/maxMergeAtOnce;
    } else {
      skew = ((double) floorSize(size(candidate.get(0))))/totAfterMergeBytesFloored;
    }

            double mergeScore = skew;

                    mergeScore *= Math.pow(totAfterMergeBytes, 0.05);

        final double nonDelRatio = ((double) totAfterMergeBytes)/totBeforeMergeBytes;
    mergeScore *= Math.pow(nonDelRatio, reclaimDeletesWeight);

    final double finalMergeScore = mergeScore;

    return new MergeScore() {

      @Override
      public double getScore() {
        return finalMergeScore;
      }

      @Override
      public String getExplanation() {
        return "skew=" + String.format(Locale.ROOT, "%.3f", skew) + " nonDelRatio=" + String.format(Locale.ROOT, "%.3f", nonDelRatio);
      }
    };
  }

  @Override
  public MergeSpecification findForcedMerges(SegmentInfos infos, int maxSegmentCount, Map<SegmentCommitInfo,Boolean> segmentsToMerge) throws IOException {
    if (verbose()) {
      message("findForcedMerges maxSegmentCount=" + maxSegmentCount + " infos=" + writer.get().segString(infos) + " segmentsToMerge=" + segmentsToMerge);
    }

    List<SegmentCommitInfo> eligible = new ArrayList<SegmentCommitInfo>();
    boolean forceMergeRunning = false;
    final Collection<SegmentCommitInfo> merging = writer.get().getMergingSegments();
    boolean segmentIsOriginal = false;
    for(SegmentCommitInfo info : infos) {
      final Boolean isOriginal = segmentsToMerge.get(info);
      if (isOriginal != null) {
        segmentIsOriginal = isOriginal;
        if (!merging.contains(info)) {
          eligible.add(info);
        } else {
          forceMergeRunning = true;
        }
      }
    }

    if (eligible.size() == 0) {
      return null;
    }

    if ((maxSegmentCount > 1 && eligible.size() <= maxSegmentCount) ||
        (maxSegmentCount == 1 && eligible.size() == 1 && (!segmentIsOriginal || isMerged(infos, eligible.get(0))))) {
      if (verbose()) {
        message("already merged");
      }
      return null;
    }

    Collections.sort(eligible, new SegmentByteSizeDescending());

    if (verbose()) {
      message("eligible=" + eligible);
      message("forceMergeRunning=" + forceMergeRunning);
    }

    int end = eligible.size();
    
    MergeSpecification spec = null;

        while(end >= maxMergeAtOnceExplicit + maxSegmentCount - 1) {
      if (spec == null) {
        spec = new MergeSpecification();
      }
      final OneMerge merge = new OneMerge(eligible.subList(end-maxMergeAtOnceExplicit, end));
      if (verbose()) {
        message("add merge=" + writer.get().segString(merge.segments));
      }
      spec.add(merge);
      end -= maxMergeAtOnceExplicit;
    }

    if (spec == null && !forceMergeRunning) {
            final int numToMerge = end - maxSegmentCount + 1;
      final OneMerge merge = new OneMerge(eligible.subList(end-numToMerge, end));
      if (verbose()) {
        message("add final merge=" + merge.segString(writer.get().getDirectory()));
      }
      spec = new MergeSpecification();
      spec.add(merge);
    }

    return spec;
  }

  @Override
  public MergeSpecification findForcedDeletesMerges(SegmentInfos infos) throws IOException {
    if (verbose()) {
      message("findForcedDeletesMerges infos=" + writer.get().segString(infos) + " forceMergeDeletesPctAllowed=" + forceMergeDeletesPctAllowed);
    }
    final List<SegmentCommitInfo> eligible = new ArrayList<SegmentCommitInfo>();
    final Collection<SegmentCommitInfo> merging = writer.get().getMergingSegments();
    for(SegmentCommitInfo info : infos) {
      double pctDeletes = 100.*((double) writer.get().numDeletedDocs(info))/info.info.getDocCount();
      if (pctDeletes > forceMergeDeletesPctAllowed && !merging.contains(info)) {
        eligible.add(info);
      }
    }

    if (eligible.size() == 0) {
      return null;
    }

    Collections.sort(eligible, new SegmentByteSizeDescending());

    if (verbose()) {
      message("eligible=" + eligible);
    }

    int start = 0;
    MergeSpecification spec = null;

    while(start < eligible.size()) {
                        final int end = Math.min(start + maxMergeAtOnceExplicit, eligible.size());
      if (spec == null) {
        spec = new MergeSpecification();
      }

      final OneMerge merge = new OneMerge(eligible.subList(start, end));
      if (verbose()) {
        message("add merge=" + writer.get().segString(merge.segments));
      }
      spec.add(merge);
      start = end;
    }

    return spec;
  }

  @Override
  public void close() {
  }

  private long floorSize(long bytes) {
    return Math.max(floorSegmentBytes, bytes);
  }

  private boolean verbose() {
    final IndexWriter w = writer.get();
    return w != null && w.infoStream.isEnabled("TMP");
  }

  private void message(String message) {
    writer.get().infoStream.message("TMP", message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[" + getClass().getSimpleName() + ": ");
    sb.append("maxMergeAtOnce=").append(maxMergeAtOnce).append(", ");
    sb.append("maxMergeAtOnceExplicit=").append(maxMergeAtOnceExplicit).append(", ");
    sb.append("maxMergedSegmentMB=").append(maxMergedSegmentBytes/1024/1024.).append(", ");
    sb.append("floorSegmentMB=").append(floorSegmentBytes/1024/1024.).append(", ");
    sb.append("forceMergeDeletesPctAllowed=").append(forceMergeDeletesPctAllowed).append(", ");
    sb.append("segmentsPerTier=").append(segsPerTier).append(", ");
    sb.append("maxCFSSegmentSizeMB=").append(getMaxCFSSegmentSizeMB()).append(", ");
    sb.append("noCFSRatio=").append(noCFSRatio);
    return sb.toString();
  }
}
