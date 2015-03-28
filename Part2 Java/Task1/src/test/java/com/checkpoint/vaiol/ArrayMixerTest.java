package com.checkpoint.vaiol;

import com.checkpoint.vaiol.mixer.ArrayMixer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ArrayMixerTest {
    private ArrayMixer arrayMixer;
    private double percentage;
    private int[] testArray1;
    private int[] testArray2;
    private int[] testArray3;
    private int[] testArray4;
    private int[][] testArray5;
    private int[][] testArray6;
    private int[][] testArray7;
    private int[][] testArray8;

    public ArrayMixerTest() {
    }

    @Before
    public void setUpCalcTest() {
        this.percentage = 0.25;
        this.arrayMixer = new ArrayMixer((int)(this.percentage * 100.0D));
        this.testArray1 = new int[10];
        this.testArray2 = new int[100];
        this.testArray3 = new int[234];
        this.testArray4 = new int[54];
        this.testArray5 = new int[88][20];
        this.testArray6 = new int[10][43];
        this.testArray7 = new int[234][2];
        this.testArray8 = new int[54][24];
        this.testArray1 = this.fillArray((int[])this.testArray1);
        this.testArray2 = this.fillArray((int[])this.testArray2);
        this.testArray3 = this.fillArray((int[])this.testArray3);
        this.testArray4 = this.fillArray((int[])this.testArray4);
        this.testArray5 = this.fillArray((int[][])this.testArray5);
        this.testArray6 = this.fillArray((int[][])this.testArray6);
        this.testArray7 = this.fillArray((int[][])this.testArray7);
        this.testArray8 = this.fillArray((int[][])this.testArray8);
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

        int numberOfElement = (int)(testArray1.length * percentage);
        int[] mixedArray = arrayMixer.shuffle(testArray1);
        assertEquals(numberOfElement, countDifferentElement(testArray1, mixedArray));

        numberOfElement = (int)(testArray2.length * percentage);
        mixedArray = arrayMixer.shuffle(testArray2);
        assertEquals(numberOfElement, countDifferentElement(testArray2, mixedArray));

        numberOfElement = (int)(testArray2.length * percentage);
        mixedArray = arrayMixer.shuffle(testArray2);
        assertEquals(numberOfElement, countDifferentElement(testArray2, mixedArray));

        numberOfElement = (int)(testArray3.length * percentage);
        mixedArray = arrayMixer.shuffle(testArray3);
        assertEquals(numberOfElement, countDifferentElement(testArray3, mixedArray));

        numberOfElement = (int)(testArray4.length * percentage);
        mixedArray = arrayMixer.shuffle(testArray4);
        assertEquals(numberOfElement, countDifferentElement(testArray4, mixedArray));


        numberOfElement = (int)(testArray5.length * testArray5[0].length  * percentage);
        int[][] mixedArrayTwoD = arrayMixer.shuffle(testArray5);
        assertEquals(numberOfElement, countDifferentElement(testArray5, mixedArrayTwoD));

        numberOfElement = (int)(testArray6.length * testArray6[0].length  * percentage);
        mixedArrayTwoD = arrayMixer.shuffle(testArray6);
        assertEquals(numberOfElement, countDifferentElement(testArray6, mixedArrayTwoD));

        numberOfElement = (int)(testArray7.length * testArray7[0].length  * percentage);
        mixedArrayTwoD = arrayMixer.shuffle(testArray7);
        assertEquals(numberOfElement, countDifferentElement(testArray7, mixedArrayTwoD));

        numberOfElement = (int)(testArray8.length * testArray8[0].length  * percentage);
        mixedArrayTwoD = arrayMixer.shuffle(testArray8);
        assertEquals(numberOfElement, countDifferentElement(testArray8, mixedArrayTwoD));
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