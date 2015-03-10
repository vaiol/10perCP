package com.checkpoint.vaiol.usingSimpleInteger;

public class Formula {
    
    private Formula() {
    }

    public static int getCalculations(int i) {
        //2^(i - (-1)^i)
        return (int) Math.pow(2,(int)(i - Math.pow((-1), i)));
    }
}
