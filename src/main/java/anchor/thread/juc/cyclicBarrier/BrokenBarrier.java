package anchor.thread.juc.cyclicBarrier;

import anchor.thread.util.CommonUtil;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Anchor
 *
 * CyclicBarrier 的异常机制：
 *   1.当一组线程中的某个线程异常中断，那么阻拦这组线程的屏障就会被打开，此线程之前和之后在屏障前的线程都会直接通过屏障(await())，
 *     且 await() 会抛出 BrokenBarrierException
 *   2.被破坏的屏障不会自动修复，意思是当下一组线程使用此屏障时，线程都会直接通过屏障，且 await() 会抛出 BrokenBarrierException
 *   3.使用 reset() 方法可以修复被破坏的屏障
 *
 * CyclicBarrier 还有一个 await() 方法，可以指定在屏障前等待的时间：
 *   int await(long timeout, TimeUnit unit) throws InterruptedException,BrokenBarrierException,TimeoutException
 *     a.其返回值代表调用此方法的线程是第几个进入屏障的，parties - 1 是第一个，0 是最后一个
 *     b.若调用此方法的线程在 timeout 时间内没通过屏障，则会抛出 TimeoutException(换句话说：超时抛出 TimeoutException)
 *
 */
public class BrokenBarrier {

    private final static CyclicBarrier BARRIER = new CyclicBarrier(10,
            () -> System.out.println("-----" + Thread.currentThread().getName() + ": I'm the last one----"));

    public static void main(String[] args) throws InterruptedException {
        System.out.println(CommonUtil.getThreadPid());
        //10个线程为一组使用 CyclicBarrier
        for (int i = 1; i <= 10; i++) {
            Eat eat = new Eat("E" + i);
            eat.start();
            if (i == 5) {
                //睡眠1秒，等待 t5 开始 run
                TimeUnit.SECONDS.sleep(1);
                eat.interrupt();
                System.out.println(eat.getName() + " interrupted...");
            }
        }
        //等待所有 eat 执行完成
        TimeUnit.SECONDS.sleep(5);
        System.out.println("------------------------------------------------------------");
        if (BARRIER.isBroken()) {
            //修复屏障
            BARRIER.reset();
            System.out.println("BARRIER was broken,I repaired it.Broken state = " + BARRIER.isBroken());
        }
        System.out.println("------------------------------------------------------------");
        for (int i = 1; i <= 10; i++) {
            new Drive("D" + i).start();
        }
    }

    static class Eat extends Thread {

        public Eat(String name) {
            super(name);
        }

        @Override
        public void run() {
            try {
                System.out.println(this.getName() + " entering...");
                BARRIER.await();
                //10个 eat 都不会打出此句，因为 await() 抛出了异常，直接进入了 catch 语句
                System.out.println(this.getName() + " finished waiting...");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(this.getName() + " finished eating...");
        }
    }

    static class Drive extends Thread {

        public Drive(String name) {
            super(name);
        }

        @Override
        public void run() {
            try {
                System.out.println(this.getName() + " leaving...");
                if ("D5".equals(this.getName())) {
                    //D5 比较着急，最多等待5秒，超过5秒就会抛出 TimeoutException
                    int await = BARRIER.await(5, TimeUnit.SECONDS);
                    //打印 await 的返回值(若超过了5秒抛出了异常，就不会打印这句了)
                    System.out.println(this.getName() + ": await num = " + await);
                } else {
                    BARRIER.await();
                }
            } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                e.printStackTrace();
            }
            System.out.println(this.getName() + " arrived...");
        }
    }
}
