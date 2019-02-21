package coursera.algorithmtoolbox.week2;

import java.util.*;

public class GCD {
  static long gcd_naive(long a, long b) {
    long current_gcd = 1;
    for(long d = 2; d <= a && d <= b; ++d) {
      if (a % d == 0 && b % d == 0) {
        if (d > current_gcd) {
          current_gcd = d;
        }
      }
    }

    return current_gcd;
  }

  static long gcdEuclidian(long a, long b) {
    long dividend = a;
    long divisor = b;

    while (divisor != 0) {
      long old_dividend = dividend;
      dividend = divisor;
      divisor = old_dividend % divisor;
    }

    return dividend;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    long a = scanner.nextInt();
    long b = scanner.nextInt();

    System.out.println(gcdEuclidian(a, b));
  }
}
