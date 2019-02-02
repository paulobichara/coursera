package week2;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class LCMFastTest {

    private static final String ERROR_MESSAGE = "Wrong Answer";
    private static final int MAX_STRESS_NUMBER = 2_000;

    @Test
    public void testSamples() {
        Assert.assertEquals(ERROR_MESSAGE, 24, LCMFast.lcm(6, 8));
        Assert.assertEquals(ERROR_MESSAGE, 1933053046, LCMFast.lcm(28851538, 1183019));
    }

    @Test
    public void testBigNumber() {
        LCMFast.lcm(1999999999, 1888888888);
    }

    @Test
    public void stressTest() {
        while (true) {
            int number1 = getRandom(MAX_STRESS_NUMBER);
            int number2 = getRandom(MAX_STRESS_NUMBER);
            System.out.println("Looking for week2.LCM between " + number1 + " and " + number2);
            long result1 = LCMFast.lcm(number1, number2);
            long result2 = LCM.lcm_naive(number1, number2);
            Assert.assertEquals(ERROR_MESSAGE, result1, result2);
        }
    }

    private int getRandom(int bound) {
        return ThreadLocalRandom.current().nextInt(1, bound);
    }

}
