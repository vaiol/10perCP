package com.checkpoint.vaiol.usingBigInteger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Loader {
    public static List<Calculator> download(String path) throws IOException {
        File file = new File(path);
        List<String> strings = new ArrayList<String>();


        BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
        try {
            String tmp;
            while ((tmp = in.readLine()) != null) {
                strings.add(tmp);
            }
        } finally {
            in.close();
        }


        List<Calculator> result = new ArrayList<Calculator>();
        for(String string : strings) {
            String[] num = string.split(" ");
            System.out.println("Len: " + num.length);
            System.out.println("1: \'" + num[0] + "\'");
            System.out.println("2: \'" + num[1] + "\'");
            int n = Integer.parseInt(num[0]);
            int countOfThreads = Integer.parseInt(num[1]);
            result.add(new Calculator(n, countOfThreads));
        }
        return result;
    }
}
