package com.checkpoint.vaiol;

import java.util.Random;

public class Main {

    private static Random random = new Random();
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(random.nextInt(10));
        }
    }
}
