package week2;

import org.junit.Assert;
import org.junit.Test;

public class FibonacciFastTest {

    private static final String ERROR_MESSAGE = "Wrong Answer";

    @Test
    public void stressTest() {
        for (int i = 0; i <= 45; i++) {
            System.out.println("Looking for index " + i);
            long result1 = Fibonacci.calc_fib(i);
            long result2 = FibonacciFast.calc_fib(i);
            Assert.assertEquals(ERROR_MESSAGE, result1, result2);
        }
    }

}
