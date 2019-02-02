import org.junit.Assert;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.PrimitiveIterator;
import java.util.Random;

public class SortingTest {

    private static final String ERROR_MESSAGE = "Wrong Answer for: \n";

    @Test
    public void testSamples() {
        testArray(new int[] { 2, 3, 9, 2, 2 }, new int[] { 2, 2, 2, 3, 9 });
        testArray(new int[] { 1, 2 }, new int[] { 1, 2 });
        testArray(new int[] { 6, 4, 6, 6 }, new int[] { 4, 6, 6, 6 });
        testArray(new int[] { 44, 44, 44, 18 }, new int[] { 18, 44, 44, 44 });
        testArray(new int[] { 1, 2, 3, 1 }, new int[] { 1, 1, 2, 3 });
    }

    private void testArray(int[] array, int[] expected) {
        Sorting.randomizedQuickSort(array);
        Assert.assertArrayEquals(ERROR_MESSAGE, expected, array);
    }

}
