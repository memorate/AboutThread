package anchor.thread.experiment;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author Anchor
 *
 * 死锁演示
 *  thread1持有o1的锁，等待o2的锁
 *  thread2持有o2的锁，等待o1的锁
 */
public class Deadlock {
    public static void main(String[] args) {
        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        //方便jstack查看堆栈信息（jstack -l pid）
        System.out.println("pid: " + pid);
        Object1 o1 = new Object1();
        Object2 o2 = new Object2();
        new Thread(new CustomThread(o1, o2, 1, 2, true), "thread1").start();
        new Thread(new CustomThread(o1, o2, 3, 4, false), "thread2").start();
    }

    static class CustomThread implements Runnable {
        final Object1 o1;
        final Object2 o2;
        int a, b;
        boolean flag;

        public CustomThread(Object1 o1, Object2 o2, int a, int b, boolean flag) {
            this.o1 = o1;
            this.o2 = o2;
            this.a = a;
            this.b = b;
            this.flag = flag;
        }

        public void run() {
            System.out.println(Thread.currentThread().getName() + " start running");
            try {
                if (flag) {
                    synchronized (o1) {
                        TimeUnit.SECONDS.sleep(10);
                        synchronized (o2) {
                            System.out.println(a + b);
                        }
                    }
                } else {
                    synchronized (o2) {
                        TimeUnit.SECONDS.sleep(10);
                        synchronized (o1) {
                            System.out.println(a + b);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Object1 {
    }

    static class Object2 {
    }
}
