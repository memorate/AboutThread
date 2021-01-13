package anchor.thread.experiment;

import java.util.concurrent.TimeUnit;

/**
 * @author Anchor
 *
 * Thread类中有三个interrupt相关方法，interrupt并不会直接暴力终止线程，而是告诉线程可以终止了，在适合的时候终止
 *   public void interrupt()               //中断线程
 *   public boolean isInterrupted()        //判断线程是否被中断
 *   public static boolean interrupted()   //判断线程是否被中断，并清除当前中断状态
 *
 * 通过interrupt()方法来终止线程
 *    需要在线程睡眠的try-catch语句块中做补偿处理，否则无法终止线程。
 */
public class Interrupt {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(new CustomThread(), "customThread");
        thread.start();
        System.out.println(thread.getName() + "'s state: " + thread.getState().name());
        TimeUnit.SECONDS.sleep(3);
        thread.interrupt();
    }

    static class CustomThread implements Runnable {
        public void run() {
            System.out.println(Thread.currentThread().getName() + " start running...");
            while (true) {
                try {
                    //线程sleep期间如果调用了interrupt()，会抛InterruptedException异常
                    //并且中断标志会被清除
                    System.out.println(Thread.currentThread().getName() + " start sleeping...");
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    //做补偿处理，再次interrupt
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
                if (Thread.currentThread().isInterrupted()){
                    System.out.println(Thread.currentThread().getName() + " is exiting...");
                    break;
                }
            }
        }
    }
}
