package com.checkpoint.vaiol;

import com.checkpoint.vaiol.myDataStructure.MyArrayList;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        MyArrayList<Integer> name = new MyArrayList<Integer>();
        System.out.println(name.size());
        name.add(1);
        name.add(1);
        name.add(1);
        System.out.println(name.size());
        System.out.println(name.get(0));
    }
}
