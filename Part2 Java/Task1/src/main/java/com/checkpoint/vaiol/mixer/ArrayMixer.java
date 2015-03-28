package com.checkpoint.vaiol.mixer;

import java.util.*;

public class ArrayMixer {

    private double percent;

    public ArrayMixer(int percent) throws CreateMixerException {
        if(percent < 0 || percent > 100) {
            throw new CreateMixerException("Mixing percentage should be from 0 to 100");
        }
        this.percent = percent / 100.0;
    }

    public Object[] shuffle(Object[] array) {
        //определяем количество елементов которые должны поменять свои места:
        int shuffledLength = (int) (array.length * percent);
        //если количество элментов меньше 2, значит не получится ни одной перестановки:
        if(shuffledLength < 2) {
            return array;
        }

        //Рандомно определяем индексы элементов которые будут перемещаны:
        Set<Integer> indicesSet = new HashSet<Integer>();
        Random rand = new Random();
        while(indicesSet.size() != shuffledLength) {
            indicesSet.add(rand.nextInt(array.length));
        }
        List<Integer> indices = new ArrayList<Integer>(indicesSet);



        //создаем пары индексов для перестановок, рандомно и без повторений:
        List<PairOfIndices> pairOfIndicesList = new ArrayList<PairOfIndices>();
        while(indices.size() > 1) {
            int member1 = indices.remove(rand.nextInt(indices.size()));
            int member2 = indices.remove(rand.nextInt(indices.size()));
            pairOfIndicesList.add(new PairOfIndices(member1, member2));
        }
        //создаем пару для последнего элмента, если количество элементов непарное:
        if(indices.size() == 1) {
            int member1 = indices.remove(0);
            int member2 = -1;
            for(int member : indicesSet) {
                if(member != member1) {
                    member2 = member;
                }
            }
            pairOfIndicesList.add(new PairOfIndices(member1, member2));
        }

        //создаем новый результирующий массив
        Object[] newArray = new Object[array.length];
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = array[i];
        }

