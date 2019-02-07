import org.junit.Assert;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class PointsAndSegmentsTest {

    private static final String ERROR_MESSAGE = "Wrong Answer for: \n";

    private static final int ARRAY_MAX_SIZE = 50_000;
    private static final int MAX_NUMBER = 100_000_000;

    private static final int STRESS_ARRAY_MAX_SIZE = 100;
    private static final int STRESS_MAX_NUMBER = 1000;

    private static final Random RANDOM = new SecureRandom();

    @Test
    public void testQuickSortSamples() {
        testQuickSortSample(new int[]{ 0 });
        testQuickSortSample(new int[] { 1, 2 });
        testQuickSortSample(new int[]{ 2, 1 });
        testQuickSortSample(new int[]{ 1, 2, 3 });
        testQuickSortSample(new int[]{ 1, 3, 2 });
        testQuickSortSample(new int[]{ 2, 1, 3 });
        testQuickSortSample(new int[]{ 2, 3, 1 });
        testQuickSortSample(new int[]{ 3, 2, 1 });
        testQuickSortSample(new int[]{ 1, 2, 3, 4 });
        testQuickSortSample(new int[]{ 1, 3, 2, 4 });
        testQuickSortSample(new int[]{ 2, 1, 3, 4 });
        testQuickSortSample(new int[]{ 1, 2, 4, 3 });
        testQuickSortSample(new int[]{ 3, 2, 1, 4 });
        testQuickSortSample(new int[]{ 4, 3, 2, 1 });
        testQuickSortSample(new int[]{ 2, 3, 9, 2, 9 });
    }

    @Test
    public void stressTestQuickSort() {
        long startTime = System.nanoTime();
        double duration = 0;
        while (duration < 60) {
            int[] array = RANDOM.ints(RANDOM.nextInt(STRESS_ARRAY_MAX_SIZE), -STRESS_MAX_NUMBER, STRESS_MAX_NUMBER).toArray();
            PointsAndSegments.QuickSort.sort(array);

            int[] expected = Arrays.copyOf(array, array.length);
            Arrays.sort(expected);

            Assert.assertArrayEquals(ERROR_MESSAGE, expected, array);
            duration = (System.nanoTime() - startTime) / 1_000_000_000.0;
        }
    }

    @Test
    public void testEdgeCases() {
        testArrays(new int[] { -5, 0, 3, 8 }, new int[] { -3, 5, 4, 8 }, new int[] { -6, 10, -5, 0, 3, -3, 5, 4, 8 });
        testArrays(new int[] { 0, 0, 0, 0 }, new int[] { 3, 3, 3, 3 }, new int[] { -1, 2, 0, 3, 4 });
        testArrays(new int[] { 0, 2, 4 }, new int[] { 1, 3, 5 }, new int[] { 0, 2, 4, 1, 3, 5, -1, 6 });
        testArrays(new int[] { 0, 4, 8 }, new int[] { 6, 10, 14 }, new int[] { 0, 4, 8, 6, 10, 14, 5, 9, 16, -10 });
        testArrays(new int[] { }, new int[] { }, new int[] { 1 });
        testArrays(new int[] { }, new int[] { }, new int[] { });
        testArrays(new int[] { 0 }, new int[] { 1 }, new int[] { });
    }

    @Test
    public void stressTestBigInput() {
        long startTime = System.nanoTime();
        double duration = 0;
        while (duration < 60) {
            int[] points = RANDOM.ints(ARRAY_MAX_SIZE, -MAX_NUMBER, MAX_NUMBER).toArray();
            int[] starts = RANDOM.ints(ARRAY_MAX_SIZE, -MAX_NUMBER, MAX_NUMBER).toArray();

            int[] ends = new int[starts.length];
            for (int i = 0; i < starts.length; i++) {
                ends[i] = RANDOM.ints(1, starts[i], MAX_NUMBER).toArray()[0];
            }
            testArrays(starts, ends, points);
            duration = (System.nanoTime() - startTime) / 1_000_000_000.0;
        }
    }

    @Test
    public void testSamples() {
        testArrays(new int[] { 0, 7 }, new int[] { 5, 10 }, new int[] { 1, 6, 11 });
        testArrays(new int[] { 0, 7 }, new int[] { 5, 10 }, new int[] { 1, 6, 6, 11 });
        testArrays(new int[] { 0, 3 }, new int[] { 5, 5 }, new int[] { 1, 5, 6, 11 });
        testArrays(new int[] { -10 }, new int[] { 10 }, new int[] { -100, 100, 0 });
        testArrays(new int[] { 0, -3, 7 }, new int[] { 5, 2, 10 }, new int[] { 1, 6 });
        testArrays(new int[] { 2, 3, 5, 7 }, new int[] { 3, 5, 5, 7 }, new int[] { 4, 3, 2, 7 });
        testArrays(new int[] { 4, 6, 7, 9 }, new int[] { 4, 5, 5, 8 }, new int[] { 8, 6, 0 });
        testArrays(new int[] { 1, 1 }, new int[] { 2, 2 }, new int[] { 8, 6, 0 });
        testArrays(new int[] { 1 }, new int[] { 2 }, new int[] { 1, 2, 3 });
        testArrays(new int[] { 1 }, new int[] { 2 }, new int[] { 1, 2, 3 });
        testArrays(new int[] { 0 }, new int[] { 0 }, new int[] { 0 });
        testArrays(new int[] { 0, 2 }, new int[] { 2, 5 }, new int[] { -1, 1, 2, 5, 6 });
        testArrays(new int[] { 0, 0, 0, 0 }, new int[] { 1, 1, 1, 1 }, new int[] { -1, 0, 1, 2 });
        testArrays(new int[] { 1, 2, 2 }, new int[] { 3, 3, 4 }, new int[] { -1, 0, 1 });
        testArrays(new int[] { 1 }, new int[] { 1 }, new int[] { 1 });
        testArrays(new int[] { 2 }, new int[] { 2 }, new int[] { 1 });
    }

    @Test
    public void stressTest() {
        long startTime = System.nanoTime();
        double duration = 0;
        while (duration < 60) {
            int[] points = RANDOM.ints(RANDOM.nextInt(STRESS_ARRAY_MAX_SIZE), -STRESS_MAX_NUMBER, STRESS_MAX_NUMBER).toArray();
            int[] starts = RANDOM.ints(RANDOM.nextInt(STRESS_ARRAY_MAX_SIZE), -STRESS_MAX_NUMBER, STRESS_MAX_NUMBER).toArray();

            int[] ends = new int[starts.length];
            for (int i = 0; i < starts.length; i++) {
                ends[i] = RANDOM.ints(1, starts[i], STRESS_MAX_NUMBER).toArray()[0];
            }

            testArrays(starts, ends, points);
            duration = (System.nanoTime() - startTime) / 1_000_000_000.0;
        }
    }

    private void testQuickSortSample(int[] array) {
        int[] expected = Arrays.copyOf(array, array.length);
        Arrays.sort(expected);

        PointsAndSegments.QuickSort.sort(array);
        Assert.assertArrayEquals(expected, array);
    }

    private void testArrays(int[] starts, int[] ends, int[] points) {
        int[] payoffsFast = PointsAndSegments.fastCountSegments(starts, ends, points);
        int[] payoffsNaive = PointsAndSegments.naiveCountSegments(starts, ends, points);

        Assert.assertArrayEquals(generateErrorMessage(starts, ends, points, payoffsNaive, payoffsFast), payoffsNaive,
                payoffsFast);
    }

    private String generateErrorMessage(int[] starts, int[] ends, int[] points, int[] expected, int[] result) {
        StringBuilder messageBuilder = new StringBuilder(ERROR_MESSAGE).append(starts.length).append(" ")
                .append(points.length).append("\n");
        for (int i = 0; i < starts.length; i++) {
            messageBuilder.append(starts[i]).append(" ").append(ends[i]).append("\n");
        }

        IntStream.of(points).forEach(point -> messageBuilder.append(point).append(" "));
        messageBuilder.append("\nExpected: ").append(Arrays.toString(expected)).append(" and got: ").append(Arrays.toString(result));
        return messageBuilder.toString();
    }

}
