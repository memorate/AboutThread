package anchor.thread.juc.semaphore;

import anchor.thread.util.CommonUtil;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author Anchor
 *
 * Semaphore(信号量) 的作用是控制访问特定资源线程的数量
 *
 * 它有两个构造方法：
 *  permits 用来控制可访问的数量，fair 用来控制是否是公平信号量(先到先得)。1 中默认是非公平信号量，效率会比公平信号量更高。
 *  1.public Semaphore(int permits)
 *  2.public Semaphore(int permits, boolean fair)
 *
 * Semaphore 通过 acquire() 来获取 permit，release() 来释放获取到的 permit。
 * 若未执行 acquire()，但是执行了 release()，会导致 permit 的数量永久减少1！！！！
 *
 * 线程 start() 之后若获取不到 permit，会卡在 acquire()，直到在其他线程 release() 后获取到了 permit 才会继续执行。
 *
 * 注意：release() 需要放在 finally 中，确保执行。
 *      但是有个隐患，若在 acquire() 之前发生了异常，未获取到 permit 但是执行了 release()，会使 permit 的总数减一！！！！
 *
 *      public void run() {
 *          try {
 *              someMethod();          //此处发生异常会直接执行 release()
 *              semaphore.acquire();
 *          } catch (InterruptedException e) {
 *              e.printStackTrace();
 *          } finally {
 *              semaphore.release();
 *          }
 *      }
 *
 *      解决方案见下述代码
 */
public class Sample {

    static Semaphore semaphore = new Semaphore(3);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("pid: " + CommonUtil.getThreadPid());

        for (int i = 1; i <= 10; i++) {
            new CustomThread("t" + i).start();
        }
    }

    static class CustomThread extends Thread {
        public CustomThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            //使用一个标记来解决未获取 permit 但是会释放的问题
            boolean flag = false;
            System.out.println(this.getName() + " start running...");
            try {
                semaphore.acquire();
                flag = true;
                System.out.println(this.getName() + " get one permit...");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //根据标记来判断是否释放 permit
                if (flag) {
                    semaphore.release();
                    System.out.println(this.getName() + " release one permit...");
                }
            }
        }
    }
}
