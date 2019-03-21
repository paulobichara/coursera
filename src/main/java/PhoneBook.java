import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.StringTokenizer;

class FastScanner {
    private BufferedReader br;
    private StringTokenizer st;

    FastScanner() {
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    String next() {
        while (st == null || !st.hasMoreTokens()) {
            try {
                st = new StringTokenizer(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return st.nextToken();
    }

    int nextInt() {
        return Integer.parseInt(next());
    }
}

class PhoneBook {
    class HashFunction {
        static final int PRIME = 10000019;
        static final int CARDINALITY = 10000000;

        long param1;
        long param2;

        HashFunction() {
            Random random = new Random();
            this.param1 = random.nextInt(PRIME - 1) + 1;
            this.param2 = random.nextInt(PRIME);
        }

        int calculateHash(int number) {
            return (int) ((param1 * number + param2) % PRIME) % CARDINALITY;
        }
    }

    private String[] table;

    private HashFunction hashFunction;

    private PhoneBook() {
        table = new String[HashFunction.CARDINALITY];
        hashFunction = new HashFunction();
    }

    private void add(int number, String name) {
        table[hashFunction.calculateHash(number)] = name;
    }

    private void del(int number) {
        table[hashFunction.calculateHash(number)] = null;
    }

    private String find(int number) {
        return table[hashFunction.calculateHash(number)];
    }

    public static void main(String[] args) {
        FastScanner in = new FastScanner();
        int queryCount = in.nextInt();
        PhoneBook phoneBook = new PhoneBook();

        for (int index = 0; index < queryCount; index++) {
            switch (in.next()) {
                case "add":
                    phoneBook.add(in.nextInt(), in.next());
                    break;
                case "find":
                    String name = phoneBook.find(in.nextInt());
                    System.out.println(name == null ? "not found" : name);
                    break;
                case "del":
                    phoneBook.del(in.nextInt());
                    break;
            }
        }
    }
}
