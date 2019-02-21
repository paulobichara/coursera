package coursera.algorithmtoolbox.week2;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class GCDTest {

    private static final String ERROR_MESSAGE = "Wrong Answer";
    private static final long MAX_NUMBER = 2_000;

    @Test
    public void testSamples() {
        Assert.assertEquals(ERROR_MESSAGE, 1, GCD.gcdEuclidian(18, 35));
        Assert.assertEquals(ERROR_MESSAGE, 17657, GCD.gcdEuclidian(28851538, 1183019));
    }

    @Test
    public void testBigNumber() {
        GCD.gcdEuclidian(1999999999, 1888888888);
    }

    @Test
    public void stressTest() {
        while (true) {
            long number1 = getRandom(MAX_NUMBER);
            long number2 = getRandom(MAX_NUMBER);
            System.out.println("Looking for coursera.algorithmtoolbox.week2.GCD between " + number1 + " and " + number2);
            long result1 = GCD.gcd_naive(number1, number2);
            long result2 = GCD.gcdEuclidian(number1, number2);
            Assert.assertEquals(ERROR_MESSAGE, result1, result2);
        }
    }

    private long getRandom(long bound) {
        return ThreadLocalRandom.current().nextLong(1, bound);
    }

}
