package com.checkpoint.vaiol;

import java.util.Date;

public class Main {
    public static void main(String[] args) {

        long date1 = new Date().getTime();
        WordCounter wordCounter = new WordCounter("filesForTesting/shakespeare.txt", 1);
        long date2 = new Date().getTime();

        System.out.println(wordCounter.getCount("hold"));

        System.out.println("TIME: " + (date2 - date1));

    }
}
