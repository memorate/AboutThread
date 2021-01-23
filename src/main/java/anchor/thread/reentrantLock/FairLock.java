package anchor.thread.reentrantLock;

import anchor.util.CommonUtil;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Anchor
 */
public class FairLock {

    /**
     * 公平锁，先到先得
     * 哪个线程先启动，哪个线程首先获得锁
     */
    final static ReentrantLock LOCK = new ReentrantLock(true);

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
    }

    static class CustomThread extends Thread {
        public CustomThread(String name) {super(name);}

        @Override
        public void run() {
            System.out.println(this.getName() + " start running...");
            LOCK.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " get the lock.");
            } finally {
                LOCK.unlock();
            }
        }
    }
}
