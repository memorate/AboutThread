package anchor.thread.experiment;

import java.util.concurrent.TimeUnit;

/**
 * @author Anchor
 *
 * 通过一个volatile变量来控制线程是否终止
 *    这种方式不能实现线程终止，只有等线程睡眠结束后才能终止
 */
public class VolatileInterrupt {
    private static volatile boolean flag = false;

    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(new CustomThread(), "customThread");
        thread.start();
        System.out.println(thread.getName() + "'s state: " + thread.getState().name());
        TimeUnit.SECONDS.sleep(3);
        flag = true;
    }

    static class CustomThread implements Runnable{
        public void run() {
            System.out.println(Thread.currentThread().getName() + " start running...");
            while (true){
                try {
                    System.out.println(Thread.currentThread().getName() + " start sleeping...");
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (flag){
                    System.out.println(Thread.currentThread().getName() + " is exiting...");
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}
