import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

class Contact {
    int number;
    String name;

    Contact(int number, String name) {
        this.number = number;
        this.name = name;
    }
}

class PhoneBook {
    class HashFunction {
        static final int PRIME = 10000019;

        BigInteger cardinality;
        BigInteger param1;
        BigInteger param2;

        HashFunction(int cardinality) {
            this.cardinality = BigInteger.valueOf(cardinality);

            Random random = new Random(System.currentTimeMillis());
            this.param1 = BigInteger.valueOf(random.nextInt(PRIME - 1) + 1);
            this.param2 = BigInteger.valueOf(random.nextInt(PRIME));
        }

        int calculateHash(int number) {
            return param1.multiply(BigInteger.valueOf(number)).add(param2).mod(BigInteger.valueOf(PRIME))
                .mod(cardinality).intValueExact();
        }
    }

    private static final int SIZE = 100000;

    private Contact[] table;

    private HashFunction hashFunction;

    PhoneBook() {
        table = new Contact[SIZE];
        hashFunction = new HashFunction(SIZE);
    }

    void add(Contact contact) {
        table[hashFunction.calculateHash(contact.number)] = contact;
    }

    void del(int number) {
        table[hashFunction.calculateHash(number)] = null;
    }

    Contact find(int number) {
        return table[hashFunction.calculateHash(number)];
    }
}

class PhoneBookNaive {

    // Keep list of all existing (i.e. not deleted yet) contacts.
    private List<Contact> contacts = new ArrayList<>();

    void add(int number, String name) {
        // if we already have contact with such number,
        // we should rewrite contact's name
        boolean wasFound = false;
        for (Contact contact : contacts)
            if (contact.number == number) {
                contact.name = name;
                wasFound = true;
                break;
            }
        // otherwise, just add it
        if (!wasFound)
            contacts.add(new Contact(number, name));
    }

    void del(int number) {
        for (Iterator<Contact> it = contacts.iterator(); it.hasNext(); )
            if (it.next().number == number) {
                it.remove();
                break;
            }
    }

    Contact find(int number) {
        for (Contact contact : contacts)
            if (contact.number == number) {
                return contact;
            }

        return null;
    }
}

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
                    phoneBook.del(in.nextInt());
                    break;
            }
        }
    }

}
