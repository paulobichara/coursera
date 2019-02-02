package week2;

import java.util.Scanner;

public class FibonacciFast {

  protected static long calc_fib(int n) {
    long[] sequence = new long[n + 1];

    for (int i = 0; i <= n; i++) {
      sequence[i] = i <= 1 ? i : sequence[i-2] + sequence[i-1];
    }

    return sequence[n];
  }

  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);
    int n = in.nextInt();

    System.out.println(calc_fib(n));
  }
}
