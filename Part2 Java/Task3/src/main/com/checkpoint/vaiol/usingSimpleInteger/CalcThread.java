package com.checkpoint.vaiol.usingSimpleInteger;

public class CalcThread extends Thread {

    private Result result;

    public CalcThread(Result result) {
        this.result = result;
    }

    @Override
    public void run() {
        Integer condition = 0;
        while (condition != null) {
            condition = result.getCount();
            if(condition != null) {
                result.add(Formula.getCalculations(condition));
            }
        }
    }
}
