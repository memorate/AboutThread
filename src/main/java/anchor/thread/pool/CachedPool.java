package anchor.thread.pool;

import anchor.thread.pool.base.CustomFactory;
import anchor.thread.util.CommonUtil;

import java.util.concurrent.*;

/**
 * @author Anchor
 *
 * CachedThreadPool，内部使用 ThreadPoolExecutor，参数如下：
 *
 *   new ThreadPoolExecutor(
 *                  0,
 *                  Integer.MAX_VALUE,
 *                  60L,
 *                  TimeUnit.MILLISECONDS,
 *                  new SynchronousQueue<Runnable>(),
 *                  Executors.defaultThreadFactory(),
 *                  new ThreadPoolExecutor.AbortPolicy()
 *   );
 *
 */
public class CachedPool {
    private final static CountDownLatch LATCH = new CountDownLatch(5);

    public static void main(String[] args) throws InterruptedException {
        System.out.println(CommonUtil.getThreadPid());
        ExecutorService executor = Executors.newCachedThreadPool(new CustomFactory());
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " running...");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    LATCH.countDown();
                }
            });
        }
        new ThreadPoolExecutor(
                0,
                Integer.MAX_VALUE,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
        LATCH.await();
        System.out.println("All done!");
        executor.shutdown();
        System.out.println("Pool shutdown!");
    }
}
