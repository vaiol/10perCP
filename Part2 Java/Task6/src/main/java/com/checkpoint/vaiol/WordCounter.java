package com.checkpoint.vaiol;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WordCounter {
    private List<String> text = new ArrayList<String>();
    private int threadCount;
    private Map<String, Integer> map = new HashMap<String, Integer>();

    public WordCounter(String fileName) {
        threadCount = 1;
        initialize(fileName);
    }

    public WordCounter(String fileName, int threadCount) {
        this.threadCount = threadCount;
        initialize(fileName);
    }


    private void initialize(String fileName) {
        //read from files
        List<String> lines = new ArrayList<String>();
        try {
            lines = Files.readAllLines(Paths.get(fileName), Charset.defaultCharset());
        } catch (IOException e) {
            System.out.println("File not found!");
        }


        int countOfLines = lines.size() / threadCount;
        System.out.println(countOfLines);

        int i = 0;
        int c = 0;
        StringBuilder result = new StringBuilder(countOfLines* 10);
        for (String line : lines) {
            if (i >= countOfLines) {
                i = 0;
                text.add(result.toString());
                result = new StringBuilder(countOfLines* 10);
            }
            result.append(line + "\n");
            i++;
        }
        text.add(result.toString());

        i = 0;
        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        for (String pieceOfText : text) {
            System.out.println(pieceOfText);
            service.execute(new CounterThread(pieceOfText));
            i++;
        }

        //wait a thread
        service.shutdown();
        while (!service.isTerminated()) {
        }


    }

    /**
     * Returns the number of times the word found in the text;
     */
    public int getCount(String word) {
        return map.get(word);
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public String getText() {
        String result = "";
        for (String str : text) {
            result += str;
        }
        return result;
    }

    public int getSize() {
        return text.size();
    }
    private class CounterThread extends Thread {
        private String text;

        public CounterThread(String pieceOfText) {
            text = pieceOfText;
        }

        @Override
        public void run() {
            String[] words = text.split("[\\s\\W]+");
            for (int i = 0; i < words.length; i++) {
                String key = words[i].toLowerCase();
                synchronized (map) {
                    if (map.containsKey(key)) {
                        map.put(key, map.get(key) + 1);
                    } else {
                        map.put(key, 1);
                    }
                }
            }
        }
    }

}
