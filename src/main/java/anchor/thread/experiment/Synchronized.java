package anchor.thread.experiment;

/**
 * @author Anchor
 */
public class Synchronized {
    private int num;

    public static void main(String[] args) throws Exception {
        Synchronized object = new Synchronized();
        Counter counter1 = new Counter(object, "counter1");
        Counter counter2 = new Counter(object, "counter1");
        counter1.start();
        counter2.start();
        counter1.join();
        counter2.join();
        System.out.println(object.num);
    }

    static class Counter extends Thread {
        private Synchronized object;

        public Counter(Synchronized object, String name) {
            super(name);
            this.object = object;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                object.plus();
            }
        }
    }

    private synchronized void plus() {
        num++;
    }
}
