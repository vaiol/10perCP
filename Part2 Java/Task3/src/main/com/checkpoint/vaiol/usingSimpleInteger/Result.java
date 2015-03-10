package com.checkpoint.vaiol.usingSimpleInteger;

public class Result {
    private int result;
    private int count;
    private int n;

    Result(int n) {
        result = 0;
        count = 0;
        this.n = n;
    }

    synchronized public void add(int number) {
        result += (number);
    }

    synchronized public Integer getCount() {
        count++;
        if(count < n) {
            return count;
        } else {
            return null;
        }
    }

    public int getResult() {
        return result;
    }

}
