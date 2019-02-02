package week2;

import java.util.*;

public class GCDFast {
  protected static long gcdEuclidian(long a, long b) {
    long dividend = a;
    long divisor = b;

    while (divisor != 0) {
      long old_dividend = dividend;
      dividend = divisor;
      divisor = old_dividend % divisor;
    }

    return dividend;
  }

  public static void main(String args[]) {
    Scanner scanner = new Scanner(System.in);
    long a = scanner.nextInt();
    long b = scanner.nextInt();

    System.out.println(gcdEuclidian(a, b));
  }
}
