package coursera.algorithmtoolbox.week2;

import static java.lang.Math.toIntExact;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FibonacciPartialSum {
    private static long getFibonacciPartialSumNaive(long from, long to) {
        long sum = 0;

        long current = 0;
        long next  = 1;

        for (long i = 0; i <= to; ++i) {
            if (i >= from) {
                sum += current;
            }

            long new_current = next;
            next = next + current;
            current = new_current;
        }

        return sum % 10;
    }

    static long getFibonacciPartialSum(long from, long to) {
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

