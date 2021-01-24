package anchor.thread.lockSupport;

import anchor.util.CommonUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Anchor
 *
 * LockSupport 可以完全代替 Condition 来实现等待/通知机制
 * 二者的差别：
 *   1.LockSupport使用前不需要先获取锁(Condition需要先获取锁)
 *   2.LockSupport可以在线程睡眠之前调用唤醒方法，前提是调用唤醒方法之前线程已启动(Condition signal()必须在await()之后调用)
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
        //调用 unpark() 时线程还未执行 park()，但是线程可以被正常唤醒
        System.out.println(Thread.currentThread().getName() + " invoke unpark()...");
        LockSupport.unpark(thread);
    }


}
