package coursera.algorithmtoolbox.week2;

import org.junit.Assert;
import org.junit.Test;

public class FibonacciTest {

    private static final String ERROR_MESSAGE = "Wrong Answer";

    @Test
    public void stressTest() {
        for (int i = 0; i <= 45; i++) {
            System.out.println("Looking for index " + i);
            long result1 = Fibonacci.calculateFibonacciNaive(i);
            long result2 = Fibonacci.calculateFibonacci(i);
            Assert.assertEquals(ERROR_MESSAGE, result1, result2);
        }
    }

}
