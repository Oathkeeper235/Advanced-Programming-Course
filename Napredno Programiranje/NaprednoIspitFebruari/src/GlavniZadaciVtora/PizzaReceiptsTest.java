package GlavniZadaciVtora;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PizzaReceiptsTest {

    public static void printMap(Map<String, Integer> map) {
        map.forEach((k, v) -> System.out.println(String.format("%s -> %d", k, v)));
    }

    public static void main(String[] args) throws IOException {
        PizzaReceipts pizzaReceipts = new PizzaReceipts();

        System.out.println("READING DATA FROM INPUT STREAM");
        pizzaReceipts.readOrders(System.in);

        System.out.println("TESTING printRevenueByUser");
        pizzaReceipts.printRevenueByUser(System.out);

        System.out.println("TESTING printUserOrders");
        System.out.println("Stefan");
        pizzaReceipts.printUserOrders(System.out, "Stefan");
        System.out.println("Ana");
        pizzaReceipts.printUserOrders(System.out, "Ana");
        System.out.println("Gjorgji");
        pizzaReceipts.printUserOrders(System.out, "Gjorgji");

        System.out.println("TESTING usersByPizzaIngredient");
        Map<String, Integer> pepperoniMap = pizzaReceipts.usersByPizzaIngredient("pepperoni");
        Map<String, Integer> mushroomsMap = pizzaReceipts.usersByPizzaIngredient("mushrooms");
        Map<String, Integer> mozzarellaMap = pizzaReceipts.usersByPizzaIngredient("mozzarella");

        System.out.println("PEPPERONI");
        printMap(pepperoniMap);
        System.out.println("MUSHROOMS");
        printMap(mushroomsMap);
        System.out.println("MOZZARELLA");
        printMap(mozzarellaMap);

    }
}

class Topping {
    private boolean whiteSauce = false;
    private boolean redSauce = false;
    private boolean pepperoni = false;
    private boolean mushrooms = false;
    private boolean mozzarella = false;

    public void setWhiteSauce(boolean whiteSauce) {
        this.whiteSauce = whiteSauce;
    }

    public void setRedSauce(boolean redSauce) {
        this.redSauce = redSauce;
    }

    public void setPepperoni(boolean pepperoni) {
        this.pepperoni = pepperoni;
    }

    public void setMushrooms(boolean mushrooms) {
        this.mushrooms = mushrooms;
    }

    public void setMozzarella(boolean mozzarella) {
        this.mozzarella = mozzarella;
    }

    public boolean isWhiteSauce() {
        return whiteSauce;
    }

    public boolean isRedSauce() {
        return redSauce;
    }

    public boolean isPepperoni() {
        return pepperoni;
    }

    public boolean isMushrooms() {
        return mushrooms;
    }

    public boolean isMozzarella() {
        return mozzarella;
    }
}

class Order {
    private String customerName;
    private Topping toppings;

    public Order(String customerName, Topping toppings) {
        this.customerName = customerName;
        this.toppings = toppings;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Topping getToppings() {
        return toppings;
    }
}

class PizzaReceipts {
    private final List<Order> orders;

    public PizzaReceipts() {
        this.orders = new ArrayList<>();
    }

    public void readOrders (InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String input = br.readLine();

        while (input != null) {
            String[] splits = input.split(" ");
            String[] toppings = splits[1].split(":");

            for (String topping : toppings) {
                switch (topping) {

                }
            }
        }
    }
}