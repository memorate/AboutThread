package com.aboutThread.essential;

import java.math.BigInteger;
import java.util.List;

public class DemoRunnable implements Runnable {
    List<Integer> integerList = null;
    BigInteger total = BigInteger.ZERO;
    public DemoRunnable(List<Integer> integerList){
        this.integerList = integerList;
    }

    public void run(){
        synchronized (this){
            for(Integer currentNum:integerList){
                total = total.add(BigInteger.valueOf(currentNum));
            }
            System.out.println("current thread name: " + Thread.currentThread().getName() + ", subNum: " + total);
        }
    }

    public BigInteger getTotal(){
        return total;
    }
}
