package week2;

import org.junit.Assert;
import org.junit.Test;
import week2.FibonacciSumLastDigit;
import week2.FibonacciSumLastDigitFast;

import java.util.concurrent.ThreadLocalRandom;

public class FibonacciSumLastDigitFastTest {

    private static final String ERROR_MESSAGE = "Wrong Answer";
    private static final long MAX_LONG_STRESS = 2000;

    @Test
    public void testSamples() {
        Assert.assertEquals(ERROR_MESSAGE, 4, FibonacciSumLastDigitFast.getFibonacciSum(3));
        Assert.assertEquals(ERROR_MESSAGE, 5, FibonacciSumLastDigitFast.getFibonacciSum(100));
    }

    @Test
    public void testBigNumber() {
        FibonacciSumLastDigitFast.getFibonacciSum(1_000_000_000_000_000_000L);
    }

    @Test
    public void stressTest() {
        while (true) {
            long n = getRandomLong(MAX_LONG_STRESS);
            System.out.println("Looking for sum of last digit of first " + n + " fibonacci numbers");
            long result1 = FibonacciSumLastDigit.getFibonacciSumNaive(n);
            long result2 = FibonacciSumLastDigitFast.getFibonacciSum(n);
            Assert.assertEquals(ERROR_MESSAGE, result1, result2);
        }
    }

    private long getRandomLong(long bound) {
        return ThreadLocalRandom.current().nextLong(1, bound);
    }

    private int getRandomInt(int bound) {
        return ThreadLocalRandom.current().nextInt(2, bound);
    }
}
