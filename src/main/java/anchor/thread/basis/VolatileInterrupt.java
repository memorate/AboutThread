package anchor.thread.basis;

import anchor.thread.util.CommonUtil;

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
        System.out.println("pid: " + CommonUtil.getThreadPid());
        CustomThread thread = new CustomThread("customThread");
        thread.start();
        System.out.println(thread.getName() + "'s state: " + thread.getState().name());
        TimeUnit.SECONDS.sleep(3);
        flag = true;
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
                //只有sleep()结束后才能执行if语句
                if (flag) {
                    System.out.println(this.getName() + " is exiting...");
                    this.interrupt();
                    break;
                }
            }
        }
    }
}
