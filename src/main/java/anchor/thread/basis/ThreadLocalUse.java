package anchor.thread.basis;

import java.util.concurrent.TimeUnit;

/**
 * @author Anchor
 */
public class ThreadLocalUse {

    private final static ThreadLocal<String> PARAM = new ThreadLocal<>();

    public static void main(String[] args) throws Exception {
        PARAM.set("MainValue");
        new Thread(() -> {
            PARAM.set("ThreadValue");
            System.out.println(Thread.currentThread().getName() + ": " + PARAM.get());
            PARAM.remove();
        }).start();
        Thread.sleep(TimeUnit.SECONDS.toSeconds(2));
        System.out.println(Thread.currentThread().getName() + ": " + PARAM.get());
        PARAM.remove();
        System.out.println(Thread.currentThread().getName() + " after remove: " + PARAM.get());
    }
}
