package coursera.algorithmtoolbox.week2;

import java.util.Scanner;

public class Fibonacci {
  static long calculateFibonacciNaive(int n) {
    if (n <= 1)
      return n;

    return calculateFibonacciNaive(n - 1) + calculateFibonacciNaive(n - 2);
  }

  static long calculateFibonacci(int n) {
    long[] sequence = new long[n + 1];

    for (int i = 0; i <= n; i++) {
      sequence[i] = i <= 1 ? i : sequence[i-2] + sequence[i-1];
    }

    return sequence[n];
  }

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    int n = in.nextInt();

    System.out.println(calculateFibonacci(n));
  }
}
