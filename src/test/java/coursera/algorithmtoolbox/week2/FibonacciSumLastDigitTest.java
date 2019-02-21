package coursera.algorithmtoolbox.week2;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class FibonacciSumLastDigitTest {

    private static final String ERROR_MESSAGE = "Wrong Answer";
    private static final long MAX_LONG_STRESS = 2000;

    @Test
    public void testSamples() {
        Assert.assertEquals(ERROR_MESSAGE, 4, FibonacciSumLastDigit.getFibonacciSum(3));
        Assert.assertEquals(ERROR_MESSAGE, 5, FibonacciSumLastDigit.getFibonacciSum(100));
    }

    @Test
    public void testBigNumber() {
        FibonacciSumLastDigit.getFibonacciSum(1_000_000_000_000_000_000L);
    }

    @Test
    public void stressTest() {
        while (true) {
            long n = getRandomLong(MAX_LONG_STRESS);
            System.out.println("Looking for sum of last digit of first " + n + " fibonacci numbers");
            long result1 = FibonacciSumLastDigit.getFibonacciSumNaive(n);
            long result2 = FibonacciSumLastDigit.getFibonacciSum(n);
            Assert.assertEquals(ERROR_MESSAGE, result1, result2);
        }
    }

    private long getRandomLong(long bound) {
        return ThreadLocalRandom.current().nextLong(1, bound);
    }

}
