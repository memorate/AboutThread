package anchor.thread.experiment;

import java.util.concurrent.TimeUnit;

/**
 * @author Anchor
 */
public class ThreadGroupUse {
    public static void main(String[] args) {
        ThreadGroup group = new ThreadGroup("group1");
        CustomThread thread1 = new CustomThread(group, "thread1");
        CustomThread thread2 = new CustomThread(group, "thread1");
        thread1.start();
        thread2.start();
        System.out.println("-------------Group-------------");
        System.out.println("name: " + group.getName());
        System.out.println("activeCount: " + group.activeCount());
        System.out.println("ActiveGroupCount: " + group.activeGroupCount());
        System.out.println("-------------Parent-------------");
        ThreadGroup parent = group.getParent();
        System.out.println("name: " + parent.getName());
        System.out.println("activeCount: " + parent.activeCount());
        System.out.println("ActiveGroupCount: " + parent.activeGroupCount());
        group.interrupt();
        group.list();
    }

    static class CustomThread extends Thread {
        public CustomThread(ThreadGroup group, String name) {
            super(group, name);
        }

        @Override
        public void run() {
            System.out.println(this.getName() + " start running...");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
