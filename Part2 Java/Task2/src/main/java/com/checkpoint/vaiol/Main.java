package com.checkpoint.vaiol;

import com.checkpoint.vaiol.myDataStructure.MyArrayList;
import com.checkpoint.vaiol.myDataStructure.MyLinkedList;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        MyLinkedList<Integer> list = new MyLinkedList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);


        System.out.println(list);
        System.out.println(list.lastIndexOf(8));




    }
}
