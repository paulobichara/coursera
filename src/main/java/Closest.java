import java.io.*;
import java.util.*;

public class Closest {

    static class Point {
        long x, y;


        Point(long x, long y) {
            this.x = x;
            this.y = y;
        }

        int compareTo(Point other, Coordinate coordinate) {
            if (Coordinate.Y.equals(coordinate)) {
                return other.y == y ? Long.signum(x - other.x) : Long.signum(y - other.y);
            } else {
                return other.x == x ? Long.signum(y - other.y) : Long.signum(x - other.x);
            }
        }

        double getDistanceTo(Point other) {
            return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
        }
    }

    private enum Coordinate { X, Y }

    static class QuickSort {

        private static class Partitions {
            int start;
            int end;
        }

        private Coordinate coordinate;

        QuickSort(Coordinate coordinate) {
            this.coordinate = coordinate;
        }

        void sort(Point[] array) {
            if (array.length > 1) {
                sort(array, 0, array.length);
            }
        }

        private void sort(Point[] array, int left, int right) {
            while (left + 1 < right) {
                switchValues(array, left, choosePivotRange(array, left, right));

                Partitions partitions = partitionInThree(array, left, right);

                sort(array, left, partitions.start);
                left = partitions.end + 1;
            }
        }

        private int choosePivotRange(Point[] array, int left, int right) {
            int middleIndex = (right - 1 - left) / 2 + left;
            Point[] candidates = new Point[] { array[left], array[middleIndex], array[right - 1] };

            int bestIndex = left;

            Point best = findBestPivotCandidate(candidates);
            if (best.compareTo(candidates[1], coordinate) == 0) {
                bestIndex = middleIndex;
            } else if (best.compareTo(candidates[2], coordinate) == 0) {
                bestIndex = right - 1;
            }

            return bestIndex;
        }

        private Point findBestPivotCandidate(Point[] candidates) {
            Point best = candidates[0];
            if (candidates[0].compareTo(candidates[1], coordinate) > 0) {
                if (candidates[0].compareTo(candidates[2], coordinate) > 0) {
                    if (candidates[1].compareTo(candidates[2], coordinate) < 0) {
                        best = candidates[2];
                    } else {
                        best = candidates[1];
                    }
                }
            } else if (candidates[1].compareTo(candidates[2], coordinate) > 0) {
                if (candidates[0].compareTo(candidates[2], coordinate) < 0) {
                    best = candidates[2];
                }
            } else {
                best = candidates[1];
            }
            return best;
        }

        private Partitions partitionInThree(Point[] array, int left, int right) {
            Partitions partitions = new Partitions();
            partitions.start = partitionInTwo(array, left, right, false);
            partitions.end = partitionInTwo(array, partitions.start, right, true);
            return partitions;
        }

        private int partitionInTwo(Point[] array, int left, int right,
                                   boolean invertEquals) {
            Point pivot = array[left];
            int pivotIndex = left;

            for (int i = left + 1; i < right; i++) {
                Point current = array[i];
                if ((current.compareTo(pivot, coordinate) < 0 && !invertEquals)
                        || (current.compareTo(pivot, coordinate) <= 0 && invertEquals)) {
                    pivotIndex = pivotIndex + 1;
                    switchValues(array, i, pivotIndex);
                }
            }
            switchValues(array, left, pivotIndex);
            return pivotIndex;
        }

        private void switchValues(Point[] array, int from, int to) {
            Point oldValue = array[from];
            array[from] = array[to];
            array[to] = oldValue;
        }
    }

    static class MergeSort {

        private Coordinate sortCoordinate;

        MergeSort(Coordinate sortCoordinate) {
            this.sortCoordinate = sortCoordinate;
        }

        void sort(Point[] array, Point[] sorted, int left, int right) {
            if (right <= left + 1) {
                sorted[left] = array[left];
                return;
            }

            int middle = (left + right) / 2;
            sort(array, sorted, left, middle);
            sort(array, sorted, middle, right);
            merge(sorted, left, middle, right);
        }

