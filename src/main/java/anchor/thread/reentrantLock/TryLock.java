package anchor.thread.reentrantLock;

import anchor.util.CommonUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Anchor
 *
 * tryLock() 与 tryLock(long timeout, TimeUnit unit)
 * 可尝试获取锁，并返回尝试的结果(boolen值)
 *
 * tryLock() 尝试并立即返回结果，且不会被 interrupt() 打断
 * tryLock(long timeout, TimeUnit unit) 会尝试指定时间后返回结果，会被 interrupt() 打断
 */
public class TryLock {

    final static ReentrantLock LOCK = new ReentrantLock();

    public static void main(String[] args) {
        System.out.println(CommonUtil.getThreadPid());
        CustomThread t1 = new CustomThread("t1");
        CustomThread t2 = new CustomThread("t2");
        t1.start();
        t2.start();
    }

    static class CustomThread extends Thread {

        public CustomThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println(this.getName() + " start running...");
            try {
                boolean r = LOCK.tryLock(3, TimeUnit.SECONDS);
                System.out.println("whether" + this.getName() + " get the lock: " + r);
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (LOCK.isHeldByCurrentThread()) {
                    LOCK.unlock();
                }
            }
        }
    }
}
