package Imenik;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

class DuplicateNumberException extends Exception {
    public DuplicateNumberException(String string) {
        super(string);
    }
}

class Contact implements Comparable<Contact> {
    private final String name;
    private final String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public int compareTo(Contact o) {
        if (!this.name.equals(o.getName())) {
            return this.name.compareTo(o.getName());
        }
        return this.number.compareTo(o.number);
    }

    @Override
    public String toString() {
        return name + " " + number;
    }
}

class PhoneBook {
    private final Set<Contact> set;
    private final Map<String, TreeSet<Contact>> treeSetMap;

    public PhoneBook() {
        this.set = new TreeSet<>();
        this.treeSetMap = new HashMap<>();
    }

    public void addContact(String name, String number) throws DuplicateNumberException {
        Contact newContact = new Contact(name, number);
        if (!set.add(newContact)) {
            throw new DuplicateNumberException(String.format("Duplicate number: %s", number));
        }
        TreeSet<Contact> tmp;
        if (treeSetMap.containsKey(name)) {
            tmp = treeSetMap.get(name);
        } else {
            tmp = new TreeSet<>();
        }
        tmp.add(newContact);
        treeSetMap.put(name, tmp);
    }

    public void contactsByNumber(String number) {
        AtomicBoolean bool = new AtomicBoolean(true);
        List<Contact> tmp = new ArrayList<>();

        set.forEach(contact -> {
            if (contact.getNumber().contains(number)) {
                tmp.add(contact);
                bool.set(false);
            }
        });

        if (bool.get()) {
            System.out.println("NOT FOUND");
        } else {
            tmp.stream().sorted(Contact::compareTo).forEach(System.out::println);
        }
    }

    public void contactsByName(String name) {
        if (treeSetMap.containsKey(name)) {
            treeSetMap.get(name).forEach(System.out::println);
        } else System.out.println("NOT FOUND");
    }
}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

// Вашиот код овде

