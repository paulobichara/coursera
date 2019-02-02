package week2;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class FibonacciLastDigitFastTest {

    private static final String ERROR_MESSAGE = "Wrong Answer";
    private static final long MAX_NUMBER = 200;

    @Test
    public void testSamples() {
        Assert.assertEquals(ERROR_MESSAGE, 2, FibonacciLastDigitFast.getFibonacciLastDigit(3));
        Assert.assertEquals(ERROR_MESSAGE, 9, FibonacciLastDigitFast.getFibonacciLastDigit(331));
        Assert.assertEquals(ERROR_MESSAGE, 5, FibonacciLastDigitFast.getFibonacciLastDigit(327305));
    }

    @Test
    public void testBigNumber() {
        FibonacciLastDigitFast.getFibonacciLastDigit(10000000);
    }

    @Test
    public void stressTest() {
        while (true) {
            long number = getRandom(MAX_NUMBER);
            System.out.println("Looking for last fibonacci digit for " + number);
            long result1 = FibonacciLastDigit.getFibonacciLastDigitNaive((int) number);
            long result2 = FibonacciLastDigitFast.getFibonacciLastDigit(number);
            Assert.assertEquals(ERROR_MESSAGE, result1, result2);
        }
    }

    private long getRandom(long bound) {
        return ThreadLocalRandom.current().nextLong(bound);
    }
}
