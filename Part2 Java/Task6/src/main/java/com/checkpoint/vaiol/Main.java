package com.checkpoint.vaiol;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        long date1 = new Date().getTime();
        WordCounter wordCounter = new WordCounter("shakespeare.txt");
        long date2 = new Date().getTime();

        HashMap<String, Integer> map = (HashMap<String, Integer>) wordCounter.getMap();
        List<String> list = new ArrayList<String>(map.keySet());
        for (String l : list) {
            System.out.println(l + ": " + map.get(l));
        }

    }
}
