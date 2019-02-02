package week2;

import org.junit.Assert;
import org.junit.Test;
import week2.FibonacciHuge;
import week2.FibonacciHugeFast;

import java.util.concurrent.ThreadLocalRandom;

public class FibonacciHugeFastTest {

    private static final String ERROR_MESSAGE = "Wrong Answer";
    private static final long MAX_LONG_STRESS = 2000;
    private static final int MAX_INT_STRESS = 2000;

    @Test
    public void testSamples() {
        Assert.assertEquals(ERROR_MESSAGE, 161, FibonacciHugeFast.getFibonacciHuge(239, 1000));
        Assert.assertEquals(ERROR_MESSAGE, 75, FibonacciHugeFast.getFibonacciHuge(1000, 100));
        Assert.assertEquals(ERROR_MESSAGE, 151, FibonacciHugeFast.getFibonacciHuge(2816213588L, 239));
    }

    @Test
    public void testBigNumber() {
        FibonacciHugeFast.getFibonacciHuge(1_000_000_000_000_000_000L, 1000);
    }

    @Test
    public void stressTest() {
        while (true) {
            long n = getRandomLong(MAX_LONG_STRESS);
            int m = getRandomInt(MAX_INT_STRESS);
            System.out.println("Looking for " + n + "th fibonacci module " + m);
            long result1 = FibonacciHugeFast.getFibonacciHuge(n, m);
            long result2 = FibonacciHuge.getFibonacciHugeNaive(n, m);
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
