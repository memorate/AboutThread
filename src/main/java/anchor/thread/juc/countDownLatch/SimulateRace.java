package anchor.thread.juc.countDownLatch;

import anchor.thread.util.CommonUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Anchor
 *
 * 模拟跑步比赛，有三个选手、一个裁判。
 * 选手就位后等待裁判指令，裁判发令后选手开始比赛，所有选手完成后比赛结束
 *
 * 用两个 CountDownLatch 来模拟裁判和选手。
 *   选手 start() 之后调用 referee.await() 进入等待状态
 *   裁判发令(referee.countDown())后选手开始比赛，每个选手完成后调用 referee.countDown()
 */
public class SimulateRace {

    static CountDownLatch referee = new CountDownLatch(1);
    static CountDownLatch latch = new CountDownLatch(3);

    public static void main(String[] args) throws InterruptedException {
        System.out.println(CommonUtil.getThreadPid());
        long start = System.currentTimeMillis();
        CustomThread t1 = new CustomThread("t1", 3);
        CustomThread t2 = new CustomThread("t2", 5);
        CustomThread t3 = new CustomThread("t3", 4);
        t1.start();
        t2.start();
        t3.start();
        //裁判做准备
        TimeUnit.SECONDS.sleep(2);
        System.out.println("---------Race starting---------");
        //裁判发令
        referee.countDown();
        //等待选手完成
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("---------Race over---------");
        //比赛经过的时间
        System.out.println("Race completed,elapse time = " + TimeUnit.MILLISECONDS.toSeconds((end - start)) + " seconds");
    }

    static class CustomThread extends Thread {
        int sleepTime;

        public CustomThread(String name, int sleepTime) {
            super(name);
            this.sleepTime = sleepTime;
        }

        @Override
        public void run() {
            System.out.println(this.getName() + " is ready...");
            long start = 0L, end = 0L;
            try {
                //等待裁判发令
                referee.await();
                start = System.currentTimeMillis();
                System.out.println(this.getName() + " start running...");
                //模拟选手耗时
                TimeUnit.SECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
                end = System.currentTimeMillis();
                System.out.println(this.getName() + ": completed race,elapse time = " + TimeUnit.MILLISECONDS.toSeconds((end - start)) + " seconds");
            }
        }
    }
}
