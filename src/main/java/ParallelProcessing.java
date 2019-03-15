import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.stream.Stream;

public class ParallelProcessing {

    static class Job {
        private int startTime;
        private int duration;
        private int threadIndex;

        Job(int duration) {
            this.duration = duration;
        }
    }

    static class Thread {
        private int index;
        private Job job;

        Thread(int index) {
            this.index = index;
        }

        void run(Job job, int now) {
            this.job = job;
            this.job.startTime = now;
            this.job.threadIndex = index;
        }

        void finish() {
            this.job = null;
        }

        int getNextFreeTime() {
            return job.startTime + job.duration;
        }

        int getIndex() {
            return index;
        }
    }

    static class Heap {

        @FunctionalInterface
        interface ThreadComparator {
            boolean hasHigherPriority(Thread first, Thread second);
        }

        private ThreadComparator comparator;
        private Thread[] array;
        private int size;
        private int maxSize;

        Heap(int maxSize, ThreadComparator comparator) {
            this.maxSize = maxSize;
            this.comparator = comparator;
            this.array = new Thread[maxSize];
            this.size = 0;
        }

        void insert(Thread thread) {
            if (size == maxSize) {
                throw new IllegalArgumentException("Heap is full!");
            }
            array[size] = thread;
            siftUp(size);
            size++;
        }

        Thread peekBest() {
            return array[0];
        }

        Thread extractBest() {
            Thread root = array[0];
            if (size > 1) {
                array[0] = array[size - 1];
                siftDown(0);
            }
            size--;
            return root;
        }

        boolean isEmpty() {
            return size == 0;
        }

        boolean isNotEmpty() {
            return size > 0;
        }

        private int parentIndex(int index) {
            return (index - 1) / 2;
        }

        private int leftChildIndex(int index) {
            return 2 * index + 1;
        }

        private int rightChildIndex(int index) {
            return 2 * index + 2;
        }

        private void siftUp(int index) {
            for (int parentIndex = parentIndex(index); index > 0
                    && comparator.hasHigherPriority(array[index], array[parentIndex]);
                    index = parentIndex, parentIndex = parentIndex(index)) {
                swap(parentIndex, index);
            }
        }

        private void siftDown(int index) {
            int bestIndex = index;

            int leftChildIndex = leftChildIndex(index);
            if (leftChildIndex < array.length && comparator.hasHigherPriority(array[leftChildIndex], array[bestIndex])) {
                bestIndex = leftChildIndex;
            }

            int rightChildIndex = rightChildIndex(index);
            if (rightChildIndex < array.length && comparator.hasHigherPriority(array[rightChildIndex], array[bestIndex])) {
                bestIndex = rightChildIndex;
            }

            if (index != bestIndex) {
                swap(index, bestIndex);
                siftDown(bestIndex);
            }
        }

        private void swap(int index1, int index2) {
            Thread value1 = array[index1];
            array[index1] = array[index2];
            array[index2] = value1;
        }
    }

    static class ThreadPoolExecutor {
        private Heap idleThreads;
        private Heap busyThreads;

        ThreadPoolExecutor(int maxWorkers) {
            idleThreads = new Heap(maxWorkers, (first, second) -> first.getIndex() < second.getIndex());
            busyThreads = new Heap(maxWorkers, (first, second) -> first.getNextFreeTime() < second.getNextFreeTime()
                    || (first.getNextFreeTime() == second.getNextFreeTime() && first.getIndex() < second.getIndex()));

            for (int index = 0; index < maxWorkers; index++) {
                idleThreads.insert(new Thread(index));
            }
        }

        void processJobs(Job[] jobs) {
            for (int index = 0, now = 0; index < jobs.length;) {
                updateBusyThreadsHeap(now);
                if (idleThreads.isEmpty()) {
                    now = busyThreads.peekBest().getNextFreeTime();
                } else {
                    allocateJobToIdleThread(jobs[index], now);
                    index++;
                }
            }
        }

        private void updateBusyThreadsHeap(int now) {
            while (busyThreads.isNotEmpty() && busyThreads.peekBest().getNextFreeTime() == now) {
                Thread thread = busyThreads.extractBest();
                thread.finish();
                idleThreads.insert(thread);
            }
        }

        private void allocateJobToIdleThread(Job job, int now) {
            Thread thread = idleThreads.extractBest();
            thread.run(job, now);
            busyThreads.insert(thread);
        }
    }

    public static void main(String[] args) throws IOException {
        FastScanner in = new FastScanner();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(in.nextInt());

        Job[] jobs = new Job[in.nextInt()];
        for (int index = 0; index < jobs.length; index++) {
            jobs[index] = new Job(in.nextInt());
        }

        executor.processJobs(jobs);

        writeResponse(jobs);
    }

    private static void writeResponse(final Job[] jobs) {
        try (final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out))) {
            Stream.of(jobs).forEach(job -> out.println(job.threadIndex + " " + job.startTime));
        }
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
