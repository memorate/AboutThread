package com.demo.thread;

import com.runnable.DemoRunnable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DemoThreadTest {

    private final List<Integer> integerList = new ArrayList<Integer>();

    @Before
    public void initiate(){
        for (int i = 1; i < 10000001; i++) {
            integerList.add(i);
        }
    }

    @Test
    public void threadTest() {
        double start = System.currentTimeMillis();
        DemoThread threadOne = new DemoThread("ThreadOne", integerList);
        threadOne.run();
        double end = System.currentTimeMillis();
        BigInteger totalNum = threadOne.getTotal();
        System.out.println("current Thread:" + Thread.currentThread().getName() + ", total num:" + totalNum + ", total time:" + (end - start) / 1000 + "s");
    }

    @Test
    public void runnableTest(){
        double start = System.currentTimeMillis();
        DemoRunnable runnable = new DemoRunnable(integerList);
        runnable.run();
        double end = System.currentTimeMillis();
        BigInteger totalNum = runnable.getTotal();
        System.out.println("current Thread:" + Thread.currentThread().getName() + ", total num:" + totalNum + ", total time:" + (end - start) / 1000 + "s");
    }
}
