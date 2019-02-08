import org.junit.Assert;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Random;

public class ClosestTest {

    private static final String ERROR_MESSAGE = "Wrong Answer for: \n";

    private static final int ARRAY_MAX_SIZE = 50_000;
    private static final int MAX_NUMBER = 100_000_000;

    private static final int STRESS_ARRAY_MAX_SIZE = 6;
    private static final int STRESS_MAX_NUMBER = 10;

    private static final Random RANDOM = new SecureRandom();

    @Test
    public void testSamples() {
        Assert.assertEquals(5, Closest.minimalDistance(createPointArray(new long[]{ 0, 3 }, new long[]{ 0, 4 })), 0);
        Assert.assertEquals(0, Closest.minimalDistance(createPointArray(new long[]{ 7, 1, 4, 7 }, new long[]{ 7, 100, 8, 7 })), 0);
        Assert.assertEquals(2, Closest.minimalDistance(createPointArray(new long[]{ 9, 9, 9, 1, 4 }, new long[]{ -2, 0, -9, -1, 8 })), 0);

        double result = Closest.minimalDistance(createPointArray(new long[]{ 4, -2, -3, -1, 2, -4, 1, -1, 3, -4, -2 },
                new long[]{ 4, -2, -4, 3, 3, 0, 1, -1, -1, 2, 4 }));
        double expected = 1.414213;
        Assert.assertEquals(expected, result, result - expected);

        expected = 2.828427;
        result = Closest.minimalDistance(createPointArray(new long[]{ 0, 5, 3, 7 }, new long[]{ 0, 6, 4, 2 }));
        Assert.assertEquals(expected, result, result - expected);

        expected = 9.055385;
        result = Closest.minimalDistance(createPointArray(new long[]{ -8, 1 }, new long[]{ 8, 9 }));
        Assert.assertEquals(expected, result, result - expected);

        expected = 7.211102;
        result = Closest.minimalDistance(createPointArray(new long[]{ -5, -3, 9, -9 }, new long[]{ 3, -9, -1, -5 }));
        Assert.assertEquals(expected, result, result - expected);

        expected = 4.472135;
        result = Closest.minimalDistance(createPointArray(new long[]{ 5, 6, -1, -8, 7 }, new long[]{ 1, 6, -1, -1, -3 }));
        Assert.assertEquals(expected, result, result - expected);
    }

    @Test
    public void stressTest() {
        long startTime = System.nanoTime();
        double duration = 0;
        while (duration < 60) {
            long[] x = RANDOM.longs(STRESS_ARRAY_MAX_SIZE, -STRESS_MAX_NUMBER, STRESS_MAX_NUMBER).toArray();
            long[] y = RANDOM.longs(STRESS_ARRAY_MAX_SIZE, -STRESS_MAX_NUMBER, STRESS_MAX_NUMBER).toArray();
            System.out.println("Testing new input:");
            printInput(x, y);
            Assert.assertEquals(Closest.naiveMinimalDistance(createPointArray(x, y)),
                    Closest.minimalDistance(createPointArray(x, y)), 0);
            System.out.println("Finished\n\n");
            duration = (System.nanoTime() - startTime) / 1_000_000_000.0;
        }
    }

    private void printInput(long[] x, long[] y) {
        System.out.println(x.length);
        for (int i = 0; i < x.length; i++) {
            System.out.printf("%d %d%n", x[i], y[i]);
        }
    }

    private Closest.Point[] createPointArray(long[] x, long[] y) {
        Closest.Point[] points = new Closest.Point[x.length];
        for (int i = 0; i < x.length; i++) {
            points[i] = new Closest.Point(x[i], y[i]);
        }
        return points;
    }

}
