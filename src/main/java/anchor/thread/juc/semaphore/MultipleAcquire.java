package anchor.thread.juc.semaphore;

import anchor.thread.util.CommonUtil;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author Anchor
 *
 * Semaphore 有以下八个 acquire(throws InterruptedException 代表方法会响应 interrupt())
 *
 * 1.void acquire() throws InterruptedException
 *   获取一个信号量，开始获取但是还未获取到的过程中若线程被 interrupt()，会抛出 InterruptedException
 *
 * 2.void acquire(int permits) throws InterruptedException
 *   获取指定数量个信号量，开始获取但是还未获取到的过程中若线程被 interrupt()，会抛出 InterruptedException
 *
 * 3.void acquireUninterruptibly()
 *   获取一个信号量，不会响应 interrupt()
 *
 * 4.void acquireUninterruptibly(int permits)
 *   获取指定数量个信号量，不会响应 interrupt()
 *
 * 5.boolean tryAcquire()
 *   尝试获取一个信号量，不论成功与否，都会立即返回。成功返回 true，失败返回 false。不会响应 interrupt()
 *
 * 6.boolean tryAcquire(int permits)
 *   尝试获取指定数量个信号量，不论成功与否，都会立即返回。成功返回 true，失败返回 false。不会响应 interrupt()
 *
 * 7.boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException
 *   尝试在指定时间内获取一个信号量。在指定时间内成功返回 true，超时返回 false。会响应 interrupt()
 *
 * 8.boolean tryAcquire(int permits, long timeout, TimeUnit unit) throws InterruptedException
 *   尝试在指定时间内获取指定数量个信号量。在指定时间内成功返回 true，超时返回 false。会响应 interrupt()
 *
 * release 有两个方法：1.void release(); 2.void release(int permits);
 */
public class MultipleAcquire {

    static Semaphore semaphore = new Semaphore(3);
    static volatile boolean loop = true;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("pid: " + CommonUtil.getThreadPid());

        Thread t1 = new Thread(() -> {
            Thread thread = Thread.currentThread();
            System.out.println(thread.getName() + ": start running...");
            boolean flag = false;
            try {
                semaphore.acquireUninterruptibly(2);
                flag = true;
                //写个死循环，测试 acquireUninterruptibly() 是否会响应 interrupt()
                while (loop) {
                }
            } finally {
                if (flag) {
                    semaphore.release(2);
                    System.out.println(thread.getName() + ": release 2 permits...");
                }
            }
        });
        Thread t2 = new Thread(() -> {
            Thread thread = Thread.currentThread();
            System.out.println(thread.getName() + ": start running...");
            boolean acquired = false;
            try {
                System.out.println(thread.getName() + ": tryAcquire for 5 seconds...");
                //semaphore 中一共有3个 permits，t1 占了两个，理论上 tryAcquire() 会等待5秒后返回 false
                acquired = semaphore.tryAcquire(2, 5, TimeUnit.SECONDS);
                System.out.println(thread.getName() + ": tryAcquire result: " + acquired);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (acquired) {
                    semaphore.release(2);
                    System.out.println(thread.getName() + ": release 2 permits...");
                }
            }
        });
        t1.setName("t1");
        t2.setName("t2");
        t1.start();
        //睡眠1秒确保t1先拿到 permit
        TimeUnit.SECONDS.sleep(1);
        t2.start();
        t1.interrupt();
        System.out.println(Thread.currentThread().getName() + ": " + semaphore.availablePermits() + " permits left in semaphore...");
        t2.join();
        //结束t1中的死循环
        loop = false;
    }


}
