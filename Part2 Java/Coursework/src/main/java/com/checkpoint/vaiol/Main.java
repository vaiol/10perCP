package com.checkpoint.vaiol;

import com.checkpoint.vaiol.mobileNerwork.operator.OperatorsNameEnum;

import java.util.Random;

public class Main {

    private static Random random = new Random();
    public static void main(String[] args) {
        System.out.println(OperatorsNameEnum.Kyivstar.name().equals("Kyevstar"));

    }
}
