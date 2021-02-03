package anchor.thread.basis;

import anchor.thread.pool.base.CustomFactory;
import anchor.thread.pool.base.CustomRejectHandler;
import anchor.thread.util.CommonUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Anchor
 *
 * CAS —— CompareAndSwap
 *
 * 加一的三个原子操作：
 *   1.a = counter   从主从中拉去到线程的内存中
 *   2.a = a + 1     将副本加一
 *   3.counter = a   将副本的值刷新到主存
 *
 * 普通方式的多线程下加一操作(锁住三步):
 *   synchronize plus{
 *      a = counter
 *      a = a + 1
 *      counter = a
 *   }
 *
 *  CompareAndSwap(只锁住最后一步):
 *   synchronize plus{
 *      counter = a
 *   }
 *  具体实现见下面代码
 *
 *  CAS存在的问题：
 *    1.ABA问题：
 *      CAS的核心在于更新前检查值是否变化，若无，则更新。但如果一个值原来是 A，变成了 B，最后又变成了 A，则会产生 ABA 问题，常用的做法是
 *      加版本号，1A - 2B - 3A。Java 提供了 AtomicStampedReference 类来解决 ABA 问题。
 *    2.循环时间长，开销大
 *
 *  ABA的实际问题：
 *    用户 U 的余额为 100，正在取钱，打算取 50 元。由于某些原因，有两个线程 A、B，同时执行取钱这一操作，都想将余额从 100 减为 50。
 *      A: 获取当前值 100，期望更新为 50
 *      B: 获取当前值 100，期望更新为 50
 *    假设 A 成功，余额被更新为 50，B 被阻塞了，而此时正好有一线程 C 在往账户里打钱，计划打 50 元
 *      C: 获取当前值 50，期望更新为 100
 *    C 成功了，更新余额为 100。然后 B 阻塞结束，继续运行，B 执行 CAS 成功，最终余额变为了 50。
 *  期望: 初始余额 100 - 取钱 50 + 打钱 50 = 100 元
 *  实际: 初始余额 100 - 取钱 50 + 打钱 50 - 错误取钱 50 = 50 元
 *  用户: 打给我的 50 块哪去了？？？？？
 */
public class CAS {

    private final static int TASK_NUM = 1000000;
    private final static CountDownLatch LATCH = new CountDownLatch(TASK_NUM);
    private static volatile int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println(CommonUtil.getThreadPid());
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                10,
                20,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new CustomFactory(),
                new CustomRejectHandler());
        for (int i = 0; i < TASK_NUM; i++) {
            executor.execute(() -> {
                try {
                    for (int j = 0; j < 100; j++) {
                        plusOne();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    LATCH.countDown();
                }
            });
        }
        LATCH.await();
        executor.shutdown();
        System.out.println(counter);
    }

    private static void plusOne() {
        int expect = get();
        //无限循环直到 CAS 成功
        while (!cas(expect, expect + 1)) {
            expect = get();
        }
    }

    private static int get() {
        return counter;
    }

    /**
     * CompareAndSwap 的核心
     * 每次赋值前去对比副本和主存中的值是否一致，若是，交换
     *
     * @param copy    获取到 counter 副本的值
     * @param expect  期望加一后的结果
     * @return 是否交换成功
     */
    private static synchronized boolean cas(int copy, int expect) {
        if (copy == get()) {
            counter = expect;
            return true;
        }
        return false;
    }
}