        //выполняем перестановки:
        for (PairOfIndices pairOfIndices : pairOfIndicesList) {
            Object tmp = newArray[pairOfIndices.getMember1()];
            newArray[pairOfIndices.getMember1()] = newArray[pairOfIndices.getMember2()];
            newArray[pairOfIndices.getMember2()] = tmp;
        }
        return newArray;
    }

    public Object[][] shuffle(Object[][] array) {
        //определяем количество елементов в массиве
        int countOfElement = 0;
        for (Object[] arrayStrip : array) {
            countOfElement += arrayStrip.length;
        }
        //определяем количество елементов которые должны поменять свои места:
        int shuffledLength = (int) (countOfElement * percent);

        //если количество элментов меньше 2, значит не получится ни одной перестановки:
        if(shuffledLength < 2) {
            return array;
        }

        //Рандомно определяем индексы элементов которые будут перемещаны:
        Set<PairOfIndices> indicesSet = new HashSet<PairOfIndices>();
        Random rand = new Random();
        while(indicesSet.size() != shuffledLength) {
            int member1 = rand.nextInt(array.length);
            int member2 = rand.nextInt(array[member1].length);
            indicesSet.add(new PairOfIndices(member1, member2));
        }
        List<PairOfIndices> indices = new ArrayList<PairOfIndices>(indicesSet);



        //создаем пары для перестановок, рандомно и без повторений:
        List<PairOfPairIndices> pairOfIndicesList = new ArrayList<PairOfPairIndices>();
        while(indices.size() > 1) {
            PairOfIndices pair1 = indices.remove(rand.nextInt(indices.size()));
            PairOfIndices pair2 = indices.remove(rand.nextInt(indices.size()));
            pairOfIndicesList.add(new PairOfPairIndices(pair1, pair2));
        }
        //создаем пару для последнего элмента, если количество элементов непарное:
        if(indices.size() == 1) {
            PairOfIndices pair1 = indices.remove(0);
            PairOfIndices pair2 = null;
            for(PairOfIndices pair : indicesSet) {
                if(pair != pair1) {
                    pair2 = pair;
                }
            }
            pairOfIndicesList.add(new PairOfPairIndices(pair1, pair2));
        }

        //создаем новый результирующий массив
        Object[][] newArray = new Object[array.length][];
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = new Object[array[i].length];
            for (int j = 0; j < newArray[i].length; j++) {
                newArray[i][j] = array[i][j];
            }
        }

        //выполняем перестановки:
        for (PairOfPairIndices pairOfIndices : pairOfIndicesList) {

            int pair1Index1 = pairOfIndices.getPair1().getMember1();
            int pair1Index2 = pairOfIndices.getPair1().getMember2();
            int pair2Index1 = pairOfIndices.getPair2().getMember1();
            int pair2Index2 = pairOfIndices.getPair2().getMember2();

            Object tmp = newArray [pair1Index1][pair1Index2];
            newArray [pair1Index1][pair1Index2] = newArray [pair2Index1][pair2Index2];
            newArray [pair2Index1][pair2Index2] = tmp;
        }
        return newArray;
    }

    public int[] shuffle(int[] array) {
        Integer[] newArray = new Integer[array.length];
        int i = 0;
        for (int value : array) {
            newArray[i++] = value;
        }
        i = 0;
        int[] newSimpleArray = new int[array.length];
        for (Object value : shuffle(newArray)) {
            newSimpleArray[i++] = (Integer) value;
        }
        return newSimpleArray;
    }

    public short[] shuffle(short[] array) {
        Short[] newArray = new Short[array.length];
        int i = 0;
        for (short value : array) {
            newArray[i++] = value;
        }
        i = 0;
        short[] newSimpleArray = new short[array.length];
        for (Object value : shuffle(newArray)) {
            newSimpleArray[i++] = (Short) value;
        }
        return newSimpleArray;
    }

    public long[] shuffle(long[] array) {
        Long[] newArray = new Long[array.length];
        int i = 0;
        for (long value : array) {
            newArray[i++] = value;
        }
        i = 0;
        long[] newSimpleArray = new long[array.length];
        for (Object value : shuffle(newArray)) {
            newSimpleArray[i++] = (Long) value;
        }
        return newSimpleArray;
    }

    public byte[] shuffle(byte[] array) {
        Byte[] newArray = new Byte[array.length];
        int i = 0;
        for (byte value : array) {
            newArray[i++] = value;
        }
        i = 0;
        byte[] newSimpleArray = new byte[array.length];
        for (Object value : shuffle(newArray)) {
            newSimpleArray[i++] = (Byte) value;
        }
        return newSimpleArray;
    }

    public double[] shuffle(double[] array) {
        Double[] newArray = new Double[array.length];
        int i = 0;
        for (double value : array) {
            newArray[i++] = value;
        }
        i = 0;
        double[] newSimpleArray = new double[array.length];
        for (Object value : shuffle(newArray)) {
            newSimpleArray[i++] = (Double) value;
        }
        return newSimpleArray;
    }

    public float[] shuffle(float[] array) {
        Float[] newArray = new Float[array.length];
        int i = 0;
        for (float value : array) {
            newArray[i++] = value;
        }
        i = 0;
        float[] newSimpleArray = new float[array.length];
        for (Object value : shuffle(newArray)) {
            newSimpleArray[i++] = (Float) value;
        }
        return newSimpleArray;
    }

    public char[] shuffle(char[] array) {
        Character[] newArray = new Character[array.length];
        int i = 0;
        for (char value : array) {
            newArray[i++] = value;
        }
        i = 0;
        char[] newSimpleArray = new char[array.length];
        for (Object value : shuffle(newArray)) {
            newSimpleArray[i++] = (Character) value;
        }
        return newSimpleArray;
    }

    public boolean[] shuffle(boolean[] array) {
        Boolean[] newArray = new Boolean[array.length];
        int i = 0;
        for (boolean value : array) {
            newArray[i++] = value;
        }
        i = 0;
        boolean[] newSimpleArray = new boolean[array.length];
        for (Object value : shuffle(newArray)) {
            newSimpleArray[i++] = (Boolean) value;
        }
        return newSimpleArray;
    }

    public int[][] shuffle(int[][] array) {
        Integer[][] newArray = new Integer[array.length][];

        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = new Integer[array[i].length];
            for (int j = 0; j < newArray[i].length; j++) {
                newArray[i][j] = array[i][j];
            }
        }

        int[][] newSimpleArray = new int[array.length][];
        Object[][] tmpArray =  shuffle(newArray);
        for (int i = 0; i < newSimpleArray.length; i++) {
            newSimpleArray[i] = new int[array[i].length];
            for (int j = 0; j < array[i].length; j++) {
                newSimpleArray[i][j] = (Integer) tmpArray[i][j];
            }
        }
        return newSimpleArray;
    }

    public short[][] shuffle(short[][] array) {
        Short[][] newArray = new Short[array.length][];

        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = new Short[array[i].length];
            for (int j = 0; j < newArray[i].length; j++) {
                newArray[i][j] = array[i][j];
            }
        }

        short[][] newSimpleArray = new short[array.length][];
        Object[][] tmpArray =  shuffle(newArray);
        for (int i = 0; i < newSimpleArray.length; i++) {
            newSimpleArray[i] = new short[array[i].length];
            for (int j = 0; j < array[i].length; j++) {
                newSimpleArray[i][j] = (Short) tmpArray[i][j];
            }
        }
        return newSimpleArray;
    }

    public long[][] shuffle(long[][] array) {
        Long[][] newArray = new Long[array.length][];

        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = new Long[array[i].length];
            for (int j = 0; j < newArray[i].length; j++) {
                newArray[i][j] = array[i][j];
            }
        }

        long[][] newSimpleArray = new long[array.length][];
        Object[][] tmpArray =  shuffle(newArray);
        for (int i = 0; i < newSimpleArray.length; i++) {
            newSimpleArray[i] = new long[array[i].length];
            for (int j = 0; j < array[i].length; j++) {
                newSimpleArray[i][j] = (Long) tmpArray[i][j];
            }
        }
        return newSimpleArray;
    }

    public byte[][] shuffle(byte[][] array) {
        Byte[][] newArray = new Byte[array.length][];

        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = new Byte[array[i].length];
            for (int j = 0; j < newArray[i].length; j++) {
                newArray[i][j] = array[i][j];
            }
        }

        byte[][] newSimpleArray = new byte[array.length][];
        Object[][] tmpArray =  shuffle(newArray);
        for (int i = 0; i < newSimpleArray.length; i++) {
            newSimpleArray[i] = new byte[array[i].length];
            for (int j = 0; j < array[i].length; j++) {
                newSimpleArray[i][j] = (Byte) tmpArray[i][j];
            }
        }
        return newSimpleArray;
    }

    public float[][] shuffle(float[][] array) {
        Float[][] newArray = new Float[array.length][];

        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = new Float[array[i].length];
            for (int j = 0; j < newArray[i].length; j++) {
                newArray[i][j] = array[i][j];
            }
        }

        float[][] newSimpleArray = new float[array.length][];
        Object[][] tmpArray =  shuffle(newArray);
        for (int i = 0; i < newSimpleArray.length; i++) {
            newSimpleArray[i] = new float[array[i].length];
            for (int j = 0; j < array[i].length; j++) {
                newSimpleArray[i][j] = (Float) tmpArray[i][j];
            }
        }
        return newSimpleArray;
    }

    public double[][] shuffle(double[][] array) {
        Double[][] newArray = new Double[array.length][];

        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = new Double[array[i].length];
            for (int j = 0; j < newArray[i].length; j++) {
                newArray[i][j] = array[i][j];
            }
        }

        double[][] newSimpleArray = new double[array.length][];
        Object[][] tmpArray =  shuffle(newArray);
        for (int i = 0; i < newSimpleArray.length; i++) {
            newSimpleArray[i] = new double[array[i].length];
            for (int j = 0; j < array[i].length; j++) {
                newSimpleArray[i][j] = (Double) tmpArray[i][j];
            }
        }
        return newSimpleArray;
    }

    public char[][] shuffle(char[][] array) {
        Character[][] newArray = new Character[array.length][];

        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = new Character[array[i].length];
            for (int j = 0; j < newArray[i].length; j++) {
                newArray[i][j] = array[i][j];
            }
        }

        char[][] newSimpleArray = new char[array.length][];
        Object[][] tmpArray =  shuffle(newArray);
        for (int i = 0; i < newSimpleArray.length; i++) {
            newSimpleArray[i] = new char[array[i].length];
            for (int j = 0; j < array[i].length; j++) {
                newSimpleArray[i][j] = (Character) tmpArray[i][j];
            }
        }
        return newSimpleArray;
    }

    public boolean[][] shuffle(boolean[][] array) {
        Boolean[][] newArray = new Boolean[array.length][];

        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = new Boolean[array[i].length];
            for (int j = 0; j < newArray[i].length; j++) {
                newArray[i][j] = array[i][j];
            }
        }

        boolean[][] newSimpleArray = new boolean[array.length][];
        Object[][] tmpArray =  shuffle(newArray);
        for (int i = 0; i < newSimpleArray.length; i++) {
            newSimpleArray[i] = new boolean[array[i].length];
            for (int j = 0; j < array[i].length; j++) {
                newSimpleArray[i][j] = (Boolean) tmpArray[i][j];
            }
        }
        return newSimpleArray;
    }

    /**
     * Хранит два индекса.
     * Необходимых для перестановки (если массив одномерный)
     * или для двойного индекса (если массив двумерный).
     */
    private class PairOfIndices {
        private int member1;
        private int member2;

        public PairOfIndices(int member1, int member2) {
            this.member1 = member1;
            this.member2 = member2;
        }

        public int getMember1() {
            return member1;
        }

        public int getMember2() {
            return member2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PairOfIndices that = (PairOfIndices) o;

            if (member1 != that.member1) return false;
            if (member2 != that.member2) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = member1;
            result = 31 * result + member2;
            return result;
        }
    }

    /**
     * Хранит пару двойных индексов.
     * Необходимо для перестановок в двумерном массиве.
     * Такая кривая структура для простоты использования (без кастований).
     */
    private class PairOfPairIndices {
        private PairOfIndices member1;
        private PairOfIndices member2;

        public PairOfPairIndices(PairOfIndices member1, PairOfIndices member2) {
            this.member1 = member1;
            this.member2 = member2;
        }

        public PairOfIndices getPair1() {
            return member1;
        }

        public PairOfIndices getPair2() {
            return member2;
        }
    }


}
