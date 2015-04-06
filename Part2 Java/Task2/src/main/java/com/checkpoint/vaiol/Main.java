package com.checkpoint.vaiol;

import com.checkpoint.vaiol.myDataStructure.MyArrayList;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        MyArrayList<Integer> name = new MyArrayList<Integer>();
        System.out.println(name.size());
        name.add(0, 1);
        System.out.println(name.get(0));
    }
}
