package anchor.thread.condition;

import anchor.util.CommonUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Anchor
 *
 * await的作用：释放锁并使线程进入等待状态。进入等待状态后可被 signal() 唤醒。
 * 调用 await() 后有两种方式结束等待状态：1.被 signal() 2.线程被 interrupt()
 *
 * Condition 类中有五个 await 方法。抛出 InterruptedException 异常的方法等待状态都可以被 interrupt() 终结
 * 1.void await() throws InterruptedException;
 *   释放锁并使当前线程进入等待状态。可被 interrupt() 打断。
 *
 * 2.void awaitUninterruptibly();
 *   释放锁并使当前线程进入等待状态。不可被 interrupt() 打断。
 *
 * 3.long awaitNanos(long nanosTimeout) throws InterruptedException;
 *   释放锁并使当前线程进入等待状态。可被 interrupt() 打断。
 *     a.nanosTimeout 表示指定的等待时间(单位为纳秒)，若指定时间到之后线程未被唤醒，则会自动醒来。
 *     b.被唤醒时返回实际等待时间，自动唤醒时返回一个负数。
 *
 * 4.boolean await(long time, TimeUnit unit) throws InterruptedException;
 *   释放锁并使当前线程进入等待状态。可被 interrupt() 打断。
 *     a.time 表示指定的等待时间(可以更自由的指定时间单位)，若指定时间到之后线程未被唤醒，则会自动醒来。
 *     b.被唤醒时返回 true，自动唤醒时返回 false。
 *
 * 5.boolean awaitUntil(Date deadline) throws InterruptedException;
 *   释放锁并使当前线程进入等待状态。可被 interrupt() 打断。
 *     a.deadline 表示一个时间点，若此时间点到来之后线程未被唤醒，则会自动醒来。
 *     b.被唤醒时返回 true，自动唤醒时返回 false。
 */
public class Await {

    final static ReentrantLock LOCK = new ReentrantLock();
    final static Condition CONDITION1 = LOCK.newCondition();
    final static Condition CONDITION2 = LOCK.newCondition();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("pid: " + CommonUtil.getThreadPid());
        T1 t1 = new T1();
        T2 t2 = new T2();
        t1.start();
        TimeUnit.SECONDS.sleep(2);
        t2.start();
        t1.join();
        t2.join();
    }

    static class T1 extends Thread {
        public T1() {
            super("T1");
        }

        @Override
        public void run() {
            System.out.println(this.getName() + " start running...");
            LOCK.lock();
            try {
                //被 signal() 了会返回 true，超过了等待时间返回 false
                boolean await = CONDITION1.await(5, TimeUnit.SECONDS);
                System.out.println("await: " + await);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                LOCK.unlock();
            }
        }
    }

    static class T2 extends Thread {
        public T2() {
            super("T2");
        }

        @Override
        public void run() {
            System.out.println(this.getName() + " start running...");
            LOCK.lock();
            try {
                CONDITION1.signal();
                //被 signal() 了会返回实际等待的时间，超过了等待时间返回一个负数
                long await = CONDITION2.awaitNanos(TimeUnit.SECONDS.toNanos(5));
                System.out.println("await: " + await);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                LOCK.unlock();
            }
        }
    }
}
