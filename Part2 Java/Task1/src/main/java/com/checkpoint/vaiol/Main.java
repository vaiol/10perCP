package com.checkpoint.vaiol;

import com.checkpoint.vaiol.mixer.ArrayMixer;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] array = new int[10];
        for (int i = 0; i < array.length; i ++) {
            array[i] = i + 1;
        }

        ArrayMixer arrayMixer = new ArrayMixer(100);

        System.out.println(Arrays.toString(array));
        array = arrayMixer.shuffle(array);
        System.out.println(Arrays.toString(array));

        //------------------------------------------


        System.out.println("--------------------------------");
        int[][] twoDimensionalArray = new int[2][5];
        int k = 1;
        for (int i = 0; i < twoDimensionalArray.length; i++) {
            for (int j = 0; j < twoDimensionalArray[i].length; j++) {
                twoDimensionalArray[i][j] = k++;
            }
        }
        for (int i = 0; i < twoDimensionalArray.length; i++) {
            System.out.println(Arrays.toString(twoDimensionalArray[i]));
        }
        twoDimensionalArray = arrayMixer.shuffle(twoDimensionalArray);

        for (int i = 0; i < twoDimensionalArray.length; i++) {
            System.out.println(Arrays.toString(twoDimensionalArray[i]));
        }

        System.out.println("END");
    }
}
