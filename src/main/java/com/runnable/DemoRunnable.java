package com.runnable;

import java.math.BigInteger;
import java.util.List;

public class DemoRunnable implements Runnable {
    List<Integer> integerList = null;
    BigInteger total = BigInteger.ZERO;
    public DemoRunnable(List<Integer> integerList){
        this.integerList = integerList;
    }

    public void run(){
        for(Integer currentNum:integerList){
            total = total.add(BigInteger.valueOf(currentNum));
        }
        System.out.println("current thread:" + Thread.currentThread().getName() + ",subNum:" + total);
    }

    public BigInteger getTotal(){
        return total;
    }
}
