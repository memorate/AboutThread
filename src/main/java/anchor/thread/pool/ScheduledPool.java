package anchor.thread.pool;

import anchor.thread.pool.base.CustomFactory;
import anchor.thread.util.CommonUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Anchor
 *
 * ScheduledThreadPool 使用的是 ScheduledThreadPoolExecutor 类，但是底层还是对 ThreadPoolExecutor 的包装，参数如下：
 *
 *   new ThreadPoolExecutor(
 *                  corePoolSize,
 *                  Integer.MAX_VALUE,
 *                  0,
 *                  TimeUnit.NANOSECONDS,
 *                  new DelayedWorkQueue<>(),
 *                  Executors.defaultThreadFactory(),
 *                  new ThreadPoolExecutor.AbortPolicy()
 *   );
 *
 *   1.阻塞队列使用 DelayedWorkQueue，它是基于堆的优先级阻塞队列，延时时间短的任务排在队列前面
 *   2.核心线程创建时指定，最大线程数量不限，非核心线程存活时间是 0 纳秒
 *   3.DelayedWorkQueue 的最大容量是 Integer.MAX_VALUE，意味着可能会 OOM
 *   3.ScheduledThreadPoolExecutor 有三种执行方式：
 *     a.在指定延迟时间后执行
 *       public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit)
 *     b.按固定频率执行，当第一次 initialDelay 时间后执行，第二次 initialDelay + period，第三次 initialDelay + period * 2
 *       public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
 *     c.按固定延迟时间执行，当第一次 command 执行结束后，延迟 delay 时间后执行第二次，第二次结束后延迟 delay 时间后执行第三次
 *       public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
 *
 *   若是 Callable 类型的任务，可以通过 ScheduledFuture 的 get() 方法来获取返回值
 */
public class ScheduledPool {
    private final static CountDownLatch LATCH = new CountDownLatch(10);

    public static void main(String[] args) throws InterruptedException {
        System.out.println(CommonUtil.getThreadPid());
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3, new CustomFactory());
        for (int i = 0; i < 10; i++) {
            executor.schedule(() -> {
                System.out.println(Thread.currentThread().getName() + " running...");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    LATCH.countDown();
                }
            }, 3, TimeUnit.SECONDS);
        }
        LATCH.await();
        System.out.println("All done!");
        executor.shutdown();
        System.out.println("Pool shutdown!");
    }

    /**
     * 还有一个 SingleThreadScheduledExecutor，功能同 ScheduledThreadPool，参数如下：
     *
     *   new ThreadPoolExecutor(
     *                 1,
     *                 1,
     *                 0L,
     *                 TimeUnit.MILLISECONDS,
     *                 new LinkedBlockingQueue<Runnable>(),
     *                 new CustomFactory()
     *   );
     */
    private ScheduledExecutorService initSingle() {
        return Executors.newSingleThreadScheduledExecutor();
    }
}
