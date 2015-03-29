package com.checkpoint.vaiol;

import com.checkpoint.vaiol.mixer.ArrayMixer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ArrayMixerTest {
    private ArrayMixer arrayMixer;
    private static final double PERCENTAGE = 0.25;
    private int[] testArray1;
    private int[] testArray2;
    private int[] testArray3;
    private int[] testArray4;
    private int[][] testArray5;
    private int[][] testArray6;
    private int[][] testArray7;
    private int[][] testArray8;
    private int[][] testArray9;
    private int[] testArray10;

    public ArrayMixerTest() {
    }

    @Before
    public void setUpCalcTest() {
        arrayMixer = new ArrayMixer((int)(PERCENTAGE * 100D));
        testArray1 = new int[10];
        testArray2 = new int[100];
        testArray3 = new int[234];
        testArray4 = new int[54];
        testArray5 = new int[88][20];
        testArray6 = new int[10][43];
        testArray7 = new int[234][2];
        testArray8 = new int[54][24];
        testArray9 = new int[7][1];
        testArray10 = new int[5];

        testArray1 = fillArray(testArray1);
        testArray2 = fillArray(testArray2);
        testArray3 = fillArray(testArray3);
        testArray4 = fillArray(testArray4);
        testArray5 = fillArray(testArray5);
        testArray6 = fillArray(testArray6);
        testArray7 = fillArray(testArray7);
        testArray8 = fillArray(testArray8);
        testArray9 = fillArray(testArray9);
        testArray10 = fillArray(testArray10);
    }

    private int[] fillArray(int[] array) {
        for(int i = 0; i < array.length; ++i) {
            array[i] = i + 1;
        }
        return array;
    }

    private int[][] fillArray(int[][] array) {
        int count = 1;

        for(int i = 0; i < array.length; ++i) {
            for(int j = 0; j < array[i].length; ++j) {
                array[i][j] = count++;
            }
        }

        return array;
    }

    @Test
    public void testMixer() {

        /*
        ВНИМАНИЕ!!!
        При подсчете необходимого количества элементов для перемешивания (в зависисмости от %)
        итоговове значение всегда округляется до меньшего целого числа.
        Например: 25% от 10 => 2.5. А это дробное значение округляется до 2.
         */
        int numberOfElement = (int)(testArray1.length * PERCENTAGE);
        int[] mixedArray = arrayMixer.shuffle(testArray1);
        assertEquals(numberOfElement, countDifferentElement(testArray1, mixedArray));

        numberOfElement = (int)(testArray2.length * PERCENTAGE);
        mixedArray = arrayMixer.shuffle(testArray2);
        assertEquals(numberOfElement, countDifferentElement(testArray2, mixedArray));

        numberOfElement = (int)(testArray2.length * PERCENTAGE);
        mixedArray = arrayMixer.shuffle(testArray2);
        assertEquals(numberOfElement, countDifferentElement(testArray2, mixedArray));

        numberOfElement = (int)(testArray3.length * PERCENTAGE);
        mixedArray = arrayMixer.shuffle(testArray3);
        assertEquals(numberOfElement, countDifferentElement(testArray3, mixedArray));

        numberOfElement = (int)(testArray4.length * PERCENTAGE);
        mixedArray = arrayMixer.shuffle(testArray4);
        assertEquals(numberOfElement, countDifferentElement(testArray4, mixedArray));


        numberOfElement = (int)(testArray5.length * testArray5[0].length  * PERCENTAGE);
        int[][] mixedArrayTwoD = arrayMixer.shuffle(testArray5);
        assertEquals(numberOfElement, countDifferentElement(testArray5, mixedArrayTwoD));

        numberOfElement = (int)(testArray6.length * testArray6[0].length  * PERCENTAGE);
        mixedArrayTwoD = arrayMixer.shuffle(testArray6);
        assertEquals(numberOfElement, countDifferentElement(testArray6, mixedArrayTwoD));

        numberOfElement = (int)(testArray7.length * testArray7[0].length  * PERCENTAGE);
        mixedArrayTwoD = arrayMixer.shuffle(testArray7);
        assertEquals(numberOfElement, countDifferentElement(testArray7, mixedArrayTwoD));

        numberOfElement = (int)(testArray8.length * testArray8[0].length  * PERCENTAGE);
        mixedArrayTwoD = arrayMixer.shuffle(testArray8);
        assertEquals(numberOfElement, countDifferentElement(testArray8, mixedArrayTwoD));
    }

    @Test
    public void testNoMix() {
        /*
        ВНИМАНИЕ!!!
        Если количество элементов для перемешивания будет меньше чем 2.
        То перестановок не будет вовсе. Так как невозможно сделать так,
        чтобы только лишь 1 элемент в массиве поменял свое местоположение.
        Был вариант, округлять до большего значения, например: 25% от 5 => 1.25,
        округляем до 2 и перемешиваем, но в данном случае перемешается 40% массива,
        а что делать если длина массива и вовсе = 2?.
        Был еще один вариант, кидать исключение если перемешивание не удалось,
        но автору показалось это плохой идеей.
         */
        int numberOfElement = 0;
        int[][] mixedArray = arrayMixer.shuffle(testArray9);
        assertEquals(numberOfElement, countDifferentElement(testArray9, mixedArray));

        int[] mixArray = arrayMixer.shuffle(testArray10);
        assertEquals(numberOfElement, countDifferentElement(testArray10, mixArray));
    }

    private int countDifferentElement(int[] array1, int[] array2) {
        int count = 0;

        for(int i = 0; i < array1.length; ++i) {
            if(array1[i] != array2[i]) {
                ++count;
            }
        }

        return count;
    }

    private int countDifferentElement(int[][] array1, int[][] array2) {
        int count = 0;

        for(int i = 0; i < array1.length; ++i) {
            for(int j = 0; j < array1[i].length; ++j) {
                if(array1[i][j] != array2[i][j]) {
                    ++count;
                }
            }
        }

        return count;
    }
}