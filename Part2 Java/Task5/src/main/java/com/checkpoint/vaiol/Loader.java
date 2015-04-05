package com.checkpoint.vaiol;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    public static boolean write(String source, String fileName) {
        try {
            Files.write(Paths.get(fileName), source.getBytes());
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static String read(String fileName) {
        String result = "";
        List<String> lines = new ArrayList<String>();
        try {
           lines = Files.readAllLines(Paths.get(fileName), Charset.defaultCharset());
        } catch (IOException e) {
        }
        for (String line : lines) {
            result += line + "\n";
        }
        return result;
    }
}
