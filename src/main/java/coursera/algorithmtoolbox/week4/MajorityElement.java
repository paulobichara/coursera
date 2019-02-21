package coursera.algorithmtoolbox.week4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MajorityElement {

    private static class Partitions {
        int start;
        int end;
    }

    static int getMajorityElementNaive(int[] array) {
        Set<Integer> distinct = Arrays.stream(array).boxed().collect(Collectors.toSet());

        long totalCount = 0;
        int majority = array.length / 2 + 1;
        for (Integer value : distinct) {
            long count = IntStream.of(array).filter(number -> number == value).count();

            totalCount += count;
            if (count >= majority) {
                return value;
            }

            if (totalCount >= majority) {
                break;
            }
        }

        return -1;
    }

    static int getMajorityElement(int[] array) {
        return getMajorityElement(array, 0, array.length);
    }

    static int getMajorityElement(int[] array, int left, int right) {
        if (left == right) {
            return -1;
        }

        if (left + 1 == right) {
            return array[left];
        }

        switchValues(array, left, (right / 2) + left - 1);

        Partitions partitions = partitionInThree(array, left, right);

        int totalMajority = (right / 2) + 1;
        int majorityLeft = (partitions.start - left) / 2 + 1;
        int majorityMiddle = partitions.end - partitions.start + 1;
        int majorityRight = (right - (partitions.end + 1)) / 2 + 1;

        if (majorityMiddle >= totalMajority) {
            return array[partitions.start];
        }

        if (majorityLeft + majorityRight < totalMajority) {
            return -1;
        }

        int element;
        if (majorityLeft >= majorityRight) {
            element = getMajorityElement(array, left, partitions.start);
        } else {
            element = getMajorityElement(array, partitions.end + 1, right);
        }

        if (countOccurrences(array, element) >= totalMajority) {
            return element;
        } else {
            return -1;
        }
    }

    private static int countOccurrences(int[] array, int value) {
        return (int) IntStream.of(array).filter(number -> number == value).count();
    }

    private static int partitionInTwo(int[] a, int left, int right, boolean invertEquals) {
        int pivot = a[left];
        int pivotIndex = left;

        for (int i = left + 1; i < right; i++) {
            if ((a[i] < pivot && !invertEquals) || (a[i] <= pivot && invertEquals)) {
                pivotIndex = pivotIndex + 1;
                switchValues(a, i, pivotIndex);
            }
        }
        switchValues(a, left, pivotIndex);
        return pivotIndex;
    }

    private static Partitions partitionInThree(int[] a, int left, int right) {
        Partitions partitions = new Partitions();
        partitions.start = partitionInTwo(a, left, right, false);
        partitions.end = partitionInTwo(a, partitions.start, right, true);
        return partitions;
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
        if (getMajorityElement(a, 0, a.length) != -1) {
            System.out.println(1);
        } else {
            System.out.println(0);
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

