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
public class Sample {

    static CyclicBarrier barrier = new CyclicBarrier(5);

    public static void main(String[] args) {
        System.out.println(CommonUtil.getThreadPid());
        for (int i = 1; i <= 5; i++) {
            new CustomThread("t" + i, i).start();
        }
    }

    static class CustomThread extends Thread {
        int sleepTime;

        public CustomThread(String name, int sleepTime) {
            super(name);
            this.sleepTime = sleepTime;
        }

        @Override
        public void run() {
            System.out.println(this.getName() + " start preparing...");
            try {
                long start = System.currentTimeMillis();
                TimeUnit.SECONDS.sleep(sleepTime);
                long elapse = System.currentTimeMillis() - start;
                barrier.await();
                System.out.println(this.getName() + " finished preparation,start running.Prepare time = " + elapse + "ms");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
