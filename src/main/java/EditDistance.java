import java.util.*;

class EditDistance {

    private static int editDistance(String first, String second) {
        int[][] editDistances = new int[first.length() + 1][second.length() + 1];

        for (int row = 0; row <= first.length(); row++) {
            editDistances[row][0] = row;
        }

        for (int column = 0; column <= second.length(); column++) {
            editDistances[0][column] = column;
        }

        for (int row = 1; row <= first.length(); row++) {
            for (int column = 1; column <= second.length(); column++) {
                editDistances[row][column] = Math.min(editDistances[row - 1][column] + 1, editDistances[row][column - 1] + 1);
                if (first.charAt(row - 1) != second.charAt(column - 1)) {
                    editDistances[row][column] = Math.min(editDistances[row][column], editDistances[row - 1][column - 1] + 1);
                } else {
                    editDistances[row][column] = Math.min(editDistances[row][column], editDistances[row - 1][column - 1]);
                }
            }
        }

        return editDistances[first.length()][second.length()];
    }

    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);

        String s = scan.next();
        String t = scan.next();

        System.out.println(editDistance(s, t));
    }

}
