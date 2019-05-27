import java.util.Arrays;
import java.util.Scanner;

public class ConstructBWT {


    private static String getBurrowsWheelerTransform(String text) {
        String[] rotations = new String[text.length()];
        for (int startIndex = 0; startIndex < text.length(); startIndex++) {
            int endIndex = (rotations.length + startIndex - 1) % rotations.length;
            if (endIndex > startIndex) {
                rotations[startIndex] = text.substring(startIndex, endIndex + 1);
            } else {
                rotations[startIndex] = text.substring(startIndex);
                rotations[startIndex] += text.substring(0, endIndex + 1);
            }
        }
        Arrays.sort(rotations);
        StringBuilder builder = new StringBuilder();
        Arrays.stream(rotations).forEach(rotation -> builder.append(rotation.charAt(rotation.length() - 1)));
        return builder.toString();
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println(getBurrowsWheelerTransform(scanner.next()));
        }
    }

}
