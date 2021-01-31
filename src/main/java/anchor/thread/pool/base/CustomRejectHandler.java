package anchor.thread.pool.base;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Anchor
 *
 * 自定义线程池拒绝策略
 */
public class CustomRejectHandler implements RejectedExecutionHandler {

    public CustomRejectHandler() {
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("Queue size = " + executor.getQueue().size() +
                ", active thread num = " + executor.getActiveCount() +
                ", task num = " + executor.getTaskCount() +
                ", ignore task = " + r.toString());
    }
}
