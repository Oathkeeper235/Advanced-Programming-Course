package Discounts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Discounts
 */
public class DiscountsTest {
    public static void main(String[] args) throws IOException {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

// Vashiot kod ovde

class Product {
    private final int price;
    private final int discountPrice;

    public Product(int price, int discountPrice) {
        this.price = price;
        this.discountPrice = discountPrice;
    }

    public int getPrice() {
        return price;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}

class Store {
    private String name;
    private final List<Product> products;

    public Store() {
        this.products = new ArrayList<>();
    }

    public Store(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public double averageDiscount() {
        return products.stream().mapToDouble(Product::getDiscountPrice).average().orElse(0);
    }
}

class Discounts {
    private final List<Store> stores;

    public Discounts() {
        this.stores = new ArrayList<>();
    }

    public int readStores(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        int counter = 0;
        String line = br.readLine();

        while (line != null) {
            String[] splits = line.split(" ");
            List<Product> products = new ArrayList<Product>();

            for (int i = 1; i < splits.length; i++) {
                String[] prices = splits[i].split(":");
                products.add(new Product(Integer.parseInt(prices[1]), Integer.parseInt(prices[0])));
            }

            stores.add(new Store(splits[0], products));

            counter++;
            line = br.readLine();
        }

        return counter;
    }

    public List<Store> byAverageDiscount() {
        return new ArrayList<>();
    }

    public List<Store> byTotalDiscount() {
        return new ArrayList<>();
    }
}