package anchor.thread.pool.base;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Anchor
 *
 * 自定义的线程工厂
 *
 * 1.ThreadFactory 是一个接口，其中只定义了一个 public Thread newThread(Runnable r) 方法
 * 2.顾名思义，ThreadFactory 是线程的加工厂，其主要作用是生产线程(newThread())，一般用来在新建线程时设置线程的name、daemon、priority等
 * 3.线程池中都使用 ThreadFactory 来新建线程，若创建线程池时不指定，则使用 Executors.defaultThreadFactory
 *
 *  在项目中使用线程池时，若线程池中的线程有一个比较好辨识的名字，会比较好排查问题，因此至少要使用 ThreadFactory 来自定义线程的名字
 */
public class CustomFactory implements ThreadFactory {

    private final AtomicInteger counter = new AtomicInteger(1);

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        //设置为非daemon线程
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        //设置优先级为一般
        thread.setPriority(Thread.NORM_PRIORITY);
        //设置名字为 custom-thread-N
        thread.setName("Anchor-worker-" + counter.getAndIncrement());
        System.out.println("Build a thread in factory, thread name = " + thread.getName());
        return thread;
    }
}
