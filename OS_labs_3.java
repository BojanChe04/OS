import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;

class DotProduct {

    static double dotProduct = 0;
    // DEFINE OTHER GLOBAL VARIABLES
    static final BoundedRandomGenerator random = new BoundedRandomGenerator();
    static Semaphore semaphore;
    static double maxProduct = Double.NEGATIVE_INFINITY;

    private static final int ARRAY_LENGTH = 10000000;
    private static final int NUM_THREADS = 10;

    // TODO: Define synchronization elements
    static Lock lock;
    static Thread[] threads;

    static void init() {
        // TODO: Initialize synchronization elements
        lock = new ReentrantLock();
        threads = new Thread[NUM_THREADS];
        semaphore = new Semaphore(0);
    }

    // DO NOT CHANGE
    public static int[] getSubArray(int[] array, int start, int end) {
        return Arrays.copyOfRange(array, start, end);
    }

    public static void main(String[] args) {

        init();

        int[] a = ArrayGenerator.generate(ARRAY_LENGTH);
        int[] b = ArrayGenerator.generate(ARRAY_LENGTH);

        // TODO: Make the CalculateThread class a thread and start 10 instances
        // Each instance should take a subarray from the original array with equal length
        int size = ARRAY_LENGTH / NUM_THREADS;

        // TODO: Create and start 10 threads for calculating the dot product
        for (int i = 0; i < NUM_THREADS; i++) {
            int pocetok = i * size;
            int kraj = Math.min((i + 1) * size, ARRAY_LENGTH);
            threads[i] = new CalculateThread(a, b, pocetok, kraj);
            threads[i].start();
        }
//        for (int i = 0; i < NUM_THREADS; i++) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//        }
        // DO NOT CHANGE
        System.out.println("Your calculated dot product is: " + dotProduct);
        System.out.println("The actual dot product is: " + ArrayGenerator.calculateDotProduct(a, b));
        System.out.println("Maximum local dot product from a single thread: " + maxProduct);

        SynchronizationChecker.checkResult();
    }

    // TO DO: Make the CalculateThread class a thread, you can add methods and attributes
    static class CalculateThread extends Thread {

        private int[] a;
        private int[] b;
        int startSearch;
        int endSearch;

        public CalculateThread(int[] a, int[] b, int startSearch, int endSearch) {
            this.a = a;
            this.b = b;
            this.startSearch = startSearch;
            this.endSearch = endSearch;
        }

        public void calculateDotProductParallel() {
            // TO DO: Implement and run this method from the thread
            // The method should not return a result
            // Take care of the proper synchronization
            double sum = 0.0;
            for (int i = startSearch; i < endSearch; i++) {
                sum += a[i] * b[i];
            }
            lock.lock();
            try {
                dotProduct += sum;
                if(sum > maxProduct) {
                    maxProduct = sum;
                }
            } finally {
                lock.unlock();
            }
            semaphore.release();//signalizirame deka niskata zavrsila
        }

        @Override
        public void run() {
            calculateDotProductParallel();
        }
    }

    /******************************************************
     // DO NOT CHANGE THE CODE BELOW TO THE END OF
     THE FILE
     *******************************************************/

    static class BoundedRandomGenerator {
        static final Random random = new Random();
        static final int RANDOM_BOUND_UPPER = 10;
        static final int RANDOM_BOUND_LOWER = 6;

        public int nextInt() {
            return random.nextInt(RANDOM_BOUND_UPPER - RANDOM_BOUND_LOWER) + RANDOM_BOUND_LOWER;
        }
    }

    static class ArrayGenerator {

        private static double actualDotProduct = 0.0;

        static int[] generate(int length) {
            int[] array = new int[length];
            for (int i = 0; i < length; i++) {
                int num = DotProduct.random.nextInt();
                array[i] = num;
            }
            return array;
        }

        public static double calculateDotProduct(int[] a, int[] b) {
            double prod = 0.0;
            for (int i = 0; i < a.length; i++) {
                prod += a[i] * b[i];
            }
            actualDotProduct = prod;
            return prod;
        }
    }

    static class SynchronizationChecker {
        public static void checkResult() {
            if (ArrayGenerator.actualDotProduct != dotProduct) {
                throw new RuntimeException("The calculated result is not equal to the actual dot product!");
            }
        }
    }
}
