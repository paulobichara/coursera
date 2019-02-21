package coursera.algorithmtoolbox.week2;

import java.util.*;

public class FibonacciLastDigit {
    static int getFibonacciLastDigitNaive(int n) {
        if (n <= 1)
            return n;

        int previous = 0;
        int current  = 1;

        for (int i = 0; i < n - 1; ++i) {
            int tmp_previous = previous;
            previous = current;
            current = tmp_previous + current;
        }

        return current % 10;
    }

    static int getFibonacciLastDigit(long n) {
        if (n <= 1)
            return (int) n;

        int previous = 0;
        int current  = 1;

        for (long i = 0; i < n - 1; ++i) {
            int tmp_previous = previous;
            previous = current;
            current = (tmp_previous % 10) + (current % 10);
        }

        return current % 10;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextLong();
        long c = getFibonacciLastDigit(n);
        System.out.println(c);
    }
}

