import org.junit.Assert;
import org.junit.Test;

public class PointsAndSegmentsTest {

    private static final String ERROR_MESSAGE = "Wrong Answer for: \n";

    @Test
    public void testPointsSorter() {
        testArray(new int[]{ 0 }, new int[] { 0 });
        testArray(new int[] { 1, 2 }, new int[] { 0, 1 });
        testArray(new int[]{ 2, 1 }, new int[] { 1, 0 });
        testArray(new int[]{ 1, 2, 3 }, new int[] { 0, 1, 2 });
        testArray(new int[]{ 1, 3, 2 }, new int[] { 0, 2, 1 });
        testArray(new int[]{ 2, 1, 3 }, new int[] { 1, 0, 2 });
        testArray(new int[]{ 2, 3, 1 }, new int[] { 2, 0, 1 });
        testArray(new int[]{ 3, 2, 1 }, new int[] { 2, 1, 0 });
        testArray(new int[]{ 1, 2, 3, 4 }, new int[] { 0, 1, 2, 3 });
        testArray(new int[]{ 1, 3, 2, 4 }, new int[] { 0, 2, 1, 3 });
        testArray(new int[]{ 2, 1, 3, 4 }, new int[] { 1, 0, 2, 3 });
        testArray(new int[]{ 1, 2, 4, 3 }, new int[] { 0, 1, 3, 2 });
        testArray(new int[]{ 3, 2, 1, 4 }, new int[] { 2, 1, 0, 3 });
        testArray(new int[]{ 4, 3, 2, 1 }, new int[] { 3, 2, 1, 0 });
        testArray(new int[]{ 2, 3, 9, 2, 9 }, new int[] { 0, 3, 1, 2, 4 });
    }

    @Test
    public void testSamples() {
        PointsAndSegments.Range[] ranges = new PointsAndSegments.Range[] { createRange(0, 5), createRange(7, 10) };
        Assert.assertArrayEquals(ERROR_MESSAGE, new int[] { 1, 0, 0 }, PointsAndSegments.fastCountSegments(ranges, new int[] { 1, 6, 11 }));

        ranges = new PointsAndSegments.Range[] { createRange( -10, 10) };
        Assert.assertArrayEquals(ERROR_MESSAGE, new int[] { 0, 0, 1 }, PointsAndSegments.fastCountSegments(ranges, new int[] { -100, 100, 0 }));

        ranges = new PointsAndSegments.Range[] { createRange( 0, 5 ), createRange( -3, 2 ), createRange( 7, 10 ) };
        Assert.assertArrayEquals(ERROR_MESSAGE, new int[] { 2, 0 }, PointsAndSegments.fastCountSegments(ranges, new int[] { 1, 6 }));
    }

    private PointsAndSegments.Range createRange(int start, int end) {
        return new PointsAndSegments.Range(start, end);
    }

    private void testArray(int[] array, int[] expected) {
        int[] sortedIndexes = new int[array.length];
        PointsAndSegments.PointsSorter.sort(array, sortedIndexes, 0, array.length);
        Assert.assertArrayEquals(expected, sortedIndexes);
    }
}
