package com.checkpoint.vaiol;

import com.checkpoint.vaiol.usingSimpleInteger.Calculator;
import com.checkpoint.vaiol.usingSimpleInteger.Loader;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        String fileName = "example.data";
        try {
            for(Calculator calculator : Loader.download(fileName)) {
                calculator.outputCalculate();
            }
        } catch (IOException e) {
            System.out.println("File not found");
        }
        try {
            for(com.checkpoint.vaiol.usingBigInteger.Calculator calculator : com.checkpoint.vaiol.usingBigInteger.Loader.download(fileName)) {
                calculator.outputCalculate();
            }
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
}
