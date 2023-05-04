package BooksCollection;

import java.util.*;
import java.util.stream.Collectors;

public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}

// Вашиот код овде

class Book implements Comparable<Book> {
    private final String title;
    private final String category;
    private final float price;

    public Book(String title, String category, float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public float getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int compareTo(Book o) {
        if (title.equals(o.title)) {
            return Integer.compare((int) price, (int) o.price);
        }

        return title.compareTo(o.title);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) %.2f", title, category, price);
    }
}

class BookCollection {
    private final List<Book> books;

    public BookCollection() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void printByCategory(String category) {
        books.stream().sorted().filter(i -> i.getCategory().equalsIgnoreCase(category)).forEach(System.out::println);
    }

    public List<Book> getCheapestN(int n) {
        return books.stream().sorted(Comparator.comparing(Book::getPrice).thenComparing(Book::getTitle)).limit(n).collect(Collectors.toList());
    }
}