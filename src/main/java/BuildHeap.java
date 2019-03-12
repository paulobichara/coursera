import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

class BuildHeap {

    static class Heap {

        private int[] array;
        private List<Swap> swaps;

        Heap(int[] array) {
            this.array = array;
            this.swaps = new ArrayList<>();

            for (int index = array.length - 1; index > 0; index--) {
                siftUp(index);
                siftDown(index);
            }
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
            for (int parentIndex = parentIndex(index); index > 0 && array[parentIndex] > array[index];
                    index = parentIndex, parentIndex = parentIndex(index)) {
                swap(parentIndex, index);
            }
        }

        private void siftDown(int index) {
            int minIndex = index;

            int leftChildIndex = leftChildIndex(index);
            if (leftChildIndex < array.length && array[leftChildIndex] < array[minIndex]) {
                minIndex = leftChildIndex;
            }

            int rightChildIndex = rightChildIndex(index);
            if (rightChildIndex < array.length && array[rightChildIndex] < array[minIndex]) {
                minIndex = rightChildIndex;
            }

            if (index != minIndex) {
                swap(index, minIndex);
                siftDown(minIndex);
            }
        }

        private void swap(int index1, int index2) {
            int value1 = array[index1];
            array[index1] = array[index2];
            array[index2] = value1;
            swaps.add(new Swap(index1, index2));
        }
    }

    static class Swap {
        int index1;
        int index2;

        Swap(int index1, int index2) {
            this.index1 = index1;
            this.index2 = index2;
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

    public static void main(String[] args) throws IOException {
        int[] array = readData();
        writeResponse(generateSwaps(array));
    }

    private static int[] readData() throws IOException {
        FastScanner in = new FastScanner();
        int n = in.nextInt();
        int[] data = new int[n];
        for (int i = 0; i < n; ++i) {
          data[i] = in.nextInt();
        }
        return data;
    }

    private static void writeResponse(List<Swap> swaps) {
        try (PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out))) {
            out.println(swaps.size());
            for (Swap swap : swaps) {
                out.println(swap.index1 + " " + swap.index2);
            }
        }
    }

    private static List<Swap> generateSwaps(int[] data) {
        Heap heap = new Heap(data);
        return heap.swaps;
    }
}
