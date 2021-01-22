package anchor.thread.reentrantLock;

import anchor.util.CommonUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Anchor
 *
 * 可中断锁是指：在等待获取锁的过程中可以被中断。
 *
 * LOCK.lockInterruptibly() 与 LOCK.lock() 的作用相同
 * 不同的是在获取锁的过程中，若线程调用了interrupt()方法，
 * lockInterruptibly() 可以抛出 InterruptedException 异常，从而中断获取锁的过程，而 lock() 不能,
 * 使用 try-catch 语句捕捉 lockInterruptibly() 抛出的异常可以自由进行其他操作
 */
public class Interrupt {

    final static ReentrantLock LOCK1 = new ReentrantLock();
    final static ReentrantLock LOCK2 = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        System.out.println(CommonUtil.getThreadPid());
        CustomThread t1 = new CustomThread("t1", true);
        CustomThread t2 = new CustomThread("t2", false);
        t1.start();
        t2.start();
        TimeUnit.SECONDS.sleep(3);
        t1.interrupt();
        t2.interrupt();
    }

    static class CustomThread extends Thread {
        private final boolean flag;

        public CustomThread(String name, boolean flag) {
            super(name);
            this.flag = flag;
        }

        @Override
        public void run() {
            System.out.println(this.getName() + " start running...");
            try {
                if (flag) {
                    LOCK1.lockInterruptibly();
                    //睡眠三秒，给其他线程时间去获取另一把锁
                    TimeUnit.SECONDS.sleep(3);
                    LOCK2.lockInterruptibly();
                } else {
                    LOCK2.lockInterruptibly();
                    TimeUnit.SECONDS.sleep(3);
                    LOCK1.lockInterruptibly();
                }
                TimeUnit.SECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Catch InterruptedException," + this.getName() + " is interrupted: " + this.isInterrupted());
            } finally {
                if (LOCK1.isHeldByCurrentThread()) {
                    LOCK1.unlock();
                    System.out.println(this.getName() + " release lock1...");
                }
                if (LOCK2.isHeldByCurrentThread()) {
                    LOCK2.unlock();
                    System.out.println(this.getName() + " release lock2...");
                }
            }
        }
    }
}
