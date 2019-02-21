import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class PlacingParentheses {

    private static BigInteger getMaxValue(String exp) {
        BigInteger[] numbers = Stream.of(exp.split("[+*\\-]")).map(BigInteger::new).toArray(BigInteger[]::new);

        if (numbers.length > 1) {
            String[] operations = exp.split("[0-9]+");
            operations = Arrays.copyOfRange(operations, 1, operations.length);

            BigInteger[][] maxValues = new BigInteger[numbers.length][numbers.length];
            BigInteger[][] minValues = new BigInteger[numbers.length][numbers.length];

            for (int numberIndex = 0; numberIndex < numbers.length; numberIndex++) {
                maxValues[numberIndex][numberIndex] = numbers[numberIndex];
                minValues[numberIndex][numberIndex] = numbers[numberIndex];
            }

            for (int separation = 1; separation < numbers.length; separation++) {
                for (int startIndex = 0; startIndex < numbers.length - separation; startIndex++) {
                    setMinMax(maxValues, minValues, operations, startIndex, startIndex + separation);
                }
            }

            return maxValues[0][numbers.length - 1];
        } else {
            return numbers[0];
        }
    }

    private static void setMinMax(BigInteger[][] maxValues, BigInteger[][] minValues, String[] operations, int start, int end) {
        BigInteger minValue = BigInteger.valueOf(Long.MAX_VALUE);
        BigInteger maxValue = BigInteger.valueOf(Long.MIN_VALUE);

        for (int middleIndex = start; middleIndex < end; middleIndex++) {
            BigInteger result1 = applyOperation(maxValues[start][middleIndex], maxValues[middleIndex + 1][end], operations[middleIndex].charAt(0));
            BigInteger result2 = applyOperation(maxValues[start][middleIndex], minValues[middleIndex + 1][end], operations[middleIndex].charAt(0));
            BigInteger result3 = applyOperation(minValues[start][middleIndex], maxValues[middleIndex + 1][end], operations[middleIndex].charAt(0));
            BigInteger result4 = applyOperation(minValues[start][middleIndex], minValues[middleIndex + 1][end], operations[middleIndex].charAt(0));

            minValue = minValue.min(result1).min(result2).min(result3).min(result4);
            maxValue = maxValue.max(result1).max(result2).max(result3).max(result4);
        }

        maxValues[start][end] = maxValue;
        minValues[start][end] = minValue;
    }

    private static BigInteger applyOperation(BigInteger a, BigInteger b, char op) {
        if (op == '+') {
            return a.add(b);
        } else if (op == '-') {
            return a.subtract(b);
        } else if (op == '*') {
            return a.multiply(b);
        } else {
            assert false;
            return BigInteger.ZERO;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String exp = scanner.next();
        System.out.println(getMaxValue(exp));
    }
}

