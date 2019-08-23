package com.aboutThread.essential;

import java.math.BigInteger;
import java.util.List;

public class DemoRunnable implements Runnable {
    List<Integer> integerList = null;
    BigInteger total = BigInteger.ZERO;
    private volatile boolean flag = true;

    public DemoRunnable(List<Integer> integerList) {
        this.integerList = integerList;
    }

    public void run() {
        synchronized (this) {
            final String threadName = Thread.currentThread().getName();
            Thread countThread = new Thread() {
                @Override
                public void run() {
                    while (flag){
                        int num = 1;
                        for (; ; ) {
                            System.out.println(threadName + " sleep " + num++ + "s");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
            try {
                System.out.println(threadName + " waiting for 5s");
                countThread.start();
                this.wait(5000);
                this.notify();
                this.flag = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Integer currentNum : integerList) {
                total = total.add(BigInteger.valueOf(currentNum));
            }
            System.out.println("current thread name: " + threadName + ", subNum: " + total);
        }
    }

    public BigInteger getTotal() {
        return total;
    }
}
