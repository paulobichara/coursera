package coursera.algorithmtoolbox.week2;

import org.junit.Assert;
import org.junit.Test;

public class FibonacciPartialSumTest {

    private static final String ERROR_MESSAGE = "Wrong Answer";

    @Test
    public void testSamples() {
        Assert.assertEquals(ERROR_MESSAGE, 1, FibonacciPartialSum.getFibonacciPartialSum(3, 7));
        Assert.assertEquals(ERROR_MESSAGE, 5, FibonacciPartialSum.getFibonacciPartialSum(10, 10));
        Assert.assertEquals(ERROR_MESSAGE, 2, FibonacciPartialSum.getFibonacciPartialSum(10, 200));
    }

    @Test
    public void testBigNumber() {
        FibonacciPartialSum.getFibonacciPartialSum(1_000_000_000_000_000_000L, 1_000_000_000_000_000_000L);
        FibonacciPartialSum.getFibonacciPartialSum(999_999_999_999_999_999L, 1_000_000_000_000_000_000L);
    }

}
