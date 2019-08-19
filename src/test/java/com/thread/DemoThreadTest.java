package com.thread;

import com.aboutThread.essential.DemoCallable;
import com.aboutThread.essential.DemoRunnable;
import com.aboutThread.essential.DemoThread;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(MockitoJUnitRunner.class)
public class DemoThreadTest {

    private final List<Integer> integerList = new ArrayList<Integer>();

    @Before
    public void initiate(){
        for (int i = 1; i < 1000001; i++) {
            integerList.add(i);
        }
    }

    @Test
    public void threadTest() {
        double start = System.currentTimeMillis();
        DemoThread threadOne = new DemoThread("ThreadOne", integerList);
        threadOne.start();
        try {
            threadOne.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        double end = System.currentTimeMillis();
        BigInteger totalNum = threadOne.getTotal();
        System.out.println("current Thread:" + Thread.currentThread().getName() + ", total num:" + totalNum + ", total time:" + (end - start) / 1000 + "s");
    }

    @Test
    public void runnableTest(){
        double start = System.currentTimeMillis();
        DemoRunnable runnable = new DemoRunnable(integerList);
        Thread threadOne = new Thread(runnable);
        threadOne.setName("ThreadOne");
        threadOne.start();
        try {
            threadOne.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        double end = System.currentTimeMillis();
        BigInteger totalNum = runnable.getTotal();
        System.out.println("current Thread:" + Thread.currentThread().getName() + ", total num:" + totalNum + ", total time:" + (end - start) / 1000 + "s");
    }

    @Test
    public void callableTest(){
        double start = System.currentTimeMillis();
        DemoCallable callable = new DemoCallable(integerList);
        Thread threadOne = new Thread((Runnable) callable);
        threadOne.setName("ThreadOne");
        threadOne.start();
        try {
            threadOne.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        double end = System.currentTimeMillis();
        BigInteger totalNum = callable.getTotal();
        System.out.println("current Thread:" + Thread.currentThread().getName() + ", total num:" + totalNum + ", total time:" + (end - start) / 1000 + "s");
    }

    @Test
    public void threadPoolTest(){
        ExecutorService pool = Executors.newFixedThreadPool(3);
    }
}
