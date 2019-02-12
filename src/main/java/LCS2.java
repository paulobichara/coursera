import java.util.*;

public class LCS2 {

    private static int lcs2(int[] first, int[] second) {
        int[][] longestSequences = new int[first.length + 1][second.length + 1];

        for (int row = 0; row <= first.length; row++) {
            longestSequences[row][0] = 0;
        }

        for (int column = 0; column <= second.length; column++) {
            longestSequences[0][column] = 0;
        }

        for (int row = 1; row <= first.length; row++) {
            for (int column = 1; column <= second.length; column++) {
                longestSequences[row][column] = Math.max(longestSequences[row - 1][column], longestSequences[row][column - 1]);
                if (first[row - 1] == second[column - 1]) {
                    longestSequences[row][column] = Math.max(longestSequences[row][column], longestSequences[row - 1][column - 1] + 1);
                } else {
                    longestSequences[row][column] = Math.max(longestSequences[row][column], longestSequences[row - 1][column - 1]);
                }
            }
        }

        return longestSequences[first.length][second.length];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        int m = scanner.nextInt();
        int[] b = new int[m];
        for (int i = 0; i < m; i++) {
            b[i] = scanner.nextInt();
        }

        System.out.println(lcs2(a, b));
    }
}

