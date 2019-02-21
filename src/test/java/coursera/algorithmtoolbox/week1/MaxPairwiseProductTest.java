package coursera.algorithmtoolbox.week1;

import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class MaxPairwiseProductTest {

    private static final Random RANDOM = new Random();
    private static final int MAX_ARRAY_SIZE = 100;
    private static final int MAX_NUMBER = 200000;
    private static final String ERROR_MESSAGE = "Wrong Answer";

    @Test
    public void testBigArray() {
        long[] numbers = initializeBigArray();
        Assert.assertEquals(39_999_800_000L, MaxPairwiseProduct.getMaxPairwiseProduct(numbers));
    }

    @Test
    public void stressTest() {
        while (true) {
            long[] numbers = initializeStressArray();
            System.out.println(Arrays.toString(numbers));
            long result1 = MaxPairwiseProductNaive.getMaxPairwiseProduct(numbers);
            long result2 = MaxPairwiseProduct.getMaxPairwiseProduct(numbers);
            Assert.assertEquals(ERROR_MESSAGE, result1, result2);
        }
    }

    private long[] initializeStressArray() {
        int arraySize = getRandom(MAX_ARRAY_SIZE - 2) + 2;
        long[] numbers = new long[arraySize];
        for (int i = 0; i < arraySize; i++) {
            numbers[i] = getRandom(MAX_NUMBER);
        }
        return numbers;
    }

    @SneakyThrows
    private int getRandom(int bound) {
        return RANDOM.nextInt(bound);
    }

    private long[] initializeBigArray() {
        int size = 200000;
        long[] numbers = new long[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = i + 1;
        }
        return numbers;
    }


}
