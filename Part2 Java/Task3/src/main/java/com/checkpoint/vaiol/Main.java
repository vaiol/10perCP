package com.checkpoint.vaiol;

import com.checkpoint.vaiol.someCalculator.Calculator;
import com.checkpoint.vaiol.someCalculator.Loader;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String fileName = "example.dat";
        try {
            for(Calculator calculator : Loader.download(fileName)) {
                calculator.outputCalculate();
            }
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
}
