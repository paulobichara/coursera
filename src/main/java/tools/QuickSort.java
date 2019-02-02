package tools;

class QuickSort<T extends Comparable<T>> {

    private static class Partitions {
        int start;
        int end;
    }

    void sort(T[] array, int left, int right) {
        if (left == right || left == right - 1) {
            return;
        }

        switchValues(array, left, (right / 2) + left - 1);

        Partitions partitions = partitionInThree(array, left, right);
        sort(array, left, partitions.start);
        sort(array, partitions.end + 1, right);
    }

    private int partitionInTwo(T[] array, int left, int right, boolean invertEquals) {
        T pivot = array[left];
        int pivotIndex = left;

        for (int i = left + 1; i < right; i++) {
            if ((array[i].compareTo(pivot) < 0  && !invertEquals) || (array[i].compareTo(pivot) <= 0 && invertEquals)) {
                pivotIndex = pivotIndex + 1;
                switchValues(array, i, pivotIndex);
            }
        }
        switchValues(array, left, pivotIndex);
        return pivotIndex;
    }

    private Partitions partitionInThree(T[] a, int left, int right) {
        Partitions partitions = new Partitions();
        partitions.start = partitionInTwo(a, left, right, false);
        partitions.end = partitionInTwo(a, partitions.start, right, true);
        return partitions;
    }

    private void switchValues(T[] array, int from, int to) {
        T oldValue = array[from];
        array[from] = array[to];
        array[to] = oldValue;
    }
}
