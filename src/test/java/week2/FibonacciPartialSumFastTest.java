package week2;

import org.junit.Assert;
import org.junit.Test;
import week2.FibonacciPartialSumFast;

public class FibonacciPartialSumFastTest {

    private static final String ERROR_MESSAGE = "Wrong Answer";
    private static final long MAX_LONG_STRESS = 2000;

    @Test
    public void testSamples() {
        Assert.assertEquals(ERROR_MESSAGE, 1, FibonacciPartialSumFast.getFibonacciPartialSum(3, 7));
        Assert.assertEquals(ERROR_MESSAGE, 5, FibonacciPartialSumFast.getFibonacciPartialSum(10, 10));
        Assert.assertEquals(ERROR_MESSAGE, 2, FibonacciPartialSumFast.getFibonacciPartialSum(10, 200));
    }

    @Test
    public void testBigNumber() {
        FibonacciPartialSumFast.getFibonacciPartialSum(1_000_000_000_000_000_000L, 1_000_000_000_000_000_000L);
        FibonacciPartialSumFast.getFibonacciPartialSum(999_999_999_999_999_999L, 1_000_000_000_000_000_000L);
    }

}
