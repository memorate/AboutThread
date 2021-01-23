package anchor.thread.condition;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Anchor
 *
 * Condition 可以代替 Object 中的 wait() notify() 实现等待/通知机制
 * Condition 由 ReentrantLock 对象调用 newCondition()创建
 */
public class ConditionUse {

    final static ReentrantLock LOCK = new ReentrantLock();
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
        public Wait() {
            super("Wait");
        }

        @Override
        public void run() {
            System.out.println(this.getName() + " start running...");
            LOCK.lock();
            try {
                System.out.println(this.getName() + " start waiting...");
                CONDITION.await();
                System.out.println(this.getName() + " resume...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                LOCK.unlock();
                System.out.println(this.getName() + " release lock and finish job...");
            }
        }
    }

    static class Signal extends Thread {
        public Signal() {
            super("Signal");
        }

        @Override
        public void run() {
            System.out.println(this.getName() + " start running...");
            LOCK.lock();
            try {
                System.out.println(this.getName() + " signal...");
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
