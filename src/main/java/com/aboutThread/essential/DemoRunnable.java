package com.aboutThread.essential;

import java.math.BigInteger;
import java.util.List;

public class DemoRunnable implements Runnable {
    List<Integer> integerList = null;
    BigInteger total = BigInteger.ZERO;

    public DemoRunnable(List<Integer> integerList) {
        this.integerList = integerList;
    }

    public void run() {
        synchronized (this) {
            final String threadName = Thread.currentThread().getName();

            Thread countThread = new Thread() {
                boolean flag = true;

                public boolean isFlag() {
                    return flag;
                }

                public void setFlag(boolean flag) {
                    this.flag = flag;
                }

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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.notify();

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
