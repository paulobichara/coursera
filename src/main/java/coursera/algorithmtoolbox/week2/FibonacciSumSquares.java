package coursera.algorithmtoolbox.week2;

import java.util.*;

public class FibonacciSumSquares {
    private static long getFibonacciSumSquaresNaive(long n) {
        if (n <= 1)
            return n;

        long previous = 0;
        long current  = 1;
        long sum      = 1;

        for (long i = 0; i < n - 1; ++i) {
            long tmp_previous = previous;
            previous = current;
            current = tmp_previous + current;
            sum += current * current;
        }

        return sum % 10;
    }

    private static long getFibonacciSumSquares(long n) {
        List<Integer> sequence = new ArrayList<>();

        int periodLength = findPisanoPeriodLength(10, sequence);
        int sum = 0;

        for (int i = 0; i <= Math.toIntExact(n % periodLength); i++) {
            sum += (sequence.get(i) * sequence.get(i)) % 10;
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
        long n = scanner.nextLong();
        long s = getFibonacciSumSquares(n);
        System.out.println(s);
    }
}

