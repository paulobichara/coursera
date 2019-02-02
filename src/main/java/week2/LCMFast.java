package week2;

import java.util.*;

public class LCMFast {

  protected static long lcm(int a, int b) {
    int factoredA = a;
    int factoredB = b;

    long lcm = 1;
    int factor = 2;

    while (factoredA != 1 || factoredB != 1) {
      boolean isAMultiple = factoredA % factor == 0;
      boolean isBMultiple = factoredB % factor == 0;

      factoredA = isAMultiple ? factoredA / factor : factoredA;
      factoredB = isBMultiple ? factoredB / factor : factoredB;

      if (!isAMultiple && !isBMultiple) {
        factor++;
      } else {
        lcm = lcm * factor;
      }

    }

    return lcm;
  }

  public static void main(String args[]) {
    Scanner scanner = new Scanner(System.in);
    int a = scanner.nextInt();
    int b = scanner.nextInt();

    System.out.println(lcm(a, b));
  }
}
