package com.checkpoint.vaiol.someCalculator;

import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Calculator {

    private int n;
    private int countOfThreads;

    public Calculator(int n, int countOfThreads) {
        this.n = n;
        this.countOfThreads = countOfThreads;
    }

    public BigInteger calculate() {
        return calculate(n);
    }

    public static BigInteger calculate(int n) {
        Result result = new Result(n);
        for(int i = 1; i <= n; i++) {
            result.add(Formula.getCalculations(i));
        }
        return result.getResult();
    }


    public  BigInteger calculateWithThreads() {
        return calculateWithThreads(n, countOfThreads);
    }

    public static BigInteger calculateWithThreads(int n, int countOfThreads) {
        Result result = new Result(n);
        Thread[] threads = new Thread[countOfThreads];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new CalcThread(result);
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Main thread is stopped!");
            }
        }
        return result.getResult();
    }

    public BigInteger calculateWithExecutor() {
        return calculateWithExecutor(n, countOfThreads);
    }
    public static BigInteger calculateWithExecutor(int n, int countOfThreads) {
        Result result = new Result(n);
        ExecutorService service = Executors.newFixedThreadPool(countOfThreads);
        for (int i = 1; i <= n; i++) {
            service.execute(new CalcThread(result));
        }
        service.shutdown();
        while (!service.isTerminated()) {
        }
        return result.getResult();
    }

    public void outputCalculate() {
        long time1 = new Date().getTime();
        System.out.println(calculate());
        long time2 = new Date().getTime();
        System.out.println(calculateWithThreads());
        long time3 = new Date().getTime();
        System.out.println(calculateWithExecutor());
        long time4 = new Date().getTime();


        System.out.println("Time without threads: " + (time2 - time1) + " ms");
        System.out.println("Time with threads: " + (time3 - time2) + " ms");
        System.out.println("Time with executors: " + (time4 - time3) + " ms");

        System.out.println("---------------------------------------------------------");

    }
}
