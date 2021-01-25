package anchor.thread.basis;

import anchor.thread.util.CommonUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author Anchor
 *
 * Thread类中stop()方法会立即终止线程，过于暴力，可能会导致不可预测情况发生，已被Deprecated
 */
public class Stop {
    public static void main(String[] args) throws Exception {
        System.out.println("pid: " + CommonUtil.getThreadPid());
        CustomThread thread = new CustomThread("customThread");
        thread.start();
        System.out.println(thread.getName() + "'s state: " + thread.getState().name());
        TimeUnit.SECONDS.sleep(3);
        thread.stop();
        System.out.println(thread.getName() + "'s state: " + thread.getState().name());
    }

    static class CustomThread extends Thread {
        public CustomThread(String name) {super(name);}

        @Override
        public void run() {
            System.out.println(this.getName() + " start running...");
            while (true) {
                try {
                    System.out.println(this.getName() + " start sleeping...");
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
