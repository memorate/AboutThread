package anchor.thread.example;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.Callable;

public class DemoCallable implements Callable {
    List<Integer> integerList = null;
    BigInteger total = BigInteger.ZERO;
    public DemoCallable(List<Integer> integerList) {
        this.integerList = integerList;
    }

    public Object call() {
        for(Integer currentNum:integerList){
            total = total.add(BigInteger.valueOf(currentNum));
        }
        System.out.println("current thread name: " + Thread.currentThread().getName() + ", subNum: " + total);
        return null;
    }

    public BigInteger getTotal(){
        return total;
    }
}
