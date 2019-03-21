import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.Random;
import java.util.StringTokenizer;

public class PhoneBookProblem {

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

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

    static class Contact {
        int number;
        String name;

        Contact(int number, String name) {
            this.number = number;
            this.name = name;
        }
    }

    static class HashFunction {
        static final int PRIME = 10000019;

        int cardinality;
        long param1;
        long param2;

        HashFunction(int cardinality) {
            this.cardinality = cardinality;

            Random random = new SecureRandom();
            this.param1 = random.nextInt(PRIME - 1) + 1;
            this.param2 = random.nextInt(PRIME);
        }

        int calculateHash(int number) {
            return (int) (((param1 * number + param2) % PRIME) % cardinality);
        }
    }

    static class PhoneBook {
        static final int SIZE = 100000;

        Contact[] table;
        int numKeys;

        HashFunction hashFunction;

        PhoneBook() {
            numKeys = 0;
            table = new Contact[SIZE];
            hashFunction = new HashFunction(SIZE);
        }

        void add(Contact contact) {
            table[hashFunction.calculateHash(contact.number)] = contact;
        }

        void remove(int number) {
            table[hashFunction.calculateHash(number)] = null;
        }

        Contact find(int number) {
            return table[hashFunction.calculateHash(number)];
        }
    }

    public static void main(String[] args) {
        FastScanner in = new FastScanner();
        int queryCount = in.nextInt();
        PhoneBook phoneBook = new PhoneBook();

        for (int index = 0; index < queryCount; index++) {
            switch (in.next()) {
                case "add":
                    phoneBook.add(new Contact(in.nextInt(), in.next()));
                    break;
                case "find":
                    Contact contact = phoneBook.find(in.nextInt());
                    System.out.println(contact == null ? "not found" : contact.name);
                    break;
                case "del":
                    phoneBook.remove(in.nextInt());
                    break;
            }
        }
    }

}
