package com.demo.thread;

import java.math.BigInteger;
import java.util.List;

class DemoThread extends Thread{
    List<Integer> integerList = null;
    BigInteger total = BigInteger.ZERO;
    DemoThread(String threadName, List<Integer> integerList){
        super(threadName);
        this.integerList = integerList;
    }

    @Override
    public void run(){
        for(Integer currentNum:integerList){
            total = total.add(BigInteger.valueOf(currentNum));
        }
        System.out.println("current thread:" + this.getName() + ",subNum:" + total);
    }

    public BigInteger getTotal(){
        return total;
    }
}