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
 *   具体实现如下
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
