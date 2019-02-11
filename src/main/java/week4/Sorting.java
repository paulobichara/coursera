package week4;

import java.io.*;
import java.util.*;

public class Sorting {
    private static Random random = new Random();

    private static class Partitions {
        int start;
        int end;
    }

    static void randomizedQuickSort(int[] a) {
        randomizedQuickSort(a, 0, a.length - 1);
    }

    private static void randomizedQuickSort(int[] a, int l, int r) {
        while (l < r) {
            int k = random.nextInt(r - l + 1) + l;
            switchValues(a, l, k);

            Partitions partitions = partitionInThree(a, l, r + 1);

            randomizedQuickSort(a, l, partitions.start);
            l = partitions.end + 1;
        }
    }

    private static Partitions partitionInThree(int[] a, int left, int right) {
        Partitions partitions = new Partitions();
        partitions.start = partitionInTwo(a, left, right, false);
        partitions.end = partitionInTwo(a, partitions.start, right, true);
        return partitions;
    }

    private static int partitionInTwo(int[] a, int left, int right, boolean invertEquals) {
        int pivot = a[left];
        int pivotIndex = left;

        for (int i = left + 1; i < right; i++) {
            if (invertEquals) {
                if (a[i] <= pivot) {
                    pivotIndex++;
                    switchValues(a, i, pivotIndex);
                }
            } else if (a[i] < pivot) {
                pivotIndex++;
                switchValues(a, i, pivotIndex);
            }
        }
        switchValues(a, left, pivotIndex);
        return pivotIndex;
    }

    private static void switchValues(int[] a, int from, int to) {
        int oldValue = a[from];
        a[from] = a[to];
        a[to] = oldValue;
    }

    public static void main(String[] args) {
        FastScanner scanner = new FastScanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        randomizedQuickSort(a, 0, n - 1);
        for (int i = 0; i < n; i++) {
            System.out.print(a[i] + " ");
        }
    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner(InputStream stream) {
            try {
                br = new BufferedReader(new InputStreamReader(stream));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }
}

