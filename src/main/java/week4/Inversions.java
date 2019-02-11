package week4;

import java.util.Scanner;

public class Inversions {

    static long getNumberOfInversions(int[] original) {
        return getNumberOfInversions(original, new int[original.length], 0, original.length);
    }

    static long getNumberOfInversions(int[] array, int[] sorted, int left, int right) {
        if (right <= left + 1) {
            sorted[left] = array[left];
            return 0;
        }

        int middle = (left + right) / 2;
        long numberOfInversions = getNumberOfInversions(array, sorted, left, middle);
        numberOfInversions += getNumberOfInversions(array, sorted, middle, right);
        numberOfInversions += mergeAndCountInversions(sorted, left, middle, right);
        return numberOfInversions;
    }

    private static long mergeAndCountInversions(int[] array, int leftBound, int middle, int rightBound) {
        int totalElements = rightBound - leftBound;
        int[] original = new int[totalElements];
        System.arraycopy(array, leftBound, original, 0, rightBound - leftBound);

        int nextLeftIndex = leftBound;
        int nextRightIndex = middle;
        long numberOfInversions = 0;

        for (int i = 0; i < totalElements; i++) {
            if (nextLeftIndex < middle) {

                int nextLeft = original[nextLeftIndex - leftBound];
                int nextRight;

                if (nextRightIndex < rightBound && ((nextRight = original[nextRightIndex - leftBound]) < nextLeft)) {
                    int realIndex = i + leftBound;
                    array[realIndex] = nextRight;
                    numberOfInversions = numberOfInversions + Math.abs(realIndex - nextRightIndex);
                    nextRightIndex++;
                } else {
                    array[i + leftBound] = nextLeft;
                    nextLeftIndex++;
                }

            } else {
                array[i + leftBound] = original[nextRightIndex - leftBound];
                nextRightIndex++;
            }
        }

        return numberOfInversions;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int[] b = new int[n];
        System.out.println(getNumberOfInversions(a, b, 0, a.length));
    }
}

