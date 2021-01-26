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

    private final static CyclicBarrier BARRIER = new CyclicBarrier(5);

    public static void main(String[] args) throws InterruptedException {
        System.out.println(CommonUtil.getThreadPid());
        for (int i = 1; i <= 5; i++) {
            CustomThread thread = new CustomThread("t" + i);
            thread.start();
            if (i == 3) {
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

        private void eat(){
            try {
                System.out.println(this.getName() + " entering...");
                if ("t5".equals(this.getName())) {
                    TimeUnit.SECONDS.sleep(10);
                }
                BARRIER.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(this.getName() + " finished eating...");
        }

        private void drive(){
            try {
                System.out.println(this.getName() + " leaving...");
                if ("t5".equals(this.getName())) {
                    TimeUnit.SECONDS.sleep(10);
                }
                BARRIER.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(this.getName() + " arrived...");
        }

        @Override
        public void run() {
            eat();
            drive();
        }
    }
}
