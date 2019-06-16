package coursera.strings.week3;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SuffixArrayMatching {

    private static class Alphabet {
        static final int SIZE = 5;

        static int getCharKey(char character) {
            switch (character) {
                case '$': return 0;
                case 'A': return 1;
                case 'C': return 2;
                case 'G': return 3;
                case 'T': return 4;
                default: throw new RuntimeException("Unexpected character '" + character + "'");
            }
        }
    }

    private static class SuffixArrayWithLCP {
        String text;
        int[] suffixes;
        int[] lcp;

        SuffixArrayWithLCP(String text) {
            this.text = text + "$";
            suffixes = SuffixArrayBuilder.buildSuffixArray(this.text);
            lcp = LCPArrayBuilder.computeLCPArray(this.text, suffixes);
        }

        void matchesPattern(String pattern, Map<Integer, Boolean> printedMap) {
            int minIndex = 0;
            int maxIndex = text.length();
            while (minIndex < maxIndex) {
                int midIndex = (minIndex + maxIndex) / 2;
                if (compare(pattern, suffixes[midIndex]).result > 0) {
                    minIndex = midIndex + 1;
                } else {
                    maxIndex = midIndex;
                }
            }

            int start = minIndex;
            maxIndex = text.length();
            while (minIndex < maxIndex) {
                int midIndex = (minIndex + maxIndex) / 2;
                if (compare(pattern, suffixes[midIndex]).result < 0) {
                    maxIndex = midIndex;
                } else {
                    minIndex = midIndex + 1;
                }
            }
            int end = maxIndex;
            if (start < end || (start == end && start < suffixes.length
                    && compare(pattern, suffixes[start]).suffixContains)) {
                for (int index = start; (index == start ||
                        (index - 1 < lcp.length && lcp[index - 1] >= pattern.length())); index++) {
                    if (!printedMap.containsKey(suffixes[index])) {
                        System.out.print(suffixes[index] + " ");
                        printedMap.put(suffixes[index], true);
                    }
                }
            }
        }

        private static class ComparisonResult {
            int result;
            boolean suffixContains;

            ComparisonResult(int result, boolean suffixContains) {
                this.result = result;
                this.suffixContains = suffixContains;
            }
        }

        private ComparisonResult compare(String pattern, int suffixStart) {
            for (int index = suffixStart; index < text.length(); index++) {
                int patternIndex = index - suffixStart;
                if (patternIndex == pattern.length()) {
                    return new ComparisonResult(-1, true);
                }

                int charPattern = Alphabet.getCharKey(pattern.charAt(index - suffixStart));
                int charSuffix = Alphabet.getCharKey(text.charAt(index));
                if (charPattern > charSuffix) {
                    return new ComparisonResult(1, false);
                } else if (charPattern < charSuffix) {
                    return new ComparisonResult(-1, false);
                }
            }

            if (pattern.length() == text.length() - suffixStart) {
                return new ComparisonResult(0, true);
            }

            return new ComparisonResult(1, false);
        }
    }

    private static class SuffixArrayBuilder {

        static int[] buildSuffixArray(String text) {
            int[] order = sortCharacters(text);
            int[] classes = computeCharClasses(text, order);
            int length = 1;

            while (length < text.length()) {
                order = sortDoubled(text, length, order, classes);
                classes = updateClasses(order, classes, length);
                length *= 2;
            }

            return order;
        }

        private static int[] sortCharacters(String text) {
            int[] count = new int[Alphabet.SIZE];
            for (int index = 0; index < text.length(); index++) {
                count[Alphabet.getCharKey(text.charAt(index))]++;
            }
            for (int index = 1; index < Alphabet.SIZE; index++) {
                count[index] += count[index - 1];
            }
            int[] order = new int[text.length()];
            for (int index = text.length() - 1; index >= 0; index--) {
                int current = Alphabet.getCharKey(text.charAt(index));
                count[current]--;
                order[count[current]] = index;
            }
            return order;
        }

        private static int[] computeCharClasses(String text, int[] order) {
            int[] classes = new int[text.length()];
            classes[order[0]] = 0;
            for (int index = 1; index < text.length(); index++) {
                if (text.charAt(order[index]) != text.charAt(order[index - 1])) {
                    classes[order[index]] = classes[order[index - 1]] + 1;
                } else {
                    classes[order[index]] = classes[order[index - 1]];
                }
            }
            return classes;
        }

        private static int[] sortDoubled(String text, int length, int[] order, int[] classes) {
            int[] count = new int[text.length()];
            for (int index = 0; index < text.length(); index++) {
                count[classes[index]]++;
            }
            for (int index = 1; index < text.length(); index++) {
                count[index] += count[index - 1];
            }
            int[] newOrder = new int[text.length()];
            for (int index = text.length() - 1; index >= 0; index--) {
                int start = (order[index] - length + text.length()) % text.length();
                int clazz = classes[start];
                count[clazz]--;
                newOrder[count[clazz]] = start;
            }
            return newOrder;
        }

        private static int[] updateClasses(int[] newOrder, int[] classes, int length) {
            int[] newClasses = new int[newOrder.length];
            newClasses[newOrder[0]] = 0;
            for (int index = 1; index < newClasses.length; index++) {
                int current = newOrder[index], previous = newOrder[index - 1];
                int middle = (current + length) % newClasses.length;
                int middlePrev = (previous + length) % newClasses.length;
                if (classes[current] != classes[previous] || classes[middle] != classes[middlePrev]) {
                    newClasses[current] = newClasses[previous] + 1;
                } else {
                    newClasses[current] = newClasses[previous];
                }
            }
            return newClasses;
        }
    }

    private static class LCPArrayBuilder {

        static int[] computeLCPArray(String text, int[] suffixes) {
            int[] lcpArray = new int[suffixes.length - 1];
            int lcp = 0;
            int[] positions = invertSuffixArray(suffixes);
            int suffix = suffixes[0];

            for (int index = 0; index < text.length(); index++) {
                int suffixIndex = positions[suffix];
                if (suffixIndex == text.length() - 1) {
                    lcp = 0;
                } else {
                    int nextSuffix = suffixes[suffixIndex + 1];
                    lcp = getSuffixesLCP(text, suffix, nextSuffix, lcp - 1);
                    lcpArray[suffixIndex] = lcp;
                }
                suffix = (suffix + 1) % text.length();
            }

            return lcpArray;
        }

        private static int getSuffixesLCP(String text, int firstStart, int secondStart, int equal) {
            int lcp = Math.max(0, equal);
            while (firstStart + lcp < text.length() && secondStart + lcp < text.length()) {
                if (text.charAt(firstStart + lcp) == text.charAt(secondStart + lcp)) {
                    lcp++;
                } else {
                    break;
                }
            }
            return lcp;
        }

        private static int[] invertSuffixArray(int[] suffixes) {
            int[] positions = new int[suffixes.length];
            for (int index = 0; index < suffixes.length; index++) {
                positions[suffixes[index]] = index;
            }
            return positions;
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in); PrintWriter writer = new PrintWriter(System.out)) {
            SuffixArrayWithLCP suffixArray = new SuffixArrayWithLCP(scanner.next());
            int patternsQty = scanner.nextInt();
            Map<Integer, Boolean> processed = new HashMap<>();
            while (patternsQty > 0) {
                patternsQty--;
                suffixArray.matchesPattern(scanner.next(), processed);
            }
        }
    }

}
