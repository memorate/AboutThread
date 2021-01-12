package anchor.thread.example;

import java.math.BigInteger;
import java.util.List;

public class DemoThread extends Thread{
    List<Integer> integerList = null;
    BigInteger total = BigInteger.ZERO;
    public DemoThread(String threadName, List<Integer> integerList){
        super(threadName);
        this.integerList = integerList;
    }

    @Override
    public void run(){
        for(Integer currentNum:integerList){
            total = total.add(BigInteger.valueOf(currentNum));
        }
        System.out.println("current thread name: " + Thread.currentThread().getName() + ", subNum: " + total);
    }

    public BigInteger getTotal(){
        return total;
    }
}