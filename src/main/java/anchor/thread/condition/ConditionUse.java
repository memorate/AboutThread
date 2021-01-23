package anchor.thread.condition;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Anchor
 *
 * 1.Condition 可以代替 Object 中的 wait() notify() 实现等待/通知机制
 * 2.Condition 由 ReentrantLock 对象调用 newCondition()创建
 * 3.调用 Condition 的方法前需先获取该对象关联的 ReentrantLock 锁
 */
public class ConditionUse {

    final static ReentrantLock LOCK = new ReentrantLock();
    /**
     * 创建 Condition 对象
     */
    final static Condition CONDITION = LOCK.newCondition();

    public static void main(String[] args) throws InterruptedException {
        Wait wait = new Wait();
        Signal signal = new Signal();
        wait.start();
        signal.start();
        wait.join();
        signal.join();
    }

    static class Wait extends Thread {
        public Wait() {super("Wait");}

        @Override
        public void run() {
            System.out.println(this.getName() + " start running...");
            LOCK.lock();
            try {
                System.out.println(this.getName() + " start waiting...");
                //作用同object.wait()，且会释放锁LOCK
                CONDITION.await();
                System.out.println(this.getName() + " resume...");
                //调用wait()后，若等待过程中线程被interrupt()，wait()方法会抛出InterruptedException异常
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                LOCK.unlock();
                System.out.println(this.getName() + " release lock and finish job...");
            }
        }
    }

    static class Signal extends Thread {
        public Signal() {super("Signal");}

        @Override
        public void run() {
            System.out.println(this.getName() + " start running...");
            LOCK.lock();
            try {
                System.out.println(this.getName() + " signal...");
                //通知 Condition 结束等待，Condition 收到 signal() 后需要再次获取到锁才能继续执行
                CONDITION.signal();
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                LOCK.unlock();
                System.out.println(this.getName() + " release lock and finish job...");
            }
        }
    }
}
