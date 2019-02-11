package week4;

import java.util.Scanner;

public class PointsAndSegments {

    /**
     * Class used to get the payoffs for the provided input data (quick sort)
     */
    static class QuickSort {

        private static class Partitions {
            int start;
            int end;
        }

        static void sort(int[] array) {
            if (array.length > 1) {
                sort(array, 0, array.length);
            }
        }

        private static void sort(int[] array, int left, int right) {
            while (left + 1 < right) {
                switchValues(array, left, choosePivotRange(array, left, right));

                Partitions partitions = partitionInThree(array, left, right);

                sort(array, left, partitions.start);
                left = partitions.end + 1;
            }
        }

        private static int choosePivotRange(int[] array, int left, int right) {
            int[] candidates = new int[] { array[left], array[(right / 2) - 1], array[right - 1] };

            int bestIndex = left;

            int best = findBestPivotCandidate(candidates);
            if (best == candidates[1]) {
                bestIndex = (right / 2) - 1;
            } else if (best == candidates[2]) {
                bestIndex = right - 1;
            }

            return bestIndex;
        }

        private static int findBestPivotCandidate(int[] candidates) {
            int best = candidates[0];
            if (candidates[0] > candidates[1]) {
                if (candidates[0] > candidates[2]) {
                    if (candidates[1] < candidates[2]) {
                        best = candidates[2];
                    } else {
                        best = candidates[1];
                    }
                }
            } else if (candidates[1] > candidates[2]) {
                if (candidates[0] < candidates[2]) {
                    best = candidates[2];
                }
            } else {
                best = candidates[1];
            }
            return best;
        }

        private static Partitions partitionInThree(int[] array, int left, int right) {
            Partitions partitions = new Partitions();
            partitions.start = partitionInTwo(array, left, right, false);
            partitions.end = partitionInTwo(array, partitions.start, right, true);
            return partitions;
        }

        private static int partitionInTwo(int[] array, int left, int right, boolean invertEquals) {
            int pivot = array[left];
            int pivotIndex = left;

            for (int i = left + 1; i < right; i++) {
                int current = array[i];
                if ((current < pivot  && !invertEquals) || (current <= pivot && invertEquals)) {
                    pivotIndex = pivotIndex + 1;
                    switchValues(array, i, pivotIndex);
                }
            }
            switchValues(array, left, pivotIndex);
            return pivotIndex;
        }

        private static void switchValues(int[] array, int from, int to) {
            int oldValue = array[from];
            array[from] = array[to];
            array[to] = oldValue;
        }
    }

    private static class BinarySearch {

        int qtyPointsBeforeOrEqual;
        int qtyPointsAfterOrEqual;

        void countPointsBeforeAndAfter(int[] array, int key) {
            int middleIndex = -1, middle = 0;

            this.qtyPointsBeforeOrEqual = 0;
            this.qtyPointsAfterOrEqual = 0;

            for (int leftIndex = 0, rightIndex = array.length - 1; leftIndex <= rightIndex;) {
                middleIndex = ((rightIndex - leftIndex) / 2) + leftIndex;
                middle = array[middleIndex];
                if (key < middle) {
                    rightIndex = middleIndex - 1;
                } else if (key > middle) {
                    leftIndex = middleIndex + 1;
                } else {
                    break;
                }
            }

            if (middleIndex != -1 && middle == key) {
                int firstEqual = middleIndex, lastEqual = middleIndex;
                for (int index = middleIndex - 1; index >= 0 && array[index] == middle; index--) {
                    firstEqual = index;
                }
                for (int index = middleIndex + 1; index <= array.length - 1 && array[index] == middle; index++) {
                    lastEqual = index;
                }
                this.qtyPointsBeforeOrEqual = lastEqual + 1;
                this.qtyPointsAfterOrEqual = array.length - firstEqual;
            } else {
                if (key < middle) {
                    this.qtyPointsBeforeOrEqual = middleIndex;
                } else {
                    this.qtyPointsBeforeOrEqual = middleIndex + 1;
                }
                this.qtyPointsAfterOrEqual = array.length - this.qtyPointsBeforeOrEqual;
            }
        }
    }

    static int[] fastCountSegments(int[] starts, int[] ends, int[] points) {
        QuickSort.sort(starts);
        QuickSort.sort(ends);

        BinarySearch search = new BinarySearch();

        int[] payoffs = new int[points.length];
        for (int pointIndex = 0; pointIndex < points.length; pointIndex++) {
            search.countPointsBeforeAndAfter(starts, points[pointIndex]);
            int qtyStartsBefore = search.qtyPointsBeforeOrEqual;
            search.countPointsBeforeAndAfter(ends, points[pointIndex]);
            int qtyEndsAfter = search.qtyPointsAfterOrEqual;

            int payoff = qtyStartsBefore + qtyEndsAfter - starts.length;
            payoffs[pointIndex] = payoff <= 0 ? 0 : payoff;
        }

        return payoffs;
    }

    static int[] naiveCountSegments(int[] starts, int[] ends, int[] points) {
        int[] cnt = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < starts.length; j++) {
                if (starts[j] <= points[i] && points[i] <= ends[j]) {
                    cnt[i]++;
                }
            }
        }
        return cnt;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n, m;
        n = scanner.nextInt();
        m = scanner.nextInt();
        int[] starts = new int[n];
        int[] ends = new int[n];
        int[] points = new int[m];
        for (int i = 0; i < n; i++) {
            starts[i] = scanner.nextInt();
            ends[i] = scanner.nextInt();
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //use fastCountSegments
        int[] cnt = fastCountSegments(starts, ends, points);
        for (int x : cnt) {
            System.out.print(x + " ");
        }
    }
}

