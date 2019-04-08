package coursera.datastructures.week4;

import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

public class RopeProblemTest {

    private static final Random RANDOM = new Random();
    private static final int MIN_CHAR_QTY = 1;
    //private static final int MAX_CHAR_QTY = 300_000;
    private static final int MAX_CHAR_QTY = 10;

    private static final int MIN_QUERY_QTY = 1;
    //private static final int MAX_QUERY_QTY = 100_000;
    private static final int MAX_QUERY_QTY = 100;

    private static class Operation {
        static int operationCount;

        int lowerBound;
        int upperBound;
        int k;

        Operation(int lowerBound, int upperBound, int k) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            this.k = k;
        }

        void execute(RopeProblem.Rope rope) {
            rope.processChange(lowerBound, upperBound, k);
        }

        String toOperationString() {
            return lowerBound + " " + upperBound + " " + k;
        }
    }

    @Test
    public void testSamples() {
        String text = "hlelowrold";
        RopeProblem.Rope rope = new RopeProblem.Rope(text);
        RopeProblemSlow.Rope naive = new RopeProblemSlow.Rope(text);
        rope.processChange(1, 1, 2);
        naive.processChange(1, 1, 2);
        rope.processChange(6, 6, 7);
        naive.processChange(6, 6, 7);
        Assert.assertEquals("Failed!", naive.text, rope.getText());

        text = "abcdef";
        rope = new RopeProblem.Rope(text);
        naive = new RopeProblemSlow.Rope(text);
        rope.processChange(0, 1, 1);
        naive.processChange(0, 1, 1);
        rope.processChange(4, 5, 0);
        naive.processChange(4, 5, 0);
        Assert.assertEquals("Failed!", naive.text, rope.getText());

        text = "whjjtbu";
        rope = new RopeProblem.Rope(text);
        naive = new RopeProblemSlow.Rope(text);
        rope.processChange(2, 4, 1);
        naive.processChange(2, 4, 1);
        rope.processChange(6, 6, 2);
        naive.processChange(6, 6, 2);
        rope.processChange(2, 2, 6);
        naive.processChange(2, 2, 6);
        rope.processChange(6, 6, 0);
        naive.processChange(6, 6, 0);
        rope.processChange(4, 4, 4);
        naive.processChange(4, 4, 4);
        Assert.assertEquals("Failed!", naive.text, rope.getText());
    }

    @Test
    public void stressTest() {
        long startTime = System.nanoTime();
        double duration = 0;
        StringBuilder messageBuilder;

        while (duration < 60) {
            String text = randomString();
            RopeProblem.Rope rope = new RopeProblem.Rope(text);
            RopeProblemSlow.Rope ropeNaive = new RopeProblemSlow.Rope(text);

            int queryQty = RANDOM.nextInt(MAX_QUERY_QTY - MIN_QUERY_QTY + 1) + MIN_QUERY_QTY;
            messageBuilder = new StringBuilder("Testing new input:\n\n");
            messageBuilder.append(text).append("\n").append(queryQty).append("\n");

            for (int index = 0; index < queryQty; index++) {
                Operation operation = randomOperation(text);
                messageBuilder.append(operation.toOperationString()).append("\n");
                try {
                    operation.execute(rope);
                    ropeNaive.processChange(operation.lowerBound, operation.upperBound, operation.k);
                    Assert.assertEquals(messageBuilder.toString(), ropeNaive.text, rope.getText());
                } catch (Exception e) {
                    Assert.fail(messageBuilder.toString());
                }
            }

            duration = (System.nanoTime() - startTime) / 1_000_000_000.0;
        }
    }

    private Operation randomOperation(String text) {
        int lowerBound = randomLowerBound(text.length());
        int upperBound = randomUpperBound(text.length(), lowerBound);
        int k = randomKFactor(text.length(), lowerBound, upperBound);
        return new Operation(lowerBound, upperBound, k);
    }

    private String randomString() {
        int size = RANDOM.nextInt(MAX_CHAR_QTY - MIN_CHAR_QTY + 1) + MIN_CHAR_QTY;
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < size; index++) {
            builder.append((char)(RANDOM.nextInt(26) + 'a'));
        }
        return builder.toString();
    }

    private int randomLowerBound(int size) {
        return RANDOM.nextInt(size);
    }

    private int randomUpperBound(int size, int lowerBound) {
        return RANDOM.ints(1, lowerBound, size).findFirst().getAsInt();
    }

    private int randomKFactor(int size, int lowerBound, int upperBound) {
        return RANDOM.nextInt((size - (upperBound - lowerBound + 1)) + 1);
    }

}
