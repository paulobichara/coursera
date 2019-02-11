package week4;

import org.junit.Assert;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Random;

public class MajorityElementTest {

    private static final String ERROR_MESSAGE = "Wrong Answer for: \n";
    private static final int BIG_ARRAY_SIZE = 100_000;
    private static final int BIG_NUMBER_BOUND = 1_000_000_000;

    private static final int STRESS_ARRAY_MAX_SIZE = 100;
    private static final int STRESS_MAX_NUMBER = 1000;

    private static final Random RANDOM = new SecureRandom();

    @Test
    public void testSamples() {
        Assert.assertEquals(ERROR_MESSAGE, 2, MajorityElement.getMajorityElement(new int[] { 2, 3, 9, 2, 2 }, 0, 5));
        Assert.assertEquals(ERROR_MESSAGE, -1, MajorityElement.getMajorityElement(new int[] { 1, 2 }, 0, 2));
        Assert.assertEquals(ERROR_MESSAGE, 6, MajorityElement.getMajorityElement(new int[] { 6, 4, 6, 6 }, 0, 4));
        Assert.assertEquals(ERROR_MESSAGE, 44, MajorityElement.getMajorityElement(new int[] { 18, 44, 44, 44 }, 0, 4));
        Assert.assertEquals(ERROR_MESSAGE, -1, MajorityElement.getMajorityElement(new int[] { 1, 2, 3, 4 }, 0, 4));
        Assert.assertEquals(ERROR_MESSAGE, -1, MajorityElement.getMajorityElement(new int[] { 1, 2, 3, 1 }, 0, 4));
        Assert.assertEquals(ERROR_MESSAGE, -1, MajorityElement.getMajorityElement(new int[] { 512766168, 717383758, 5, 126144732, 5, 573799007, 5, 5, 5, 405079772 }, 0, 10));
    }

    @Test
    public void testBigNumber() {
        int[] array = RANDOM.ints(BIG_ARRAY_SIZE, 0, BIG_NUMBER_BOUND).toArray();

        long startTime = System.nanoTime();
        MajorityElement.getMajorityElement(array, 0, BIG_ARRAY_SIZE);
        long stopTime = System.nanoTime();
        long totalTime = stopTime - startTime;
        Assert.assertTrue("Took longer time than expected: " + totalTime + " nano seconds", totalTime <= 1_500_000_000);
    }

    @Test
    public void stressTest() {
        long startTime = System.nanoTime();
        double duration = 0;
        while (duration < 30) {
            int[] array = RANDOM.ints(STRESS_ARRAY_MAX_SIZE, 0, STRESS_MAX_NUMBER).toArray();
            int fast = MajorityElement.getMajorityElement(array);
            int reference = MajorityElement.getMajorityElementNaive(array);
            Assert.assertEquals(generateErrorMessage(array, reference, fast), reference, fast);
            duration = (System.nanoTime() - startTime) / 1_000_000_000.0;
        }
    }

    private String generateErrorMessage(int[] array, int naive, int fast) {
        String message = ERROR_MESSAGE + array.length + "\n";
        for (int i = 0; i < array.length; i++) {
            message = message + array[i] + " ";
        }
        return message + "\nExpected: " + naive + " and got: " + fast;
    }

}
