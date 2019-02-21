package coursera.algorithmtoolbox.week4;

import org.junit.Assert;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class SortingTest {

    private static final String ERROR_MESSAGE = "Wrong Answer for: \n";

    private static final int STRESS_ARRAY_MAX_SIZE = 10;
    private static final int STRESS_MAX_NUMBER = 10;

    private static final Random RANDOM = new SecureRandom();

    @Test
    public void testSamples() {
        testArray(new int[] { 2, 3, 9, 2, 2 }, new int[] { 2, 2, 2, 3, 9 });
        testArray(new int[] { 1, 2 }, new int[] { 1, 2 });
        testArray(new int[] { 6, 4, 6, 6 }, new int[] { 4, 6, 6, 6 });
        testArray(new int[] { 44, 44, 44, 18 }, new int[] { 18, 44, 44, 44 });
        testArray(new int[] { 1, 2, 3, 1 }, new int[] { 1, 1, 2, 3 });
    }

    @Test
    public void stressTest() {
        long startTime = System.nanoTime();
        double duration = 0;
        StringBuilder messageBuilder;
        while (duration < 60) {
            int[] numbers = RANDOM.ints(STRESS_ARRAY_MAX_SIZE, -STRESS_MAX_NUMBER, STRESS_MAX_NUMBER).toArray();
            messageBuilder = new StringBuilder("\n");
            messageBuilder.append(inputToString(numbers));
            try {
                int[] expected = Arrays.copyOf(numbers, numbers.length);
                Arrays.sort(expected);
                Sorting.randomizedQuickSort(numbers);
                Assert.assertArrayEquals(messageBuilder.toString(), expected, numbers);
            } catch (Exception e) {
                Assert.fail(messageBuilder.toString());
            }
            duration = (System.nanoTime() - startTime) / 1_000_000_000.0;
        }
    }

    private String inputToString(int[] numbers) {
        StringBuilder builder = new StringBuilder().append(numbers.length).append("\n");
        IntStream.of(numbers).forEach(number -> builder.append(number).append(" "));
        return builder.toString();
    }

    private void testArray(int[] array, int[] expected) {
        Sorting.randomizedQuickSort(array);
        Assert.assertArrayEquals(ERROR_MESSAGE, expected, array);
    }

}
