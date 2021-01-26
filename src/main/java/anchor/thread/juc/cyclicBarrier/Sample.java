package anchor.thread.juc.cyclicBarrier;

import anchor.thread.util.CommonUtil;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @author Anchor
 *
 * CyclicBarrier(循环屏障)，它可以使多个线程互相等待，直到所有线程都达到了某个屏障点，它们才可以继续执行。
 * 例如：5个朋友一起吃饭，只有当大家都到了才可以开始吃；吃完只有当大家都坐上车了，车才可以开。
 *
 * CyclicBarrier 类内部维护了两个 int 类型的参数 parties 和 count。new CyclicBarrier 时指定 parties 的大小，并将 parties 的值赋给 count。
 *   每当 await() 被调用时：当前线程进入等待状态，并且 count 减一
 *   当 count 减为 0 时：唤醒所有线程，并再次将 parties 的值赋给 count(因此叫作循环屏障，可以循环多次使用)
 *
 * CyclicBarrier 类有两个构造方法：
 *  1.CyclicBarrier(int parties)
 *  2.CyclicBarrier(int parties, Runnable barrierAction)
 *    parties - 需要互相等待的线程数量
 *    barrierAction - 在屏障打开前，由最后一个进入屏障的线程执行的自定义操作
 */
public class Sample {

    private final static CyclicBarrier BARRIER = new CyclicBarrier(5,
            () -> System.out.println(Thread.currentThread().getName() + ": 我是最后一个，我准备好了"));
    private final static Random RANDOM = new Random();

    public static void main(String[] args) {
        System.out.println(CommonUtil.getThreadPid());
        for (int i = 1; i <= 5; i++) {
            //一共有五个线程
            new CustomThread("t" + i).start();
        }
    }

    static class CustomThread extends Thread {

        public CustomThread(String name) {
            super(name);
        }

        /**
         * 模拟五个朋友一起约好去吃饭
         */
        private void eat() {
            //enter 代表准备去吃饭的时间点，finished 代表坐在餐桌上的时间点，start 代表开始吃饭的时间点
            long enter = System.currentTimeMillis(), finished, start;
            try {
                //用一个随机数模拟准备的时间
                TimeUnit.SECONDS.sleep(RANDOM.nextInt(5));
                finished = System.currentTimeMillis();
                BARRIER.await();
                start = System.currentTimeMillis();
                //prepare time 是准备花费的时间，wait time 是准备好了但是在等待其他人准备好的时间
                System.out.println(this.getName() + ": prepare time = " + (finished - enter)
                        + "ms, wait time = " + (start - finished) + "ms --eat--");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        /**
         * 模拟五个朋友吃完饭开车回家
         * 流程与 eat() 相同。写此方法只是为了验证 CyclicBarrier 可以循环使用
         */
        private void drive() {
            long leave = System.currentTimeMillis(), finished, startOff;
            try {
                TimeUnit.SECONDS.sleep(RANDOM.nextInt(5));
                finished = System.currentTimeMillis();
                BARRIER.await();
                startOff = System.currentTimeMillis();
                System.out.println(this.getName() + ": prepare time = " + (finished - leave)
                        + "ms, wait time = " + (startOff - finished) + "ms --drive--");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            eat();
            drive();
        }
    }
}
