package hu.bme.mit.brszta.examples;

public class ThreadingExample {

    private static class Printer implements Runnable {

        private final String id;
        private final long timeout;

        private Printer(String id, long timeout) {
            this.id = id;
            this.timeout = timeout;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; ++i) {
                System.out.println("Printer " + id + " run");
                try {
                    synchronized (this) {
                        this.wait(timeout);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new Printer("1", 100));
        Thread t2 = new Thread(new Printer("2", 200));
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
