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
 *                  TimeUnit.SECONDS,
 *                  new SynchronousQueue<Runnable>(),
 *                  Executors.defaultThreadFactory(),
 *                  new ThreadPoolExecutor.AbortPolicy()
 *   );
 *
 *  1.coreSize 为 0，maximumPoolSize 为 Integer.MAX_VALUE，说明没有核心线程，且线程数没有上限，空闲线程的存活时间为 60 秒
 *  2.Cached 这里可以理解为临时，所有线程基本是临时创建，用完过时即毁
 */
public class CachedPool {
    private final static CountDownLatch LATCH = new CountDownLatch(50);

    public static void main(String[] args) throws InterruptedException {
        System.out.println(CommonUtil.getThreadPid());
        ExecutorService executor = Executors.newCachedThreadPool(new CustomFactory());
        for (int i = 0; i < 50; i++) {
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " running...");
                try {
                    //当不睡眠 1 秒时，部分线程可以不新建
                    TimeUnit.SECONDS.sleep(1);
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
