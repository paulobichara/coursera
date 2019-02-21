package coursera.algorithmtoolbox.week2;

import java.util.concurrent.ThreadLocalRandom;
import org.junit.Assert;
import org.junit.Test;

public class FibonacciLastDigitTest {

    private static final String ERROR_MESSAGE = "Wrong Answer";
    private static final long MAX_NUMBER = 200;

    @Test
    public void testSamples() {
        Assert.assertEquals(ERROR_MESSAGE, 2, FibonacciLastDigit.getFibonacciLastDigit(3));
        Assert.assertEquals(ERROR_MESSAGE, 9, FibonacciLastDigit.getFibonacciLastDigit(331));
        Assert.assertEquals(ERROR_MESSAGE, 5, FibonacciLastDigit.getFibonacciLastDigit(327305));
    }

    @Test
    public void testBigNumber() {
        FibonacciLastDigit.getFibonacciLastDigit(10000000);
    }

    @Test
    public void stressTest() {
        while (true) {
            long number = getRandom(MAX_NUMBER);
            System.out.println("Looking for last fibonacci digit for " + number);
            long result1 = FibonacciLastDigit.getFibonacciLastDigitNaive((int) number);
            long result2 = FibonacciLastDigit.getFibonacciLastDigit(number);
            Assert.assertEquals(ERROR_MESSAGE, result1, result2);
        }
    }

    private long getRandom(long bound) {
        return ThreadLocalRandom.current().nextLong(bound);
    }
}
