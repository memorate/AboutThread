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
 * SingleThreadExecutor
 *
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
