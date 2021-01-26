package anchor.thread.juc.cyclicBarrier;

import anchor.thread.util.CommonUtil;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @author Anchor
 *
 *
 */
public class BrokenBarrier {

    private final static CyclicBarrier BARRIER = new CyclicBarrier(7);

    public static void main(String[] args) throws InterruptedException {
        System.out.println(CommonUtil.getThreadPid());
        for (int i = 1; i <= 7; i++) {
            CustomThread thread = new CustomThread("t" + i);
            thread.start();
            if (i == 4) {
                TimeUnit.SECONDS.sleep(1);
                thread.interrupt();
                TimeUnit.SECONDS.sleep(2);
                System.out.println(thread.getName() + " interrupt...");
            }
        }
    }

    static class CustomThread extends Thread {

        public CustomThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            try {
                System.out.println(this.getName() + " entering...");
                if ("t4".equals(this.getName())) {
                    TimeUnit.SECONDS.sleep(10);
                }
                BARRIER.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(this.getName() + " finished eating...");
        }
    }
}
