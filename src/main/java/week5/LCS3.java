package week5;

import java.util.*;

public class LCS3 {

    private static int lcs3(int[] a, int[] b, int[] c) {
        int[][][] longestSequences = new int[a.length + 1][b.length + 1][c.length + 1];

        for (int aIndex = 1; aIndex <= a.length; aIndex++) {
            for (int bIndex = 1; bIndex <= b.length; bIndex++) {
                for (int cIndex = 1; cIndex <= c.length; cIndex++) {
                    longestSequences[aIndex][bIndex][cIndex] = Math.max(longestSequences[aIndex - 1][bIndex][cIndex], longestSequences[aIndex][bIndex - 1][cIndex]);
                    longestSequences[aIndex][bIndex][cIndex] = Math.max(longestSequences[aIndex][bIndex][cIndex], longestSequences[aIndex][bIndex][cIndex - 1]);
                    if (a[aIndex - 1] == b[bIndex - 1] && a[aIndex - 1] == c[cIndex - 1]) {
                        longestSequences[aIndex][bIndex][cIndex] = Math.max(longestSequences[aIndex][bIndex][cIndex], longestSequences[aIndex - 1][bIndex - 1][cIndex - 1] + 1);
                    } else {
                        longestSequences[aIndex][bIndex][cIndex] = Math.max(longestSequences[aIndex][bIndex][cIndex], longestSequences[aIndex - 1][bIndex - 1][cIndex - 1]);
                    }
                }
            }
        }

        return longestSequences[a.length][b.length][c.length];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int an = scanner.nextInt();
        int[] a = new int[an];
        for (int i = 0; i < an; i++) {
            a[i] = scanner.nextInt();
        }
        int bn = scanner.nextInt();
        int[] b = new int[bn];
        for (int i = 0; i < bn; i++) {
            b[i] = scanner.nextInt();
        }
        int cn = scanner.nextInt();
        int[] c = new int[cn];
        for (int i = 0; i < cn; i++) {
            c[i] = scanner.nextInt();
        }
        System.out.println(lcs3(a, b, c));
    }
}

