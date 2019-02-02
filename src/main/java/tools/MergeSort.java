package tools;

import java.util.ArrayList;
import java.util.List;

public class MergeSort<T extends Comparable<T>> {

    void sort(T[] array, T[] sorted, int left, int right) {
        if (right <= left + 1) {
            sorted[left] = array[left];
            return;
        }

        int middle = (left + right) / 2;
        sort(array, sorted, left, middle);
        sort(array, sorted, middle, right);
        merge(sorted, left, middle, right);
    }

    private void merge(T[] array, int leftBound, int middle, int rightBound) {
        int totalElements = rightBound - leftBound;
        List<T> original = new ArrayList<>(totalElements);

        System.arraycopy(array, leftBound, original, 0, rightBound - leftBound);

        int nextLeftIndex = leftBound;
        int nextRightIndex = middle;

        for (int i = 0; i < totalElements; i++) {
            if (nextLeftIndex < middle) {
                T nextLeft = original.get(nextLeftIndex - leftBound);
                T nextRight;
                if (nextRightIndex < rightBound
                        && ((nextRight = original.get(nextRightIndex - leftBound)).compareTo(nextLeft) < 0)) {
                    int realIndex = i + leftBound;
                    array[realIndex] = nextRight;
                    nextRightIndex++;
                } else {
                    array[i + leftBound] = nextLeft;
                    nextLeftIndex++;
                }
            } else {
                array[i + leftBound] = original.get(nextRightIndex - leftBound);
                nextRightIndex++;
            }
        }
    }

}
