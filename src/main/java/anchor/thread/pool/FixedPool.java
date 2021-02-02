package anchor.thread.pool;

import anchor.thread.pool.base.CustomFactory;
import anchor.thread.util.CommonUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Anchor
 *
 * FixedThreadPool，底层使用 ThreadPoolExecutor，参数如下：
 *
 *   new ThreadPoolExecutor(
 *                  nThreads,
 *                  nThreads,
 *                  0L,
 *                  TimeUnit.MILLISECONDS,
 *                  new LinkedBlockingQueue<Runnable>(),
 *                  Executors.defaultThreadFactory(),
 *                  new ThreadPoolExecutor.AbortPolicy()
 *   );
 *
 *   1.线程个数固定为 nThreads，最大线程数也为 nThreads，keepAliveTime 为 0。意味着线程池创建之后只能使用固定数量的线程
 *   2.阻塞队列使用 LinkedBlockingQueue，默认大小为 Integer.MAX_VALUE，任务过多时可能会导致 OOM
 */
public class FixedPool {

    private final static CountDownLatch LATCH = new CountDownLatch(5);

    public static void main(String[] args) throws InterruptedException {
        System.out.println(CommonUtil.getThreadPid());
        ExecutorService executor = Executors.newFixedThreadPool(3, new CustomFactory());
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
        LATCH.await();
        System.out.println("All done!");
        executor.shutdown();
        System.out.println("Pool shutdown!");
    }
}
