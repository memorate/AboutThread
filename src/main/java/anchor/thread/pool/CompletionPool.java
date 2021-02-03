package anchor.thread.pool;

import anchor.thread.pool.base.CustomFactory;
import anchor.thread.pool.base.CustomRejectHandler;
import anchor.thread.util.CommonUtil;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Anchor
 */
public class CompletionPool {

    public static void main(String[] args) {
        System.out.println(CommonUtil.getThreadPid());
        ExecutorCompletionService service = init();

    }

    private static ExecutorCompletionService init() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                4,
                12,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new CustomFactory(),
                new CustomRejectHandler());
        return new ExecutorCompletionService<>(executor);
    }
}
