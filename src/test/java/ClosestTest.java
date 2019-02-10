import org.junit.Assert;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Random;

public class ClosestTest {

    private static final String ERROR_MESSAGE = "Wrong Answer for: \n";

    private static final int ARRAY_MAX_SIZE = 50_000;
    private static final int MAX_NUMBER = 100_000_000;

    private static final int STRESS_ARRAY_MAX_SIZE = 50;
    private static final int STRESS_MAX_NUMBER = 1000;

    private static final Random RANDOM = new SecureRandom();

    @Test
    public void testSamples() {
        Assert.assertEquals(5, Closest.minimalDistance(createPointArray(new long[]{ 0, 3 }, new long[]{ 0, 4 })), 0);
        Assert.assertEquals(0, Closest.minimalDistance(createPointArray(new long[]{ 7, 1, 4, 7 }, new long[]{ 7, 100, 8, 7 })), 0);
        Assert.assertEquals(2, Closest.minimalDistance(createPointArray(new long[]{ 9, 9, 9, 1, 4 }, new long[]{ -2, 0, -9, -1, 8 })), 0);
        Assert.assertEquals(1, Closest.minimalDistance(createPointArray(new long[]{ 3, 8, 7, 8, 6, 3 }, new long[]{ 9, 1, -9, 2, 8, -10 })), 0);
        Assert.assertEquals(3, Closest.minimalDistance(createPointArray(new long[]{ -9, 0, -5, 8, -1, 5 }, new long[]{ 9, -6, -6, -7, 6, -7 })), 0);

        assertMinDistance(createPointArray(new long[]{ 4, -2, -3, -1, 2, -4, 1, -1, 3, -4, -2 }, new long[]{ 4, -2, -4, 3, 3, 0, 1, -1, -1, 2, 4 }), 1.414213);
        assertMinDistance(createPointArray(new long[]{ 0, 5, 3, 7 }, new long[]{ 0, 6, 4, 2 }), 2.828427);
        assertMinDistance(createPointArray(new long[]{ -8, 1 }, new long[]{ 8, 9 }), 9.055385);
        assertMinDistance(createPointArray(new long[]{ -5, -3, 9, -9 }, new long[]{ 3, -9, -1, -5 }), 7.211102);
        assertMinDistance(createPointArray(new long[]{ 5, 6, -1, -8, 7 }, new long[]{ 1, 6, -1, -1, -3 }), 4.472135);
        assertMinDistance(createPointArray(new long[]{ -2, -3, -5, 4, 1, 1 }, new long[]{ 8, -3, -6, -1, 6, -2 }), 3.162277);
        assertMinDistance(createPointArray(new long[]{ -9, -1, -7, -6, 5, -6 }, new long[]{ -7, 3, 2, -9, -4, 7 }), 3.605551);
        assertMinDistance(createPointArray(new long[]{ 1, 7, -10, 6, -5, 6 }, new long[]{ 4, -5, -2, -6, 7, 5 }), 1.414213);
        assertMinDistance(createPointArray(new long[]{ -4, -6, 1, 6, -10, 3 }, new long[]{ -10, 5, -4, -6, -10, 7 }), 5.385164);
        assertMinDistance(createPointArray(new long[]{ -2, 6, -3, 6, -9, 4 }, new long[]{ -1, -2, 9, 9, 5, 6 }), 3.605551);
        assertMinDistance(createPointArray(new long[]{ 7, -6, 2, -3, -10, -6, 0 }, new long[]{ 5, 8, -2, 4, 6, -10, 8 }), 4.472135);
        assertMinDistance(createPointArray(new long[]{ -4, -3, 7, 1, 5 }, new long[]{ 4, 9, -4, 4, -7 }), 3.605551);
        assertMinDistance(createPointArray(new long[]{ 4, -9, -8, 7, -1, -3, -1 }, new long[]{ -5, 9, -9, -4, -7, 8, 9 }), 2.236067);
        assertMinDistance(createPointArray(new long[]{ 0, 3, 2, 2, 0, 6, 8 }, new long[]{ 6, -4, 2, -10, -2, 2, -9 }), 3.605551);
        assertMinDistance(createPointArray(new long[]{ 6, 4, -6, 8, -1, -2, -9, 4 }, new long[]{ -5, 8, -8, 6, -10, 0, -3, -4 }), 2.236067);
        assertMinDistance(createPointArray(new long[]{ -7, 4, -10, 2, 2, 4, 0, -2 }, new long[]{ -8, 7, -10, 3, -4, -4, -10, 0 }), 2.0);
        assertMinDistance(createPointArray(new long[]{ 8, -10, -8, -7, -1, -6, -8, -3 }, new long[]{ 8, -4, 4, -7, 8, 4, -5, 2 }), 2.0);
    }

    private void assertMinDistance(Closest.Point[] points, double expected) {
        double result = Closest.minimalDistance(points);
        Assert.assertEquals(expected, result, result - expected);
    }

    @Test
    public void stressTest() {
        long startTime = System.nanoTime();
        double duration = 0;
        StringBuilder messageBuilder;
        while (duration < 60) {
            long[] x = RANDOM.longs(STRESS_ARRAY_MAX_SIZE, -STRESS_MAX_NUMBER, STRESS_MAX_NUMBER).toArray();
            long[] y = RANDOM.longs(STRESS_ARRAY_MAX_SIZE, -STRESS_MAX_NUMBER, STRESS_MAX_NUMBER).toArray();
            messageBuilder = new StringBuilder("Testing new input:\n");
            messageBuilder.append(inputToString(x, y));
            try {
                Assert.assertEquals(messageBuilder.toString(), Closest.naiveMinimalDistance(createPointArray(x, y)),
                        Closest.minimalDistance(createPointArray(x, y)), 0);
            } catch (Exception e) {
                Assert.fail(messageBuilder.toString());
            }
            duration = (System.nanoTime() - startTime) / 1_000_000_000.0;
        }
    }

    private String inputToString(long[] x, long[] y) {
        StringBuilder builder = new StringBuilder().append(x.length).append("\n");
        for (int i = 0; i < x.length; i++) {
            builder.append(x[i]).append(" ").append(y[i]).append("\n");
        }
        return builder.toString();
    }

    private Closest.Point[] createPointArray(long[] x, long[] y) {
        Closest.Point[] points = new Closest.Point[x.length];
        for (int i = 0; i < x.length; i++) {
            points[i] = new Closest.Point(x[i], y[i]);
        }
        return points;
    }

}
