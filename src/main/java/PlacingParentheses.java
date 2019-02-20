import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class PlacingParentheses {

    private static long getMaxValue(String exp) {
        long[] numbers = Stream.of(exp.split("[+*-]")).mapToLong(Long::valueOf).toArray();

        String[] operations = exp.split("[0-9]+");
        operations = Arrays.copyOfRange(operations, 1, operations.length);

        long[][] maxValues = new long[numbers.length][numbers.length];
        long[][] minValues = new long[numbers.length][numbers.length];

        for (int numberIndex = 0; numberIndex < numbers.length; numberIndex++) {
            maxValues[numberIndex][numberIndex] = numbers[numberIndex];
            minValues[numberIndex][numberIndex] = numbers[numberIndex];
        }

        for (int separation = 1; separation <= numbers.length - 1; separation++) {
            for (int index = 0; index < numbers.length - separation; index++) {
                setMinMax(maxValues, minValues, operations, index, index + separation);
            }
        }

        return maxValues[0][numbers.length - 1];
    }

    private static void setMinMax(long[][] maxValues, long[][] minValues, String[] operations, int start, int end) {
        long minValue = Long.MAX_VALUE;
        long maxValue = Long.MIN_VALUE;

        for (int middleIndex = start; middleIndex < end; middleIndex++) {
            long result1 = applyOperation(maxValues[start][middleIndex], maxValues[middleIndex + 1][end], operations[middleIndex].charAt(0));
            long result2 = applyOperation(maxValues[start][middleIndex], minValues[middleIndex + 1][end], operations[middleIndex].charAt(0));
            long result3 = applyOperation(minValues[start][middleIndex], maxValues[middleIndex + 1][end], operations[middleIndex].charAt(0));
            long result4 = applyOperation(minValues[start][middleIndex], minValues[middleIndex + 1][end], operations[middleIndex].charAt(0));

            minValue = Math.min(minValue, result1);
            minValue = Math.min(minValue, result2);
            minValue = Math.min(minValue, result3);
            minValue = Math.min(minValue, result4);

            maxValue = Math.max(maxValue, result1);
            maxValue = Math.max(maxValue, result2);
            maxValue = Math.max(maxValue, result3);
            maxValue = Math.max(maxValue, result4);
        }

        maxValues[start][end] = maxValue;
        minValues[start][end] = minValue;
    }

    private static long applyOperation(long a, long b, char op) {
        if (op == '+') {
            return a + b;
        } else if (op == '-') {
            return a - b;
        } else if (op == '*') {
            return a * b;
        } else {
            assert false;
            return 0;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String exp = scanner.next();
        System.out.println(getMaxValue(exp));
    }
}

