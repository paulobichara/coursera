package week3;

import org.junit.Assert;
import org.junit.Test;
import week3.LargestNumber;

public class LargestNumberTest {

    private static final String ERROR_MESSAGE = "Wrong Answer";

    @Test
    public void testSamples() {
        Assert.assertEquals(ERROR_MESSAGE, "221", LargestNumber.largestNumber(new String[] { "21", "2" }));
        Assert.assertEquals(ERROR_MESSAGE, "99641", LargestNumber.largestNumber(new String[] { "9", "4", "6", "1", "9" }));
        Assert.assertEquals(ERROR_MESSAGE, "99641", LargestNumber.largestNumber(new String[] { "9", "4", "6", "1", "9" }));
        Assert.assertEquals(ERROR_MESSAGE, "923923", LargestNumber.largestNumber(new String[] { "23", "39", "92" }));
        Assert.assertEquals(ERROR_MESSAGE, "52752", LargestNumber.largestNumber(new String[] { "52", "527" }));
        Assert.assertEquals(ERROR_MESSAGE, "9999999998888888888887777777776666666666555555554444444443333333333222" +
                "222222111111111111111101010101010101010", LargestNumber.largestNumber(new String[] { "2", "8", "2",
                "3", "6", "4", "1", "1", "10", "6", "3", "3", "6", "1", "3", "8", "4", "6", "1", "10", "8", "4", "10",
                "4", "1", "3", "2", "3", "2", "6", "1", "5", "2", "9", "8", "5", "10", "8", "7", "9", "6", "4", "2",
                "6", "3", "8", "8", "9", "8", "2", "9", "10", "3", "10", "7", "5", "7", "1", "7", "5", "1", "4", "7",
                "6", "1", "10", "5", "4", "8", "4", "2", "7", "8", "1", "1", "7", "4", "1", "1", "9", "8", "6", "5",
                "9", "9", "3", "7", "6", "3", "10", "8", "10", "7", "2", "5", "1", "1", "9", "9", "5" }));
    }

//    @Test
//    public void testBigNumber() {
//
//    }

}
