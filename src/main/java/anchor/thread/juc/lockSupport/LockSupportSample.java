package anchor.thread.juc.lockSupport;

import anchor.thread.util.CommonUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Anchor
 *
 * LockSupport 类中主要有两类方法，park() 和 unpark()，顾名思义：停车(阻塞线程)、开车(恢复线程)
 *
 * LockSupport 内部维护了一个 boolen 类型的参数 permit(定义在native方法中)，默认值是 false。
 *   调用 park() 时：若 primit 为 true，将其置为 false 并立即返回；若 permit 为 false，阻塞当前线程。
 *   调动 unpark() 时：若 permit 为 true，立即返回；若 permit 为 false，将其置为 true。
 *
 * LockSupport 可以完全代替 Condition 来实现等待/通知机制
 * 二者的差别：
 *   1.LockSupport使用前不需要先获取锁(Condition需要先获取锁)
 *   2.LockSupport不会产生死锁
 *     LockSupport可以在线程睡眠之前调用唤醒方法，前提是调用唤醒方法之前线程已启动(Condition signal()必须在await()之后调用)
 *
 *     以下代码不能！不能！！不能！！！正常唤醒线程，因为线程未启动就调用了 unpark()
 *     public static void main(String[] args) throws InterruptedException {
 *         System.out.println("pid: " + CommonUtil.getThreadPid());
 *         Thread thread = new Thread(() -> {
 *             System.out.println(Thread.currentThread().getName() + " start running...");
 *             LockSupport.park();
 *             System.out.println(Thread.currentThread().getName() + " exiting...");
 *         });
 *         LockSupport.unpark(thread);
 *         TimeUnit.SECONDS.sleep(1);
 *         thread.start();
 *     }
 */
public class LockSupportSample {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("pid: " + CommonUtil.getThreadPid());
        Thread thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " start running...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " invoke park()...");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + " exiting...");
        });
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(Thread.currentThread().getName() + " invoke unpark()...");
        //调用 unpark()，将 permit 置为 true，当 thread 调用 park()时，permit 为 true ，直接返回，因此线程可以被正常唤醒
        LockSupport.unpark(thread);
    }


}
