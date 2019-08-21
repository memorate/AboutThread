package com.thread;

import com.aboutThread.essential.DemoCallable;
import com.aboutThread.essential.DemoRunnable;
import com.aboutThread.essential.DemoThread;
import org.junit.Before;
import org.junit.Ignore;
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
        for (int i = 1; i < 1001; i++) {
            integerList.add(i);
        }
    }

    @Test
    public void threadTest() {
        Thread.currentThread().setName("threadTestThread");
        double start = System.currentTimeMillis();
        DemoThread threadOne = new DemoThread("ThreadTom", integerList);
        threadOne.start();
        try {
            threadOne.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        double end = System.currentTimeMillis();
        BigInteger totalNum = threadOne.getTotal();
        System.out.println("current Thread name: " + Thread.currentThread().getName() + ", total num: " + totalNum + ", elapse time: " + (end - start) / 1000 + "s");
    }

    @Test
    public void runnableTest(){
        Thread.currentThread().setName("runnableTestThread");
        double start = System.currentTimeMillis();
        DemoRunnable runnable = new DemoRunnable(integerList);
        Thread threadOne = new Thread(runnable, "ThreadOne");
        Thread threadTwo = new Thread(runnable, "ThreadTwo");
        threadOne.start();
        threadTwo.start();
        try {
            threadOne.join();
            threadTwo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        double end = System.currentTimeMillis();
        BigInteger totalNum = runnable.getTotal();
        System.out.println("current Thread name: " + Thread.currentThread().getName() + ", total num: " + totalNum + ", elapse time: " + (end - start) / 1000 + "s");
    }

    @Test
    @Ignore
    public void callableTest(){
        Thread.currentThread().setName("callableTestThread");
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
        System.out.println("current Thread name: " + Thread.currentThread().getName() + ", total num: " + totalNum + ", elapse time: " + (end - start) / 1000 + "s");
    }

    @Test
    @Ignore
    public void threadPoolTest(){
        ExecutorService pool = Executors.newFixedThreadPool(3);
    }
}
