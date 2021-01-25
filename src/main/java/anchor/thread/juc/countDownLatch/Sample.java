package anchor.thread.juc.countDownLatch;

import anchor.thread.util.CommonUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Anchor
 *
 * CountDownLatch(闭锁)，作用与 Thread 类中的 join() 类似，但是 CountDownLatch 可以控制线程的数量。
 *
 * new CountDownLatch 类时会指定一个参数 count 的大小，调用 await() 会使当前线程进入等待状态，直到 count 的大小为0。
 * 调用 countDown() 会使 count 减一。
 *
 * 有两个 await() 方法：
 * 1.void await() throws InterruptedException
 *   使当前线程进入等待状态，直到 count 为 0，或者被 interrupt() 打断
 *
 * 2.boolean await(long timeout, TimeUnit unit) throws InterruptedException
 *   使当前线程进入等待状态，直到 count 为 0，或者等待时间 timeout 耗尽，或者被 interrupt() 打断
 *   当 timeout 未耗尽而 count 为 0 时，返回 true；当 timeout 耗尽时而 count 不为 0 时返回 false
 */
public class Sample {

    static CountDownLatch latch = new CountDownLatch(3);

    public static void main(String[] args) throws InterruptedException {
        System.out.println(CommonUtil.getThreadPid());
        CustomThread t1 = new CustomThread("t1", 2);
        CustomThread t2 = new CustomThread("t2", 3);
        CustomThread t3 = new CustomThread("t3", 5);
        t1.start();
        t2.start();
        t3.start();
        //main 线程会等 t1、t2、t3 都结束才会继续往下执行
        latch.await();
        System.out.println("main thread over,count = " + latch.getCount());
    }

    static class CustomThread extends Thread {
        int sleepTime;

        public CustomThread(String name, int sleepTime) {
            super(name);
            this.sleepTime = sleepTime;
        }

        @Override
        public void run() {
            System.out.println(this.getName() + " start running...");
            try {
                TimeUnit.SECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
                System.out.println(this.getName() + " exiting...");
            }
        }
    }
}
