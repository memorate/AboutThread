package anchor.thread.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Anchor
 */
public class ReentrantLockUse {

    final static ReentrantLock LOCK = new ReentrantLock();

    public static void main(String[] args) {

    }



    static class CustomThread extends Thread {
        public CustomThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println(this.getName() + " start running...");
            LOCK.lock();
            try {
                for (int i = 0; i < 1000; i++) {

                }
            }finally {
                LOCK.unlock();
            }
        }
    }
}
