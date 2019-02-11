package week4;

import org.junit.Assert;
import org.junit.Test;
import week4.Inversions;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

public class InversionsTest {

    private static final String ERROR_MESSAGE = "Wrong Answer for: \n";

    private static final int BIG_ARRAY_SIZE = 100_000;
    private static final int BIG_NUMBER_BOUND = 1_000_000_000;

    private static final Random RANDOM = new SecureRandom();

    @Test
    public void testSamples() {
        Assert.assertEquals(ERROR_MESSAGE, 0, Inversions.getNumberOfInversions(new int[] { 0 }));
        Assert.assertEquals(ERROR_MESSAGE, 0, Inversions.getNumberOfInversions(new int[] { 1, 2 }));
        Assert.assertEquals(ERROR_MESSAGE, 1, Inversions.getNumberOfInversions(new int[] { 2, 1 }));
        Assert.assertEquals(ERROR_MESSAGE, 0, Inversions.getNumberOfInversions(new int[] { 1, 2, 3 }));
        Assert.assertEquals(ERROR_MESSAGE, 1, Inversions.getNumberOfInversions(new int[] { 1, 3, 2 }));
        Assert.assertEquals(ERROR_MESSAGE, 1, Inversions.getNumberOfInversions(new int[] { 2, 1, 3 }));
        Assert.assertEquals(ERROR_MESSAGE, 2, Inversions.getNumberOfInversions(new int[] { 2, 3, 1 }));
        Assert.assertEquals(ERROR_MESSAGE, 3, Inversions.getNumberOfInversions(new int[] { 3, 2, 1 }));
        Assert.assertEquals(ERROR_MESSAGE, 0, Inversions.getNumberOfInversions(new int[] { 1, 2, 3, 4 }));
        Assert.assertEquals(ERROR_MESSAGE, 1, Inversions.getNumberOfInversions(new int[] { 1, 3, 2, 4 }));
        Assert.assertEquals(ERROR_MESSAGE, 1, Inversions.getNumberOfInversions(new int[] { 2, 1, 3, 4 }));
        Assert.assertEquals(ERROR_MESSAGE, 1, Inversions.getNumberOfInversions(new int[] { 1, 2, 4, 3 }));
        Assert.assertEquals(ERROR_MESSAGE, 3, Inversions.getNumberOfInversions(new int[] { 3, 2, 1, 4 }));
        Assert.assertEquals(ERROR_MESSAGE, 6, Inversions.getNumberOfInversions(new int[] { 4, 3, 2, 1 }));
        Assert.assertEquals(ERROR_MESSAGE, 2, Inversions.getNumberOfInversions(new int[] { 2, 3, 9, 2, 9 }));
        Assert.assertEquals(ERROR_MESSAGE, 15, Inversions.getNumberOfInversions(new int[] { 9, 8, 7, 3, 2, 1 }));
    }

    @Test
    public void testBigNumber() {
        int[] array = RANDOM.ints(BIG_ARRAY_SIZE, 0, BIG_NUMBER_BOUND).toArray();
        int[] reference = Arrays.copyOf(array, BIG_ARRAY_SIZE);
        Arrays.sort(reference);
        int[] sorted = new int[BIG_ARRAY_SIZE];
        long startTime = System.nanoTime();
        Inversions.getNumberOfInversions(array, sorted, 0, BIG_ARRAY_SIZE);
        long stopTime = System.nanoTime();
        long totalTime = stopTime - startTime;
        Assert.assertArrayEquals(ERROR_MESSAGE, sorted, reference);
        Assert.assertTrue("Took longer time than expected: " + totalTime + " nano seconds", totalTime <= 1_500_000_000);
    }

}
