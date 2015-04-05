package com.checkpoint.vaiol;

public class SourceExample {
    private static String string = "package org.apache.lucene.index;\n" +
            "\n" +
            "/*\n" +
            " * Licensed to the Apache Software Foundation (ASF) under one or more\n" +
            " * contributor license agreements.  See the NOTICE file distributed with\n" +
            " * this work for additional information regarding copyright ownership.\n" +
            " * The ASF licenses this file to You under the Apache License, Version 2.0\n" +
            " * (the \"License\"); you may not use this file except in compliance with\n" +
            " * the License.  You may obtain a copy of the License at\n" +
            " *\n" +
            " *     http://www.apache.org/licenses/LICENSE-2.0\n" +
            " *\n" +
            " * Unless required by applicable law or agreed to in writing, software\n" +
            " * distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
            " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
            " * See the License for the specific language governing permissions and\n" +
            " * limitations under the License.\n" +
            " */\n" +
            "\n" +
            "import java.io.IOException;\n" +
            "import java.util.Locale;\n" +
            "import java.util.Map;\n" +
            "import java.util.Collection;\n" +
            "import java.util.Collections;\n" +
            "import java.util.HashSet;\n" +
            "import java.util.Comparator;\n" +
            "import java.util.List;\n" +
            "import java.util.ArrayList;\n" +
            "\n" +
            "/**\n" +
            " *  Merges segments of approximately equal size, subject to\n" +
            " *  an allowed number of segments per tier.  This is similar\n" +
            " *  to {@link LogByteSizeMergePolicy}, except this merge\n" +
            " *  policy is able to merge non-adjacent segment, and\n" +
            " *  separates how many segments are merged at once ({@link\n" +
            " *  #setMaxMergeAtOnce}) from how many segments are allowed\n" +
            " *  per tier ({@link #setSegmentsPerTier}).  This merge\n" +
            " *  policy also does not over-merge (i.e. cascade merges). \n" +
            " *\n" +
            " *  <p>For normal merging, this policy first computes a\n" +
            " *  \"budget\" of how many segments are allowed to be in the\n" +
            " *  index.  If the index is over-budget, then the policy\n" +
            " *  sorts segments by decreasing size (pro-rating by percent\n" +
            " *  deletes), and then finds the least-cost merge.  Merge\n" +
            " *  cost is measured by a combination of the \"skew\" of the\n" +
            " *  merge (size of largest segment divided by smallest segment),\n" +
            " *  total merge size and percent deletes reclaimed,\n" +
            " *  so that merges with lower skew, smaller size\n" +
            " *  and those reclaiming more deletes, are\n" +
            " *  favored.\n" +
            " *\n" +
            " *  <p>If a merge will produce a segment that's larger than\n" +
            " *  {@link #setMaxMergedSegmentMB}, then the policy will\n" +
            " *  merge fewer segments (down to 1 at once, if that one has\n" +
            " *  deletions) to keep the segment size under budget.\n" +
            " *      \n" +
            " *  <p><b>NOTE</b>: this policy freely merges non-adjacent\n" +
            " *  segments; if this is a problem, use {@link\n" +
            " *  LogMergePolicy}.\n" +
            " *\n" +
            " *  <p><b>NOTE</b>: This policy always merges by byte size\n" +
            " *  of the segments, always pro-rates by percent deletes,\n" +
            " *  and does not apply any maximum segment size during\n" +
            " *  forceMerge (unlike {@link LogByteSizeMergePolicy}).\n" +
            " *\n" +
            " *  @lucene.experimental\n" +
            " */\n" +
            "\n" +
            "// TODO\n" +
            "//   - we could try to take into account whether a large\n" +
            "//     merge is already running (under CMS) and then bias\n" +
            "//     ourselves towards picking smaller merges if so (or,\n" +
            "//     maybe CMS should do so)\n" +
            "\n" +
            "public class TieredMergePolicy extends MergePolicy {\n" +
            "  /** Default noCFSRatio.  If a merge's size is >= 10% of\n" +
            "   *  the index, then we disable compound file for it.\n" +
            "   *  @see MergePolicy#setNoCFSRatio */\n" +
            "  public static final double DEFAULT_NO_CFS_RATIO = 0.1;\n" +
            "  \n" +
            "  private int maxMergeAtOnce = 10;\n" +
            "  private long maxMergedSegmentBytes = 5*1024*1024*1024L;\n" +
            "  private int maxMergeAtOnceExplicit = 30;\n" +
            "\n" +
            "  private long floorSegmentBytes = 2*1024*1024L;\n" +
            "  private double segsPerTier = 10.0;\n" +
            "  private double forceMergeDeletesPctAllowed = 10.0;\n" +
            "  private double reclaimDeletesWeight = 2.0;\n" +
            "\n" +
            "  /** Sole constructor, setting all settings to their\n" +
            "   *  defaults. */\n" +
            "  public TieredMergePolicy() {\n" +
            "    super(DEFAULT_NO_CFS_RATIO, MergePolicy.DEFAULT_MAX_CFS_SEGMENT_SIZE);\n" +
            "  }\n" +
            "\n" +
            "  /** Maximum number of segments to be merged at a time\n" +
            "   *  during \"normal\" merging.  For explicit merging (eg,\n" +
            "   *  forceMerge or forceMergeDeletes was called), see {@link\n" +
            "   *  #setMaxMergeAtOnceExplicit}.  Default is 10. */\n" +
            "  public TieredMergePolicy setMaxMergeAtOnce(int v) {\n" +
            "    if (v < 2) {\n" +
            "      throw new IllegalArgumentException(\"maxMergeAtOnce must be > 1 (got \" + v + \")\");\n" +
            "    }\n" +
            "    maxMergeAtOnce = v;\n" +
            "    return this;\n" +
            "  }\n" +
            "\n" +
            "  /** Returns the current maxMergeAtOnce setting.\n" +
            "   *\n" +
            "   * @see #setMaxMergeAtOnce */\n" +
            "  public int getMaxMergeAtOnce() {\n" +
            "    return maxMergeAtOnce;\n" +
            "  }\n" +
            "\n" +
            "  // TODO: should addIndexes do explicit merging, too?  And,\n" +
            "  // if user calls IW.maybeMerge \"explicitly\"\n" +
            "\n" +
            "  /** Maximum number of segments to be merged at a time,\n" +
            "   *  during forceMerge or forceMergeDeletes. Default is 30. */\n" +
            "  public TieredMergePolicy setMaxMergeAtOnceExplicit(int v) {\n" +
            "    if (v < 2) {\n" +
            "      throw new IllegalArgumentException(\"maxMergeAtOnceExplicit must be > 1 (got \" + v + \")\");\n" +
            "    }\n" +
            "    maxMergeAtOnceExplicit = v;\n" +
            "    return this;\n" +
            "  }\n" +
            "\n" +
            "  /** Returns the current maxMergeAtOnceExplicit setting.\n" +
            "   *\n" +
            "   * @see #setMaxMergeAtOnceExplicit */\n" +
            "  public int getMaxMergeAtOnceExplicit() {\n" +
            "    return maxMergeAtOnceExplicit;\n" +
            "  }\n" +
            "\n" +
            "  /** Maximum sized segment to produce during\n" +
            "   *  normal merging.  This setting is approximate: the\n" +
            "   *  estimate of the merged segment size is made by summing\n" +
            "   *  sizes of to-be-merged segments (compensating for\n" +
            "   *  percent deleted docs).  Default is 5 GB. */\n" +
            "  public TieredMergePolicy setMaxMergedSegmentMB(double v) {\n" +
            "    if (v < 0.0) {\n" +
            "      throw new IllegalArgumentException(\"maxMergedSegmentMB must be >=0 (got \" + v + \")\");\n" +
            "    }\n" +
            "    v *= 1024 * 1024;\n" +
            "    maxMergedSegmentBytes = (v > Long.MAX_VALUE) ? Long.MAX_VALUE : (long) v;\n" +
            "    return this;\n" +
            "  }\n" +
            "\n" +
            "  /** Returns the current maxMergedSegmentMB setting.\n" +
            "   *\n" +
            "   * @see #getMaxMergedSegmentMB */\n" +
            "  public double getMaxMergedSegmentMB() {\n" +
            "    return maxMergedSegmentBytes/1024/1024.;\n" +
            "  }\n" +
            "\n" +
            "  /** Controls how aggressively merges that reclaim more\n" +
            "   *  deletions are favored.  Higher values will more\n" +
            "   *  aggressively target merges that reclaim deletions, but\n" +
            "   *  be careful not to go so high that way too much merging\n" +
            "   *  takes place; a value of 3.0 is probably nearly too\n" +
            "   *  high.  A value of 0.0 means deletions don't impact\n" +
            "   *  merge selection. */ \n" +
            "  public TieredMergePolicy setReclaimDeletesWeight(double v) {\n" +
            "    if (v < 0.0) {\n" +
            "      throw new IllegalArgumentException(\"reclaimDeletesWeight must be >= 0.0 (got \" + v + \")\");\n" +
            "    }\n" +
            "    reclaimDeletesWeight = v;\n" +
            "    return this;\n" +
            "  }\n" +
            "\n" +
            "  /** See {@link #setReclaimDeletesWeight}. */\n" +
            "  public double getReclaimDeletesWeight() {\n" +
            "    return reclaimDeletesWeight;\n" +
            "  }\n" +
            "\n" +
            "  /** Segments smaller than this are \"rounded up\" to this\n" +
            "   *  size, ie treated as equal (floor) size for merge\n" +
            "   *  selection.  This is to prevent frequent flushing of\n" +
            "   *  tiny segments from allowing a long tail in the index.\n" +
            "   *  Default is 2 MB. */\n" +
            "  public TieredMergePolicy setFloorSegmentMB(double v) {\n" +
            "    if (v <= 0.0) {\n" +
            "      throw new IllegalArgumentException(\"floorSegmentMB must be >= 0.0 (got \" + v + \")\");\n" +
            "    }\n" +
            "    v *= 1024 * 1024;\n" +
            "    floorSegmentBytes = (v > Long.MAX_VALUE) ? Long.MAX_VALUE : (long) v;\n" +
            "    return this;\n" +
            "  }\n" +
            "\n" +
            "  /** Returns the current floorSegmentMB.\n" +
            "   *\n" +
            "   *  @see #setFloorSegmentMB */\n" +
            "  public double getFloorSegmentMB() {\n" +
            "    return floorSegmentBytes/(1024*1024.);\n" +
            "  }\n" +
            "\n" +
            "  /** When forceMergeDeletes is called, we only merge away a\n" +
            "   *  segment if its delete percentage is over this\n" +
            "   *  threshold.  Default is 10%. */ \n" +
            "  public TieredMergePolicy setForceMergeDeletesPctAllowed(double v) {\n" +
            "    if (v < 0.0 || v > 100.0) {\n" +
            "      throw new IllegalArgumentException(\"forceMergeDeletesPctAllowed must be between 0.0 and 100.0 inclusive (got \" + v + \")\");\n" +
            "    }\n" +
            "    forceMergeDeletesPctAllowed = v;\n" +
            "    return this;\n" +
            "  }\n" +
            "\n" +
            "  /** Returns the current forceMergeDeletesPctAllowed setting.\n" +
            "   *\n" +
            "   * @see #setForceMergeDeletesPctAllowed */\n" +
            "  public double getForceMergeDeletesPctAllowed() {\n" +
            "    return forceMergeDeletesPctAllowed;\n" +
            "  }\n" +
            "\n" +
            "  /** Sets the allowed number of segments per tier.  Smaller\n" +
            "   *  values mean more merging but fewer segments.\n" +
            "   *\n" +
            "   *  <p><b>NOTE</b>: this value should be >= the {@link\n" +
            "   *  #setMaxMergeAtOnce} otherwise you'll force too much\n" +
            "   *  merging to occur.</p>\n" +
            "   *\n" +
            "   *  <p>Default is 10.0.</p> */\n" +
            "  public TieredMergePolicy setSegmentsPerTier(double v) {\n" +
            "    if (v < 2.0) {\n" +
            "      throw new IllegalArgumentException(\"segmentsPerTier must be >= 2.0 (got \" + v + \")\");\n" +
            "    }\n" +
            "    segsPerTier = v;\n" +
            "    return this;\n" +
            "  }\n" +
            "\n" +
            "  /** Returns the current segmentsPerTier setting.\n" +
            "   *\n" +
            "   * @see #setSegmentsPerTier */\n" +
            "  public double getSegmentsPerTier() {\n" +
            "    return segsPerTier;\n" +
            "  }\n" +
            "\n" +
            "  private class SegmentByteSizeDescending implements Comparator<SegmentCommitInfo> {\n" +
            "    @Override\n" +
            "    public int compare(SegmentCommitInfo o1, SegmentCommitInfo o2) {\n" +
            "      try {\n" +
            "        final long sz1 = size(o1);\n" +
            "        final long sz2 = size(o2);\n" +
            "        if (sz1 > sz2) {\n" +
            "          return -1;\n" +
            "        } else if (sz2 > sz1) {\n" +
            "          return 1;\n" +
            "        } else {\n" +
            "          return o1.info.name.compareTo(o2.info.name);\n" +
            "        }\n" +
            "      } catch (IOException ioe) {\n" +
            "        throw new RuntimeException(ioe);\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  /** Holds score and explanation for a single candidate\n" +
            "   *  merge. */\n" +
            "  protected static abstract class MergeScore {\n" +
            "    /** Sole constructor. (For invocation by subclass \n" +
            "     *  constructors, typically implicit.) */\n" +
            "    protected MergeScore() {\n" +
            "    }\n" +
            "    \n" +
            "    /** Returns the score for this merge candidate; lower\n" +
            "     *  scores are better. */\n" +
            "    abstract double getScore();\n" +
            "\n" +
            "    /** Human readable explanation of how the merge got this\n" +
            "     *  score. */\n" +
            "    abstract String getExplanation();\n" +
            "  }\n" +
            "\n" +
            "  @Override\n" +
            "  public MergeSpecification findMerges(MergeTrigger mergeTrigger, SegmentInfos infos) throws IOException {\n" +
            "    if (verbose()) {\n" +
            "      message(\"findMerges: \" + infos.size() + \" segments\");\n" +
            "    }\n" +
            "    if (infos.size() == 0) {\n" +
            "      return null;\n" +
            "    }\n" +
            "    final Collection<SegmentCommitInfo> merging = writer.get().getMergingSegments();\n" +
            "    final Collection<SegmentCommitInfo> toBeMerged = new HashSet<SegmentCommitInfo>();\n" +
            "\n" +
            "    final List<SegmentCommitInfo> infosSorted = new ArrayList<SegmentCommitInfo>(infos.asList());\n" +
            "    Collections.sort(infosSorted, new SegmentByteSizeDescending());\n" +
            "\n" +
            "    // Compute total index bytes & print details about the index\n" +
            "    long totIndexBytes = 0;\n" +
            "    long minSegmentBytes = Long.MAX_VALUE;\n" +
            "    for(SegmentCommitInfo info : infosSorted) {\n" +
            "      final long segBytes = size(info);\n" +
            "      if (verbose()) {\n" +
            "        String extra = merging.contains(info) ? \" [merging]\" : \"\";\n" +
            "        if (segBytes >= maxMergedSegmentBytes/2.0) {\n" +
            "          extra += \" [skip: too large]\";\n" +
            "        } else if (segBytes < floorSegmentBytes) {\n" +
            "          extra += \" [floored]\";\n" +
            "        }\n" +
            "        message(\"  seg=\" + writer.get().segString(info) + \" size=\" + String.format(Locale.ROOT, \"%.3f\", segBytes/1024/1024.) + \" MB\" + extra);\n" +
            "      }\n" +
            "\n" +
            "      minSegmentBytes = Math.min(segBytes, minSegmentBytes);\n" +
            "      // Accum total byte size\n" +
            "      totIndexBytes += segBytes;\n" +
            "    }\n" +
            "\n" +
            "    // If we have too-large segments, grace them out\n" +
            "    // of the maxSegmentCount:\n" +
            "    int tooBigCount = 0;\n" +
            "    while (tooBigCount < infosSorted.size() && size(infosSorted.get(tooBigCount)) >= maxMergedSegmentBytes/2.0) {\n" +
            "      totIndexBytes -= size(infosSorted.get(tooBigCount));\n" +
            "      tooBigCount++;\n" +
            "    }\n" +
            "\n" +
            "    minSegmentBytes = floorSize(minSegmentBytes);\n" +
            "\n" +
            "    // Compute max allowed segs in the index\n" +
            "    long levelSize = minSegmentBytes;\n" +
            "    long bytesLeft = totIndexBytes;\n" +
            "    double allowedSegCount = 0;\n" +
            "    while(true) {\n" +
            "      final double segCountLevel = bytesLeft / (double) levelSize;\n" +
            "      if (segCountLevel < segsPerTier) {\n" +
            "        allowedSegCount += Math.ceil(segCountLevel);\n" +
            "        break;\n" +
            "      }\n" +
            "      allowedSegCount += segsPerTier;\n" +
            "      bytesLeft -= segsPerTier * levelSize;\n" +
            "      levelSize *= maxMergeAtOnce;\n" +
            "    }\n" +
            "    int allowedSegCountInt = (int) allowedSegCount;\n" +
            "\n" +
            "    MergeSpecification spec = null;\n" +
            "\n" +
            "    // Cycle to possibly select more than one merge:\n" +
            "    while(true) {\n" +
            "\n" +
            "      long mergingBytes = 0;\n" +
            "\n" +
            "      // Gather eligible segments for merging, ie segments\n" +
            "      // not already being merged and not already picked (by\n" +
            "      // prior iteration of this loop) for merging:\n" +
            "      final List<SegmentCommitInfo> eligible = new ArrayList<SegmentCommitInfo>();\n" +
            "      for(int idx = tooBigCount; idx<infosSorted.size(); idx++) {\n" +
            "        final SegmentCommitInfo info = infosSorted.get(idx);\n" +
            "        if (merging.contains(info)) {\n" +
            "          mergingBytes += info.sizeInBytes();\n" +
            "        } else if (!toBeMerged.contains(info)) {\n" +
            "          eligible.add(info);\n" +
            "        }\n" +
            "      }\n" +
            "\n" +
            "      final boolean maxMergeIsRunning = mergingBytes >= maxMergedSegmentBytes;\n" +
            "\n" +
            "      if (verbose()) {\n" +
            "        message(\"  allowedSegmentCount=\" + allowedSegCountInt + \" vs count=\" + infosSorted.size() + \" (eligible count=\" + eligible.size() + \") tooBigCount=\" + tooBigCount);\n" +
            "      }\n" +
            "\n" +
            "      if (eligible.size() == 0) {\n" +
            "        return spec;\n" +
            "      }\n" +
            "\n" +
            "      if (eligible.size() >= allowedSegCountInt) {\n" +
            "\n" +
            "        // OK we are over budget -- find best merge!\n" +
            "        MergeScore bestScore = null;\n" +
            "        List<SegmentCommitInfo> best = null;\n" +
            "        boolean bestTooLarge = false;\n" +
            "        long bestMergeBytes = 0;\n" +
            "\n" +
            "        // Consider all merge starts:\n" +
            "        for(int startIdx = 0;startIdx <= eligible.size()-maxMergeAtOnce; startIdx++) {\n" +
            "\n" +
            "          long totAfterMergeBytes = 0;\n" +
            "\n" +
            "          final List<SegmentCommitInfo> candidate = new ArrayList<SegmentCommitInfo>();\n" +
            "          boolean hitTooLarge = false;\n" +
            "          for(int idx = startIdx;idx<eligible.size() && candidate.size() < maxMergeAtOnce;idx++) {\n" +
            "            final SegmentCommitInfo info = eligible.get(idx);\n" +
            "            final long segBytes = size(info);\n" +
            "\n" +
            "            if (totAfterMergeBytes + segBytes > maxMergedSegmentBytes) {\n" +
            "              hitTooLarge = true;\n" +
            "              // NOTE: we continue, so that we can try\n" +
            "              // \"packing\" smaller segments into this merge\n" +
            "              // to see if we can get closer to the max\n" +
            "              // size; this in general is not perfect since\n" +
            "              // this is really \"bin packing\" and we'd have\n" +
            "              // to try different permutations.\n" +
            "              continue;\n" +
            "            }\n" +
            "            candidate.add(info);\n" +
            "            totAfterMergeBytes += segBytes;\n" +
            "          }\n" +
            "\n" +
            "          final MergeScore score = score(candidate, hitTooLarge, mergingBytes);\n" +
            "          if (verbose()) {\n" +
            "            message(\"  maybe=\" + writer.get().segString(candidate) + \" score=\" + score.getScore() + \" \" + score.getExplanation() + \" tooLarge=\" + hitTooLarge + \" size=\" + String.format(Locale.ROOT, \"%.3f MB\", totAfterMergeBytes/1024./1024.));\n" +
            "          }\n" +
            "\n" +
            "          // If we are already running a max sized merge\n" +
            "          // (maxMergeIsRunning), don't allow another max\n" +
            "          // sized merge to kick off:\n" +
            "          if ((bestScore == null || score.getScore() < bestScore.getScore()) && (!hitTooLarge || !maxMergeIsRunning)) {\n" +
            "            best = candidate;\n" +
            "            bestScore = score;\n" +
            "            bestTooLarge = hitTooLarge;\n" +
            "            bestMergeBytes = totAfterMergeBytes;\n" +
            "          }\n" +
            "        }\n" +
            "        \n" +
            "        if (best != null) {\n" +
            "          if (spec == null) {\n" +
            "            spec = new MergeSpecification();\n" +
            "          }\n" +
            "          final OneMerge merge = new OneMerge(best);\n" +
            "          spec.add(merge);\n" +
            "          for(SegmentCommitInfo info : merge.segments) {\n" +
            "            toBeMerged.add(info);\n" +
            "          }\n" +
            "\n" +
            "          if (verbose()) {\n" +
            "            message(\"  add merge=\" + writer.get().segString(merge.segments) + \" size=\" + String.format(Locale.ROOT, \"%.3f MB\", bestMergeBytes/1024./1024.) + \" score=\" + String.format(Locale.ROOT, \"%.3f\", bestScore.getScore()) + \" \" + bestScore.getExplanation() + (bestTooLarge ? \" [max merge]\" : \"\"));\n" +
            "          }\n" +
            "        } else {\n" +
            "          return spec;\n" +
            "        }\n" +
            "      } else {\n" +
            "        return spec;\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  /** Expert: scores one merge; subclasses can override. */\n" +
            "  protected MergeScore score(List<SegmentCommitInfo> candidate, boolean hitTooLarge, long mergingBytes) throws IOException {\n" +
            "    long totBeforeMergeBytes = 0;\n" +
            "    long totAfterMergeBytes = 0;\n" +
            "    long totAfterMergeBytesFloored = 0;\n" +
            "    for(SegmentCommitInfo info : candidate) {\n" +
            "      final long segBytes = size(info);\n" +
            "      totAfterMergeBytes += segBytes;\n" +
            "      totAfterMergeBytesFloored += floorSize(segBytes);\n" +
            "      totBeforeMergeBytes += info.sizeInBytes();\n" +
            "    }\n" +
            "\n" +
            "    // Roughly measure \"skew\" of the merge, i.e. how\n" +
            "    // \"balanced\" the merge is (whether the segments are\n" +
            "    // about the same size), which can range from\n" +
            "    // 1.0/numSegsBeingMerged (good) to 1.0 (poor). Heavily\n" +
            "    // lopsided merges (skew near 1.0) is no good; it means\n" +
            "    // O(N^2) merge cost over time:\n" +
            "    final double skew;\n" +
            "    if (hitTooLarge) {\n" +
            "      // Pretend the merge has perfect skew; skew doesn't\n" +
            "      // matter in this case because this merge will not\n" +
            "      // \"cascade\" and so it cannot lead to N^2 merge cost\n" +
            "      // over time:\n" +
            "      skew = 1.0/maxMergeAtOnce;\n" +
            "    } else {\n" +
            "      skew = ((double) floorSize(size(candidate.get(0))))/totAfterMergeBytesFloored;\n" +
            "    }\n" +
            "\n" +
            "    // Strongly favor merges with less skew (smaller\n" +
            "    // mergeScore is better):\n" +
            "    double mergeScore = skew;\n" +
            "\n" +
            "    // Gently favor smaller merges over bigger ones.  We\n" +
            "    // don't want to make this exponent too large else we\n" +
            "    // can end up doing poor merges of small segments in\n" +
            "    // order to avoid the large merges:\n" +
            "    mergeScore *= Math.pow(totAfterMergeBytes, 0.05);\n" +
            "\n" +
            "    // Strongly favor merges that reclaim deletes:\n" +
            "    final double nonDelRatio = ((double) totAfterMergeBytes)/totBeforeMergeBytes;\n" +
            "    mergeScore *= Math.pow(nonDelRatio, reclaimDeletesWeight);\n" +
            "\n" +
            "    final double finalMergeScore = mergeScore;\n" +
            "\n" +
            "    return new MergeScore() {\n" +
            "\n" +
            "      @Override\n" +
            "      public double getScore() {\n" +
            "        return finalMergeScore;\n" +
            "      }\n" +
            "\n" +
            "      @Override\n" +
            "      public String getExplanation() {\n" +
            "        return \"skew=\" + String.format(Locale.ROOT, \"%.3f\", skew) + \" nonDelRatio=\" + String.format(Locale.ROOT, \"%.3f\", nonDelRatio);\n" +
            "      }\n" +
            "    };\n" +
            "  }\n" +
            "\n" +
            "  @Override\n" +
            "  public MergeSpecification findForcedMerges(SegmentInfos infos, int maxSegmentCount, Map<SegmentCommitInfo,Boolean> segmentsToMerge) throws IOException {\n" +
            "    if (verbose()) {\n" +
            "      message(\"findForcedMerges maxSegmentCount=\" + maxSegmentCount + \" infos=\" + writer.get().segString(infos) + \" segmentsToMerge=\" + segmentsToMerge);\n" +
            "    }\n" +
            "\n" +
            "    List<SegmentCommitInfo> eligible = new ArrayList<SegmentCommitInfo>();\n" +
            "    boolean forceMergeRunning = false;\n" +
            "    final Collection<SegmentCommitInfo> merging = writer.get().getMergingSegments();\n" +
            "    boolean segmentIsOriginal = false;\n" +
            "    for(SegmentCommitInfo info : infos) {\n" +
            "      final Boolean isOriginal = segmentsToMerge.get(info);\n" +
            "      if (isOriginal != null) {\n" +
            "        segmentIsOriginal = isOriginal;\n" +
            "        if (!merging.contains(info)) {\n" +
            "          eligible.add(info);\n" +
            "        } else {\n" +
            "          forceMergeRunning = true;\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    if (eligible.size() == 0) {\n" +
            "      return null;\n" +
            "    }\n" +
            "\n" +
            "    if ((maxSegmentCount > 1 && eligible.size() <= maxSegmentCount) ||\n" +
            "        (maxSegmentCount == 1 && eligible.size() == 1 && (!segmentIsOriginal || isMerged(infos, eligible.get(0))))) {\n" +
            "      if (verbose()) {\n" +
            "        message(\"already merged\");\n" +
            "      }\n" +
            "      return null;\n" +
            "    }\n" +
            "\n" +
            "    Collections.sort(eligible, new SegmentByteSizeDescending());\n" +
            "\n" +
            "    if (verbose()) {\n" +
            "      message(\"eligible=\" + eligible);\n" +
            "      message(\"forceMergeRunning=\" + forceMergeRunning);\n" +
            "    }\n" +
            "\n" +
            "    int end = eligible.size();\n" +
            "    \n" +
            "    MergeSpecification spec = null;\n" +
            "\n" +
            "    // Do full merges, first, backwards:\n" +
            "    while(end >= maxMergeAtOnceExplicit + maxSegmentCount - 1) {\n" +
            "      if (spec == null) {\n" +
            "        spec = new MergeSpecification();\n" +
            "      }\n" +
            "      final OneMerge merge = new OneMerge(eligible.subList(end-maxMergeAtOnceExplicit, end));\n" +
            "      if (verbose()) {\n" +
            "        message(\"add merge=\" + writer.get().segString(merge.segments));\n" +
            "      }\n" +
            "      spec.add(merge);\n" +
            "      end -= maxMergeAtOnceExplicit;\n" +
            "    }\n" +
            "\n" +
            "    if (spec == null && !forceMergeRunning) {\n" +
            "      // Do final merge\n" +
            "      final int numToMerge = end - maxSegmentCount + 1;\n" +
            "      final OneMerge merge = new OneMerge(eligible.subList(end-numToMerge, end));\n" +
            "      if (verbose()) {\n" +
            "        message(\"add final merge=\" + merge.segString(writer.get().getDirectory()));\n" +
            "      }\n" +
            "      spec = new MergeSpecification();\n" +
            "      spec.add(merge);\n" +
            "    }\n" +
            "\n" +
            "    return spec;\n" +
            "  }\n" +
            "\n" +
            "  @Override\n" +
            "  public MergeSpecification findForcedDeletesMerges(SegmentInfos infos) throws IOException {\n" +
            "    if (verbose()) {\n" +
            "      message(\"findForcedDeletesMerges infos=\" + writer.get().segString(infos) + \" forceMergeDeletesPctAllowed=\" + forceMergeDeletesPctAllowed);\n" +
            "    }\n" +
            "    final List<SegmentCommitInfo> eligible = new ArrayList<SegmentCommitInfo>();\n" +
            "    final Collection<SegmentCommitInfo> merging = writer.get().getMergingSegments();\n" +
            "    for(SegmentCommitInfo info : infos) {\n" +
            "      double pctDeletes = 100.*((double) writer.get().numDeletedDocs(info))/info.info.getDocCount();\n" +
            "      if (pctDeletes > forceMergeDeletesPctAllowed && !merging.contains(info)) {\n" +
            "        eligible.add(info);\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    if (eligible.size() == 0) {\n" +
            "      return null;\n" +
            "    }\n" +
            "\n" +
            "    Collections.sort(eligible, new SegmentByteSizeDescending());\n" +
            "\n" +
            "    if (verbose()) {\n" +
            "      message(\"eligible=\" + eligible);\n" +
            "    }\n" +
            "\n" +
            "    int start = 0;\n" +
            "    MergeSpecification spec = null;\n" +
            "\n" +
            "    while(start < eligible.size()) {\n" +
            "      // Don't enforce max merged size here: app is explicitly\n" +
            "      // calling forceMergeDeletes, and knows this may take a\n" +
            "      // long time / produce big segments (like forceMerge):\n" +
            "      final int end = Math.min(start + maxMergeAtOnceExplicit, eligible.size());\n" +
            "      if (spec == null) {\n" +
            "        spec = new MergeSpecification();\n" +
            "      }\n" +
            "\n" +
            "      final OneMerge merge = new OneMerge(eligible.subList(start, end));\n" +
            "      if (verbose()) {\n" +
            "        message(\"add merge=\" + writer.get().segString(merge.segments));\n" +
            "      }\n" +
            "      spec.add(merge);\n" +
            "      start = end;\n" +
            "    }\n" +
            "\n" +
            "    return spec;\n" +
            "  }\n" +
            "\n" +
            "  @Override\n" +
            "  public void close() {\n" +
            "  }\n" +
            "\n" +
            "  private long floorSize(long bytes) {\n" +
            "    return Math.max(floorSegmentBytes, bytes);\n" +
            "  }\n" +
            "\n" +
            "  private boolean verbose() {\n" +
            "    final IndexWriter w = writer.get();\n" +
            "    return w != null && w.infoStream.isEnabled(\"TMP\");\n" +
            "  }\n" +
            "\n" +
            "  private void message(String message) {\n" +
            "    writer.get().infoStream.message(\"TMP\", message);\n" +
            "  }\n" +
            "\n" +
            "  @Override\n" +
            "  public String toString() {\n" +
            "    StringBuilder sb = new StringBuilder(\"[\" + getClass().getSimpleName() + \": \");\n" +
            "    sb.append(\"maxMergeAtOnce=\").append(maxMergeAtOnce).append(\", \");\n" +
            "    sb.append(\"maxMergeAtOnceExplicit=\").append(maxMergeAtOnceExplicit).append(\", \");\n" +
            "    sb.append(\"maxMergedSegmentMB=\").append(maxMergedSegmentBytes/1024/1024.).append(\", \");\n" +
            "    sb.append(\"floorSegmentMB=\").append(floorSegmentBytes/1024/1024.).append(\", \");\n" +
            "    sb.append(\"forceMergeDeletesPctAllowed=\").append(forceMergeDeletesPctAllowed).append(\", \");\n" +
            "    sb.append(\"segmentsPerTier=\").append(segsPerTier).append(\", \");\n" +
            "    sb.append(\"maxCFSSegmentSizeMB=\").append(getMaxCFSSegmentSizeMB()).append(\", \");\n" +
            "    sb.append(\"noCFSRatio=\").append(noCFSRatio);\n" +
            "    return sb.toString();\n" +
            "  }\n" +
            "}\n";


        public static String getString() {
                return string;
        }
}
