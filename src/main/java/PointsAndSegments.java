import java.util.Scanner;

public class PointsAndSegments {

    static class Range implements Comparable<Range> {
        private int start, end;

        Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public int compareTo(Range other) {
            if (other == null) {
                throw new NullPointerException("Other value is null");
            }

            return this.start > other.start || (this.start == other.start && this.end > other.end) ? 1
                    : this.start < other.start || this.end < other.end ? -1 : 0;
        }
    }

    /**
     * Class used to get the sorted indexes order of the points without modifying the original array (merge sort)
     */
    static class PointsSorter {
        static void sort(int[] array, int[] sortedIndexes, int left, int right) {
            if (right <= left + 1) {
                sortedIndexes[left] = left;
                return;
            }

            int middle = (left + right) / 2;
            sort(array, sortedIndexes, left, middle);
            sort(array, sortedIndexes, middle, right);
            merge(array, sortedIndexes, left, middle, right);
        }

        private static void merge(int[] values, int[] sortedIndexes, int leftBound, int middle, int rightBound) {
            int totalElements = rightBound - leftBound;

            int[] original = new int[totalElements];
            System.arraycopy(sortedIndexes, leftBound, original, 0, rightBound - leftBound);

            int nextLeftIndex = leftBound;
            int nextRightIndex = middle;

            for (int i = 0; i < totalElements; i++) {
                if (nextLeftIndex < middle) {
                    int nextLeft = values[original[nextLeftIndex - leftBound]];
                    if (nextRightIndex < rightBound
                            && (values[original[nextRightIndex - leftBound]] < nextLeft)) {
                        sortedIndexes[i + leftBound] = original[nextRightIndex - leftBound];
                        nextRightIndex++;
                    } else {
                        sortedIndexes[i + leftBound] = original[nextLeftIndex - leftBound];
                        nextLeftIndex++;
                    }
                } else {
                    sortedIndexes[i + leftBound] = original[nextRightIndex - leftBound];
                    nextRightIndex++;
                }
            }
        }
    }

    /**
     * Class used to get the payoffs for the provided input data (quick sort)
     */
    private static class RangesSorter {

        private static class Partitions {
            int start;
            int end;
        }

        static void sortRanges(Range[] ranges) {
            if (ranges.length > 1) {
                sortRanges(ranges, 0, ranges.length);
            }
        }

        private static void sortRanges(Range[] ranges, int left, int right) {
            if (left + 1 >= right) {
                return;
            }

            switchValues(ranges, left, (right / 2) + left - 1);

            Partitions partitions = partitionInThree(ranges, left, right);
            sortRanges(ranges, left, partitions.start);
            sortRanges(ranges, partitions.end + 1, right);
        }

        private static Partitions partitionInThree(Range[] a, int left, int right) {
            Partitions partitions = new Partitions();
            partitions.start = partitionInTwo(a, left, right, false);
            partitions.end = partitionInTwo(a, partitions.start, right, true);
            return partitions;
        }

        private static int partitionInTwo(Range[] ranges, int left, int right, boolean invertEquals) {
            Range pivot = ranges[left];
            int pivotIndex = left;

            for (int i = left + 1; i < right; i++) {
                Range currentRange = ranges[i];
                if ((currentRange.compareTo(pivot) < 0  && !invertEquals) || (currentRange.compareTo(pivot) <= 0 && invertEquals)) {
                    pivotIndex = pivotIndex + 1;
                    switchValues(ranges, i, pivotIndex);
                }
            }
            switchValues(ranges, left, pivotIndex);
            return pivotIndex;
        }


        private static void switchValues(Range[] array, int from, int to) {
            Range oldValue = array[from];
            array[from] = array[to];
            array[to] = oldValue;
        }
    }

    static int[] fastCountSegments(Range[] ranges, int[] points) {
        int[] sortedPointsIndexes = new int[points.length];
        PointsSorter.sort(points, sortedPointsIndexes, 0, points.length);

        RangesSorter.sortRanges(ranges);
        return calculatePayoffs(points, sortedPointsIndexes, ranges);
    }

    private static  int[] calculatePayoffs(int[] points, int[] sortedIndexes, Range[] ranges) {
        int[] payoffs = new int[points.length];
        int firstReachable = 0;

        for (int rangeIndex = 0; rangeIndex < ranges.length && firstReachable < points.length; rangeIndex++) {
            Range currentRange = ranges[rangeIndex];

            for (int i = firstReachable; (i < points.length
                    && points[sortedIndexes[i]] < currentRange.start); i++) {
                firstReachable++;
            }

            for (int i = firstReachable; i < points.length && points[sortedIndexes[i]] <= currentRange.end; i++) {
                payoffs[sortedIndexes[firstReachable]]++;
            }
        }

        return  payoffs;
    }

    static int[] naiveCountSegments(Range[] ranges, int[] points) {
        int[] cnt = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < ranges.length; j++) {
                if (ranges[j].start <= points[i] && points[i] <= ranges[j].end) {
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
        Range[] ranges = new Range[n];
        int[] points = new int[m];
        for (int i = 0; i < n; i++) {
            ranges[i] = new Range(scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //use fastCountSegments
        int[] cnt = fastCountSegments(ranges, points);
        for (int x : cnt) {
            System.out.print(x + " ");
        }
    }
}

