package coursera.algorithmtoolbox.week2;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class LCMTest {

    private static final String ERROR_MESSAGE = "Wrong Answer";
    private static final int MAX_STRESS_NUMBER = 2_000;

    @Test
    public void testSamples() {
        Assert.assertEquals(ERROR_MESSAGE, 24, LCM.lcm(6, 8));
        Assert.assertEquals(ERROR_MESSAGE, 1933053046, LCM.lcm(28851538, 1183019));
    }

    @Test
    public void testBigNumber() {
        LCM.lcm(1999999999, 1888888888);
    }

    @Test
    public void stressTest() {
        while (true) {
            int number1 = getRandom(MAX_STRESS_NUMBER);
            int number2 = getRandom(MAX_STRESS_NUMBER);
            System.out.println("Looking for " + number1 + " and " + number2);
            long result1 = LCM.lcm(number1, number2);
            long result2 = LCM.lcm_naive(number1, number2);
            Assert.assertEquals(ERROR_MESSAGE, result1, result2);
        }
    }

    private int getRandom(int bound) {
        return ThreadLocalRandom.current().nextInt(1, bound);
    }

}
