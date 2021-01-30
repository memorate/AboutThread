package anchor.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Anchor
 */
public class SinglePool {

    public static void main(String[] args) {

    }

    private static ExecutorService init(){
        ExecutorService executor = Executors.newSingleThreadExecutor();

        return executor;

    }
}
