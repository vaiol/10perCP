package com.checkpoint.vaiol.someCalculator;

import java.math.BigInteger;

public class Formula {
    
    private Formula() {
    }

    private static final BigInteger TWO = new BigInteger("2");

    public static BigInteger getCalculations(int i) {
        //2^(i - (-1)^i)
        return TWO.pow((int)(i - Math.pow((-1), i)));
    }
}
