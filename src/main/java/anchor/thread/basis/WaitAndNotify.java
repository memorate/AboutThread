package anchor.thread.basis;

import anchor.thread.util.CommonUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author Anchor
 *
 * wait()方法会释放所持有的锁，而sleep()不会
 */
public class WaitAndNotify {
    private final static Object OBJECT = new Object();

    public static void main(String[] args) throws Exception {
        System.out.println("pid: " + CommonUtil.getThreadPid());
        CustomThreadX thread1 = new CustomThreadX("customThread1");
        thread1.start();
        //睡眠三秒再创建并启动thread2，确保thread1已经执行了wait()
        TimeUnit.SECONDS.sleep(3);
        CustomThreadY thread2 = new CustomThreadY("customThread2", thread1);
        thread2.start();
    }

    static class CustomThreadX extends Thread {
        public CustomThreadX(String name) {super(name);}

        @Override
        public void run() {
            synchronized (OBJECT) {
                System.out.println(this.getName() + " start running...");
                try {
                    System.out.println("Before waiting Object...");
                    OBJECT.wait();
                    System.out.println("After waiting Object,thread state: " + this.getState());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class CustomThreadY extends Thread {
        private Thread waitThread;

        public CustomThreadY(String name, Thread thread) {
            super(name);
            waitThread = thread;
        }

        @Override
        public void run() {
            System.out.println(this.getName() + " start running...");
            synchronized (OBJECT) {
                System.out.println("Before notifying Object...");
                //打印出执行wait()之后thread1的状态
                System.out.println("WaitThread name: " + waitThread.getName() + ",state: " + waitThread.getState());
                //唤醒thread1
                OBJECT.notify();
                System.out.println("After notifying Object...");
            }
        }
    }
}
