package week2;

import java.util.*;

import static java.lang.Math.toIntExact;

public class FibonacciPartialSumFast {
    protected static long getFibonacciPartialSum(long from, long to) {
        List<Integer> sequence = new ArrayList<>();

        int periodLength = findPisanoPeriodLength(10, sequence);
        int sum = 0;

        for (int i = toIntExact(from % periodLength); i <= toIntExact(to % periodLength); i++) {
            sum += sequence.get(i);
        }

        return sum % 10;
    }

    private static int findPisanoPeriodLength(int m, List<Integer> sequence) {
        List<Integer> period = new ArrayList<>();
        List<Integer> current = new ArrayList<>();
        boolean foundPeriod = false;

        for (int i = 0; !foundPeriod; i++) {
            if (i <= 1) {
                sequence.add(i, i);
                period.add(i);
            } else {
                sequence.add(i, (sequence.get(i-2) + sequence.get(i-1)) % m);
                current.add(sequence.get(i));

                if (!period.subList(0, current.size()).equals(current)) {
                    period.addAll(current);
                    current.clear();
                }
            }
            foundPeriod = period.equals(current);
        }
        return period.size();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long from = scanner.nextLong();
        long to = scanner.nextLong();
        System.out.println(getFibonacciPartialSum(from, to));
    }
}

