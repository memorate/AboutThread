package anchor.thread.juc.lockSupport;

import anchor.thread.util.CommonUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Anchor
 *
 * LockSupport 类中有以下六个静态 park 方法
 *
 * 1.void park()
 *   若 permit 为 false 时，阻塞当前线程，并且只有以下三种情况会打断线程的等待状态：
 *    a.其他线程 unpark() 了此线程
 *    b.其他线程调用 interrupt() 方法打断了此线程
 *    c.park() 方法不合逻辑的 return 了(俗称见鬼了)
 *
 * 2.void park(Object blocker)
 *   同 1，参数 blocker 是为了方便 jstack 排查问题时当做标记使用，无其他用处
 *   传一个类名比较好辨认的对象即可
 *
 * 3.void parkNanos(long nanos)
 *   同 1，但是增加了第四种情况：
 *    d.等待时间 nanos 到了
 *
 * 4.void parkNanos(Object blocker, long nanos)
 *   2、3 的结合体
 *
 * 5.void parkUntil(long deadline)
 *   同 3，但 deadline 是时间戳，而 2 中 nanos 是等待经过的时间。
 *
 * 6.void parkUntil(Object blocker, long deadline)
 *   2、5的结合体
 *
 * 总结：
 *    有两种方法可以 手动 结束线程等待状态：1.unpark();  2.interrupt();
 */
public class MultiplePark {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("pid: " + CommonUtil.getThreadPid());

        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " start running...");
            //等待 100 个小时
            LockSupport.parkNanos(new Logger1(), TimeUnit.HOURS.toNanos(100));
            System.out.println(Thread.currentThread().getName() + " is exiting...");
        });

        Thread t2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " start running...");
            //等待 100 个小时
            long deadLine = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(100);
            LockSupport.parkUntil(new Logger2(), deadLine);
            System.out.println(Thread.currentThread().getName() + " is exiting...");
        });
        t1.setName("t1");
        t2.setName("t2");
        t1.start();
        t2.start();
        TimeUnit.SECONDS.sleep(3);
        //虽然 t1 和 t2 都将等待100个小时，但是调用 interrupt() 之后会立即结束等待
        t1.interrupt();
        t2.interrupt();
    }

    static class Logger1 {
    }

    static class Logger2 {
    }
}
