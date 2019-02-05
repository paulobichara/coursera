import org.junit.Assert;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

public class PointsAndSegmentsTest {

    private static final String ERROR_MESSAGE = "Wrong Answer for: \n";

    private static final int STRESS_ARRAY_MAX_SIZE = 10;
    private static final int STRESS_MAX_NUMBER = 100000000;

    private static final Random RANDOM = new SecureRandom();

    @Test
    public void testSamples() {
        Assert.assertArrayEquals(ERROR_MESSAGE, new int[] { 1, 0, 0 },
                PointsAndSegments.fastCountSegments(new int[] { 0, 7 }, new int[] { 5, 10 }, new int[] { 1, 6, 11 }));

        Assert.assertArrayEquals(ERROR_MESSAGE, new int[] { 0, 0, 1 },
                PointsAndSegments.fastCountSegments(new int[] { -10 }, new int[] { 10 }, new int[] { -100, 100, 0 }));

        Assert.assertArrayEquals(ERROR_MESSAGE, new int[] { 2, 0 },
                PointsAndSegments.fastCountSegments(new int[] { 0, -3, 7 }, new int[] { 5, 2, 10 }, new int[] { 1, 6 }));

        Assert.assertArrayEquals(ERROR_MESSAGE, new int[] { 1, 2, 1, 1 },
                PointsAndSegments.fastCountSegments(new int[] { 2, 3, 5, 7 }, new int[] { 3, 5, 5, 7 }, new int[] { 4, 3, 2, 7 }));

        Assert.assertArrayEquals(ERROR_MESSAGE, new int[] { 0, 0, 0 },
                PointsAndSegments.fastCountSegments(new int[] { 4, 6, 7, 9 }, new int[] { 4, 5, 5, 8 }, new int[] { 8, 6, 0 }));

        Assert.assertArrayEquals(ERROR_MESSAGE, new int[] { 0, 0, 0 },
                PointsAndSegments.fastCountSegments(new int[] { 1, 1 }, new int[] { 2, 2 }, new int[] { 8, 6, 0 }));

        Assert.assertArrayEquals(ERROR_MESSAGE, new int[] { 1, 1, 0 },
                PointsAndSegments.fastCountSegments(new int[] { 1 }, new int[] { 2 }, new int[] { 1, 2, 3 }));

        Assert.assertArrayEquals(ERROR_MESSAGE, new int[] { 1, 0, 0 },
                PointsAndSegments.fastCountSegments(new int[] { 1 }, new int[] { 1 }, new int[] { 1, 2, 3 }));

        Assert.assertArrayEquals(ERROR_MESSAGE, new int[] { 1 },
                PointsAndSegments.fastCountSegments(new int[] { 0 }, new int[] { 0 }, new int[] { 0 }));

        Assert.assertArrayEquals(ERROR_MESSAGE, new int[] { 0, 1, 2, 1, 0 },
                PointsAndSegments.fastCountSegments(new int[] { 0, 2 }, new int[] { 2, 5 }, new int[] { -1, 1, 2, 5, 6 }));

        Assert.assertArrayEquals(ERROR_MESSAGE, new int[] { 0, 4, 4, 0 },
                PointsAndSegments.fastCountSegments(new int[] { 0, 0, 0, 0 }, new int[] { 1, 1, 1, 1 }, new int[] { -1, 0, 1, 2 }));

        Assert.assertArrayEquals(ERROR_MESSAGE, new int[] { 0, 0, 1 },
                PointsAndSegments.fastCountSegments(new int[] { 1, 2, 2 }, new int[] { 3, 3, 4 }, new int[] { -1, 0, 1 }));

        Assert.assertArrayEquals(ERROR_MESSAGE, new int[] { 1 },
                PointsAndSegments.fastCountSegments(new int[] { 1 }, new int[] { 1 }, new int[] { 1 }));

        Assert.assertArrayEquals(ERROR_MESSAGE, new int[] { 0 },
                PointsAndSegments.fastCountSegments(new int[] { 2 }, new int[] { 2 }, new int[] { 1 }));
    }

    @Test
    public void stressTest() {
        long startTime = System.nanoTime();
        double duration = 0;
        while (true) {
            int[] starts = RANDOM.ints(RANDOM.nextInt(STRESS_ARRAY_MAX_SIZE), 0, STRESS_MAX_NUMBER).toArray();
            int[] ends = RANDOM.ints(starts.length, -STRESS_MAX_NUMBER, STRESS_MAX_NUMBER).toArray();
            int[] points = RANDOM.ints(RANDOM.nextInt(STRESS_ARRAY_MAX_SIZE), 0, STRESS_MAX_NUMBER).toArray();
            int[] payoffsFast = PointsAndSegments.fastCountSegments(starts, ends, points);
            int[] payoffsNaive = PointsAndSegments.naiveCountSegments(starts, ends, points);

            Assert.assertArrayEquals(generateErrorMessage(starts, ends, points, payoffsNaive, payoffsFast), payoffsNaive, payoffsFast);
            duration = (System.nanoTime() - startTime) / 1_000_000_000.0;
        }
    }

    private String generateErrorMessage(int[] starts, int[] ends, int[] points, int[] expected, int[] result) {
        String message = ERROR_MESSAGE + starts.length + " " + points.length + "\n";
        for (int i = 0; i < starts.length; i++) {
            message = message + starts[i] + " " + ends[i] + "\n";
        }
        for (int i = 0; i < points.length; i++) {
            message = message + points[i] + " ";
        }
        return message + "\nExpected: " + Arrays.toString(expected) + " and got: " + Arrays.toString(result);
    }

}
