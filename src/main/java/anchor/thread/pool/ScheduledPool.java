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
 * ScheduledThreadPool，底层使用 ThreadPoolExecutor，参数如下：
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
}
