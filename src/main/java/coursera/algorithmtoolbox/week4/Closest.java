package coursera.algorithmtoolbox.week4;

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
            if (Coordinate.X.equals(coordinate)) {
                return other.x == x ? Long.signum(y - other.y) : Long.signum(x - other.x);
            } else {
                return other.y == y ? Long.signum(x - other.x) : Long.signum(y - other.y);
            }
        }

        double getDistanceTo(Point other) {
            return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
        }
    }

    private enum Coordinate { X, Y }

    static class MergeSort {

        private Coordinate sortCoordinate;

        MergeSort(Coordinate sortCoordinate) {
            this.sortCoordinate = sortCoordinate;
        }

        void sortNonRecursive(Point[] array) {
            int arraySize;
            for (arraySize = 1; arraySize < array.length; arraySize = 2 * arraySize) {
                for (int left = 0, right; left < array.length - 1; left = right) {
                    right = Math.min(left + 2 * arraySize, array.length);
                    right = array.length - right > 0 && array.length - right < arraySize + 1 ? array.length : right;

                    if (right == array.length && right - left > arraySize + 1) {
                        mergeNonRecursive(array, left + arraySize, (left + arraySize + right) / 2 - 1, right);
                    }
                    mergeNonRecursive(array, left, left + arraySize - 1, right);
                }
            }
        }

        private void mergeNonRecursive(Point[] array, int leftBound, int middle, int rightBound) {
            Point[] leftSide = Arrays.copyOfRange(array, leftBound, middle + 1);
            Point[] rightSide = Arrays.copyOfRange(array, middle + 1, rightBound);

            int leftSideCount = 0, rightSideCount = 0;

            for (int i = leftBound; i < rightBound; i++) {
                if (leftSideCount < leftSide.length && rightSideCount < rightSide.length) {
                    if (leftSide[leftSideCount].compareTo(rightSide[rightSideCount], sortCoordinate) > 0) {
                        array[i] = rightSide[rightSideCount];
                        rightSideCount++;
                    } else {
                        array[i] = leftSide[leftSideCount];
                        leftSideCount++;
                    }
                } else if (leftSideCount < leftSide.length) {
                    array[i] = leftSide[leftSideCount];
                    leftSideCount++;
                } else {
                    array[i] = rightSide[rightSideCount];
                    rightSideCount++;
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

                this.leftIndex = leftBound;

                if (ReferenceBound.RIGHT.equals(reference)) {
                    this.basePointIndex = rightBound;
                    this.rightIndex = rightBound - 1 >= 0 ? rightBound - 1 : 0;
                } else {
                    this.basePointIndex = leftBound - 1;
                    this.rightIndex = rightBound;
                }
            }
        }

        enum ReferenceBound { RIGHT, LEFT }

        static int findBound(Configuration config, Point[] array) {
            Point basePoint = array[config.basePointIndex];
            int middleIndex = -1;
            while (config.leftIndex >= config.leftBound && config.rightIndex <= config.rightBound
                    && config.leftIndex + 1 < config.rightIndex) {
                middleIndex = ((config.rightIndex - config.leftIndex) / 2) + config.leftIndex;

                long currentDistance = Math.abs(basePoint.x - array[middleIndex].x);
                if (currentDistance == config.maxDistance) {
                    break;
                } if (currentDistance > config.maxDistance) {
                    if (ReferenceBound.RIGHT.equals(config.reference)) {
                        config.leftIndex = middleIndex + 1;
                    } else {
                        config.rightIndex = middleIndex - 1;
                    }
                } else {
                    if (ReferenceBound.RIGHT.equals(config.reference)) {
                        config.rightIndex = middleIndex - 1;
                    } else {
                        config.leftIndex = middleIndex + 1;
                    }
                }
            }

            if (config.leftIndex == config.rightIndex) {
                return checkLastElement(array, config, basePoint);
            }

            if (config.leftIndex + 1 == config.rightIndex) {
                return checkLastTwoElements(config, array);
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

        private static int checkLastElement(Point[] array, Configuration config, Point basePoint) {
            if (Math.abs(basePoint.x - array[config.leftIndex].x) <= config.maxDistance) {
                return config.leftIndex;
            } else {
                if (ReferenceBound.RIGHT.equals(config.reference)) {
                    return config.leftIndex < config.leftBound ? config.leftIndex + 1 : config.leftBound;
                } else {
                    return config.rightIndex < config.rightBound ? config.rightIndex - 1 : config.rightBound;
                }
            }
        }

        private static int checkLastTwoElements(Configuration config, Point[] array) {
            long distanceLeft = Math.abs(array[config.leftIndex].x - array[config.basePointIndex].x);
            long distanceRight = Math.abs(array[config.rightIndex].x - array[config.basePointIndex].x);

            int bound;
            if (ReferenceBound.RIGHT.equals(config.reference)) {
                if (distanceLeft <= config.maxDistance) {
                    bound = config.leftIndex;
                } else if (distanceRight <= config.maxDistance) {
                    bound = config.rightIndex;
                } else {
                    bound = config.rightIndex < config.rightBound ? config.rightIndex + 1 : config.basePointIndex;
                }
            } else if (distanceRight <= config.maxDistance) {
                bound = config.rightIndex;
            } else if (distanceLeft <= config.maxDistance) {
                bound = config.leftIndex;
            } else {
                bound = config.leftIndex > config.leftBound ? config.leftIndex - 1 : config.basePointIndex;
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
            double minDistanceRight = minimalDistance(points, middleIndex + 1, rightIndex);
            double minDistance = Math.min(minDistanceLeft, minDistanceRight);

            return findRealMinimumDistance(points, leftIndex, middleIndex, rightIndex, minDistance);
        }

        private static double findRealMinimumDistance(Point[] points, int leftIndex, int middleIndex, int rightIndex,
                                                      double minDistanceByX) {
            double minDistance = minDistanceByX;

            int leftBound = BinarySearch.findBound(new BinarySearch.Configuration(BinarySearch.ReferenceBound.RIGHT,
                    leftIndex, middleIndex, (long) minDistance), points);

            int rightBound = BinarySearch.findBound(new BinarySearch.Configuration(BinarySearch.ReferenceBound.LEFT,
                    middleIndex + 1, rightIndex, (long) minDistance), points);

            Point[] sortedByY = Arrays.copyOfRange(points, leftBound, rightBound + 1);
            MergeSort mergeSort = new MergeSort(Coordinate.Y);
            mergeSort.sortNonRecursive(sortedByY);

            for (int i = 0; i < sortedByY.length; i++) {
                for (int j = i - 7 < 0 ? 0 : i - 7; j < sortedByY.length && Math.abs(i - j) <= 7; j++) {
                    if (i != j) {
                        minDistance = Math.min(minDistance, sortedByY[i].getDistanceTo(sortedByY[j]));
                    }
                }
            }

            return minDistance;
        }
    }

    static double minimalDistance(Point[] points) {
        if (points.length < 2) {
            return 0;
        }

        MergeSort sorterByX = new MergeSort(Coordinate.X);
        sorterByX.sortNonRecursive(points);

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
        System.out.println("coursera.algorithmtoolbox.week4.Closest points are: (" + first.x + ", " + first.y + ") and (" + second.x + ", " + second.y + ")");
        return minDistance;
    }

    public static void main(String[] args) {
        reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(System.out);

        int pointsQty = nextInt();

        Point[] points = new Point[pointsQty];

        for (int i = 0; i < pointsQty; i++) {
            points[i] = new Point(nextInt(), nextInt());

        }
        System.out.println(minimalDistance(points));
        writer.close();
    }

    private static BufferedReader reader;
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
