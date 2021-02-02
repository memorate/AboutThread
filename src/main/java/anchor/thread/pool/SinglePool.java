package anchor.thread.pool;

import anchor.thread.pool.base.CustomFactory;
import anchor.thread.util.CommonUtil;

import java.util.concurrent.*;

/**
 * @author Anchor
 *
 * SingleThreadExecutor，内部使用 ThreadPoolExecutor，参数如下：
 *
 *  new ThreadPoolExecutor(
 *                 1,
 *                 1,
 *                 0L,
 *                 TimeUnit.MILLISECONDS,
 *                 new LinkedBlockingQueue<Runnable>(),
 *                 Executors.defaultThreadFactory(),
 *                 new ThreadPoolExecutor.AbortPolicy()
 *   );
 *
 *  1.有且只有一个线程执行任务
 *  2.所有任务按先进先出的顺序执行
 *  3.当当前线程出现异常后，线程池会自动创建一个新的线程，始终保持池中只有一个线程存活
 */
public class SinglePool {

    private final static CountDownLatch LATCH = new CountDownLatch(5);

    public static void main(String[] args) throws InterruptedException {
        System.out.println(CommonUtil.getThreadPid());
        ExecutorService executor = Executors.newSingleThreadExecutor(new CustomFactory());
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
