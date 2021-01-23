package anchor.thread.condition;

import anchor.util.CommonUtil;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Anchor
 *
 * 使用 Condition 模拟实现 BlockingQueue
 */
public class BlockingQueue<E> {
    int size;
    LinkedList<E> list = new LinkedList<>();

    final static ReentrantLock LOCK = new ReentrantLock();
    final static Condition FULL = LOCK.newCondition();
    final static Condition EMPTY = LOCK.newCondition();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("pid: " + CommonUtil.getThreadPid());
        BlockingQueue<Integer> queue = new BlockingQueue<>(3);
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    queue.add(finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    Integer element = queue.remove();
                    System.out.println(element);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public BlockingQueue(int size) {
        this.size = size;
    }

    public void add(E element) throws InterruptedException{
        LOCK.lock();
        try {
            while (list.size() == size){
                FULL.await();
            }
            list.add(element);
            EMPTY.signal();
        }finally {
            LOCK.unlock();
        }
    }

    public E remove() throws InterruptedException {
        E element;
        LOCK.lock();
        try {
            while (list.size() == 0){
                EMPTY.await();
            }
            element = list.remove();
            FULL.signal();
            return element;
        }finally {
            LOCK.unlock();
        }
    }

}
