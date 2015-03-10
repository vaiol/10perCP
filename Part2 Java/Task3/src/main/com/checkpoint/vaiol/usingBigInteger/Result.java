package com.checkpoint.vaiol.usingBigInteger;

import java.math.BigInteger;

public class Result {
    private BigInteger result;
    private int count;
    private int n;

    Result(int n) {
        result = new BigInteger("0");
        count = 0;
        this.n = n;
    }

    synchronized public void add(BigInteger number) {
        result = result.add(number);
    }

    synchronized public Integer getCount() {
        count++;
        if(count < n) {
            return count;
        } else {
            return null;
        }
    }

    public BigInteger getResult() {
        return result;
    }

}
