package anchor.thread.juc.reentrantLock;

import anchor.thread.util.CommonUtil;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Anchor
 */
public class ReentrantLockSample {
    private static int num = 0;
    final static ReentrantLock LOCK = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        System.out.println(CommonUtil.getThreadPid());
        CustomThread t1 = new CustomThread("t1");
        CustomThread t2 = new CustomThread("t2");
        CustomThread t3 = new CustomThread("t3");
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        System.out.println(num);
    }

    /**
     * 可重入，同一个线程可多次获得同一把锁
     */
    static void plus(){
        LOCK.lock();
        LOCK.lock();
        try {
            num++;
        }finally {
            LOCK.unlock();
            LOCK.unlock();
        }
    }

    static class CustomThread extends Thread {
        public CustomThread(String name) {super(name);}

        @Override
        public void run() {
            System.out.println(this.getName() + " start running...");
            for (int i = 0; i < 1000; i++) {
                plus();
            }
        }
    }
}
