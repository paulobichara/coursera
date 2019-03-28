package coursera.datastructures.week3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class FindPatternInText {

    static class HashFunction {
        static final int PRIME = 2_000_000_007;
        static final int CARDINALITY = 2_000_000_000;
        static final long X_FACTOR = new Random(System.currentTimeMillis()).nextInt(PRIME - 1) + 1;

        int computeHash(String value) {
            long hash = 0;
            for (int i = value.length() - 1; i >= 0; --i)
                hash = (hash * X_FACTOR + value.charAt(i)) % PRIME;
            return (int)hash % CARDINALITY;
        }

        int[] preComputeHashes(String pattern, String text) {
            int lastSubStrIndex = text.length() - pattern.length();
            int[] hashes = new int[ text.length() - pattern.length() + 1 ];
            hashes[text.length() - pattern.length()] = computeHash(text.substring(lastSubStrIndex));

            long y = 1;
            for (int index = 1; index <= pattern.length(); index++) {
                y = (y * X_FACTOR) % PRIME;
            }
            for (int index = lastSubStrIndex - 1; index >= 0; index--) {
                hashes[index] = (int) ((((X_FACTOR * hashes[index + 1] + text.charAt(index)
                        - y * text.charAt(index + pattern.length())) % PRIME) + PRIME) % PRIME);
            }
            return hashes;
        }

        List<Integer> findOccurrencesRabinKarp(String pattern, String text) {
            List<Integer> occurrences = new ArrayList<>();
            int patternHash = computeHash(pattern);
            int[] hashes = preComputeHashes(pattern, text);
            for (int index = 0; index <= text.length() - pattern.length(); index++) {
                if (patternHash == hashes[index] && pattern.equals(text.substring(index, index + pattern.length()))) {
                    occurrences.add(index);
                }
            }
            return occurrences;
        }
    }

    public static void main(String[] args) throws IOException {
        FastScanner in = new FastScanner();

        String pattern = in.next();
        String text = in.next();

        HashFunction hasher = new HashFunction();
        List<Integer> occurrences = hasher.findOccurrencesRabinKarp(pattern, text);

        occurrences.forEach(index -> System.out.print(index + " "));
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

}