        private void merge(Point[] array, int leftBound, int middle, int rightBound) {
            int totalElements = rightBound - leftBound;
            Point[] original = new Point[totalElements];

            System.arraycopy(array, leftBound, original, 0, rightBound - leftBound);

            int nextLeftIndex = leftBound;
            int nextRightIndex = middle;

            for (int i = 0; i < totalElements; i++) {
                if (nextLeftIndex < middle) {
                    Point nextLeft = original[nextLeftIndex - leftBound];
                    Point nextRight;
                    if (nextRightIndex < rightBound && ((nextRight = original[nextRightIndex - leftBound])
                            .compareTo(nextLeft,sortCoordinate) < 0)) {
                        int realIndex = i + leftBound;
                        array[realIndex] = nextRight;
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
        }

    }


    static class BinarySearch {

        private static class Configuration {
            private ReferenceBound reference;

            private int leftBound;
            private int rightBound;

            private int leftIndex;
            private int rightIndex;
            private int basePointIndex;

            private long maxDistance;

            Configuration(ReferenceBound reference, int leftBound, int rightBound, long maxDistance) {
                this.reference = reference;
                this.leftBound = leftBound;
                this.rightBound = rightBound;
                this.maxDistance = maxDistance;

                if (ReferenceBound.RIGHT.equals(reference)) {
                    this.leftIndex = leftBound;
                    this.basePointIndex = rightBound;
                    this.rightIndex = rightBound - 1 >= 0 ? rightBound - 1 : 0;
                } else {
                    this.leftIndex = leftBound + 1;
                    this.basePointIndex = leftBound;
                    this.rightIndex = rightBound;
                }
            }
        }

        enum ReferenceBound { RIGHT, LEFT }

        static int findBound(Configuration config, Point[] array) {
            Point basePoint = array[config.basePointIndex];

            if (config.leftBound == config.rightBound) {
                if (Math.abs(basePoint.x - array[config.leftBound].x) < config.maxDistance) {
                    return config.leftBound;
                } else {
                    return config.basePointIndex;
                }
            }

            int middleIndex = -1;
            while (config.leftIndex >= config.leftBound && config.rightIndex <= config.rightBound
                    && config.leftIndex + 1 < config.rightIndex) {
                middleIndex = ((config.rightIndex - config.leftIndex) / 2) + config.leftIndex;

                long currentDistance = Math.abs(basePoint.x - array[middleIndex].x);
                if (currentDistance == config.maxDistance) {
                    break;
                } if (currentDistance > config.maxDistance) {
                    config.rightIndex = middleIndex;
                } else {
                    config.leftIndex = middleIndex;
                }
            }

            if (middleIndex == -1 && config.leftIndex + 1 > config.rightIndex) {
                return config.leftIndex;
            }

            if (config.leftIndex + 1 == config.rightIndex) {
                return findBoundLastTwo(config, array);
            }

            return findLastEquallyDistant(config, array, middleIndex);
        }

        private static int findLastEquallyDistant(Configuration config, Point[] array, int middleIndex) {
            Point basePoint = array[config.basePointIndex];
            int bound = middleIndex;
            if (ReferenceBound.LEFT.equals(config.reference)) {
                for (int index = middleIndex + 1; index <= array.length - 1
                        && ((Math.abs(basePoint.x - array[index].x)) <= config.maxDistance); index++) {
                    bound = index;
                }
            } else {
                for (int index = middleIndex - 1; index >= 0
                        && ((Math.abs(basePoint.x - array[index].x)) <= config.maxDistance); index--) {
                    bound = index;
                }
            }
            return bound;
        }

        private static int findBoundLastTwo(Configuration config, Point[] array) {
            long distanceLeft = Math.abs(array[config.leftIndex].x - array[config.basePointIndex].x);
            long distanceRight = Math.abs(array[config.rightIndex].x - array[config.basePointIndex].x);

            int bound;
            if (ReferenceBound.RIGHT.equals(config.reference)) {
                if (distanceLeft <= config.maxDistance) {
                    bound = config.leftIndex;
                } else if (distanceRight <= config.maxDistance) {
                    bound = config.rightIndex;
                } else {
                    bound = config.basePointIndex;
                }
            } else if (distanceRight <= config.maxDistance) {
                bound = config.rightIndex;
            } else if (distanceLeft <= config.maxDistance) {
                bound = config.leftIndex;
            } else {
                bound = config.basePointIndex;
            }
            return bound;
        }
    }

    static class DistanceCalculator {

        static double minimalDistance(Point[] points) {
            return minimalDistance(points, 0, points.length - 1);
        }

        private static double minimalDistance(Point[] points, int leftIndex, int rightIndex) {
            if (leftIndex >= rightIndex) {
                return Integer.MAX_VALUE;
            }

            if (leftIndex + 1 == rightIndex) {
                return points[leftIndex].getDistanceTo(points[rightIndex]);
            }

            int middleIndex = ((rightIndex - leftIndex) / 2) + leftIndex;
            double minDistanceLeft = minimalDistance(points, leftIndex, middleIndex);
            double minDistanceRight = minimalDistance(points, middleIndex, rightIndex);

            double minDistance = Math.min(minDistanceLeft, minDistanceRight);
            BinarySearch.Configuration config = new BinarySearch.Configuration(BinarySearch.ReferenceBound.RIGHT, 0, middleIndex, (long) minDistance);
            int leftBound = BinarySearch.findBound(config, points);
            config = new BinarySearch.Configuration(BinarySearch.ReferenceBound.LEFT, middleIndex, rightIndex, (long) minDistance);
            int rightBound = BinarySearch.findBound(config, points);

            Point[] sortedByY = new Point[points.length];

            MergeSort sorterByY = new MergeSort(Coordinate.Y);
            sorterByY.sort(points, sortedByY, leftBound, rightBound + 1);

            for (int i = leftBound; i <= rightBound; i++) {
                for (int j = leftBound; j <= rightBound && Math.abs(i - j) <= 7; j++) {
                    if (i != j) {
                        minDistance = Math.min(minDistance, sortedByY[i].getDistanceTo(sortedByY[j]));
                    }
                }
            }

            return minDistance;
        }
    }

    static double minimalDistance(Point[] points) {
        QuickSort sortByX = new QuickSort(Coordinate.X);
        sortByX.sort(points);

        return DistanceCalculator.minimalDistance(points);
    }

    static double naiveMinimalDistance(Point[] points) {
        if (points.length < 2) {
            return 0;
        }

        Point first = null, second = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (i != j && points[i].getDistanceTo(points[j]) < minDistance) {
                    first = points[i];
                    second = points[j];
                    minDistance = points[i].getDistanceTo(points[j]);
                }
            }
        }
//        System.out.println("Closest points are: (" + first.x + ", " + first.y + ") and (" + second.x + ", " + second.y + ")");
        return minDistance;
    }

    public static void main(String[] args) {
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = new PrintWriter(System.out);

        int pointsQty = nextInt();

        Point[] points = new Point[pointsQty];
        int[] x = new int[pointsQty];
        int[] y = new int[pointsQty];

        for (int i = 0; i < pointsQty; i++) {
            x[i] = nextInt();
            y[i] = nextInt();
            points[i] = new Point(x[i], y[i]);

        }
        System.out.println(minimalDistance(points));
        writer.close();
    }

    private static BufferedReader reader;
    private static PrintWriter writer;
    private static StringTokenizer tok = new StringTokenizer("");


    private static String next() {
        while (!tok.hasMoreTokens()) {
            String w = null;
            try {
                w = reader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (w == null)
                return null;
            tok = new StringTokenizer(w);
        }
        return tok.nextToken();
    }

    private static int nextInt() {
        return Integer.parseInt(next());
    }
}
