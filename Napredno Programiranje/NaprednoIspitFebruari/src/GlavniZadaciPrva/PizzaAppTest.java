package GlavniZadaciPrva;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class PizzaAppTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        PizzaApp pizzaApp = new PizzaApp();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String method = parts[0];

            if (method.equalsIgnoreCase("registerUser")) { //email, name, phone
                String email = parts[1];
                String name = parts[2];
                String phone = parts[3];
                try {
                    pizzaApp.registerUser(email, name, phone);
                } catch (UserAlreadyExistsException e) {
                    e.printStackTrace();
                }
            } else if (method.equalsIgnoreCase("makeOrder")) { //email, pizzaName, price
                String email = parts[1];
                String pizzaName = parts[2];
                float price = Float.parseFloat(parts[3]);
                pizzaApp.makeOrder(email, pizzaName, price);
            } else if (method.equalsIgnoreCase("printRevenueByPizza")) {
                System.out.println("Print revenue by pizza");
                pizzaApp.printRevenueByPizza();
            } else { //printMostFrequentUserForPizza
                System.out.println("Print most frequent user(s) by pizza");

                pizzaApp.printMostFrequentUserForPizza();
            }
        }
    }
}

class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String string) {
        System.out.println(string);
    }
}

class Pizza {
    private final String pizzaName;
    private final float pizzaPrice;
    private final Map<String, Integer> orderCounter = new HashMap<>();

    public Pizza(String pizzaName, float pizzaPrice, String userName) {
        orderCounter.putIfAbsent(userName, 0);
        orderCounter.replace(userName, orderCounter.get(userName) + 1);
        this.pizzaName = pizzaName;
        this.pizzaPrice = pizzaPrice;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public List<String> mostFrequentUsers(List<User> users) {
        List<String> resultList = new ArrayList<>();
        int max = 0;
        for (User user : users) {
            if (orderCounter.containsKey(user.getEmail())) {
                if (orderCounter.get(user.getEmail()) >= max)
                    max = orderCounter.get(user.getEmail());
            }
        }

        int finalMax = max;
        orderCounter.forEach((key, value) -> {
            if (value == finalMax)
                resultList.add(key);
        });

        return resultList;
    }

    @Override
    public String toString() {
        return pizzaName + " " + pizzaPrice;
    }
}

class User {
    private final String email;
    private final String name;
    private final String phoneNumber;
    private final int id;
    private final List<Pizza> pizzaOrders;

    public User(String email, String name, String phoneNumber, int id) {
        this.pizzaOrders = new ArrayList<>();
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public List<Pizza> getPizzaOrders() {
        return pizzaOrders;
    }
}

class PizzaApp {
    private int idCounter;
    private final List<Pizza> orderedPizzas;
    private final List<User> users;
    private final Map<String, Float> pizzaRevenue;

    public PizzaApp() {
        this.idCounter = 1;
        this.orderedPizzas = new ArrayList<>();
        this.users = new ArrayList<>();
        this.pizzaRevenue = new HashMap<>();
    }

    public void registerUser(String email, String name, String phoneNumber) throws UserAlreadyExistsException {
        User user = new User(email, name, phoneNumber, idCounter);
        AtomicReference<Boolean> bool = new AtomicReference<>(false);

        for (User value : users) {
            if (value.getEmail().equals(email))
                throw new UserAlreadyExistsException(String.format("User with email %s already exists!", email));
        }

        if (!bool.get()) {
            users.add(user);
        }

        idCounter++;
    }

    public void makeOrder(String email, String pizzaName, float pizzaPrice) {
        Pizza pizza = new Pizza(pizzaName, pizzaPrice, email);
        if (!orderedPizzas.contains(pizza))
            orderedPizzas.add(pizza);
        users.forEach(user -> {
            if (user.getEmail().equals(email))
                user.getPizzaOrders().add(pizza);
        });

        pizzaRevenue.putIfAbsent(pizzaName, (float) 0);
        pizzaRevenue.replace(pizzaName, pizzaRevenue.get(pizzaName) + pizzaPrice);
    }

    public void printRevenueByPizza() {
        pizzaRevenue.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.comparing(Float::floatValue).reversed()))
                .forEach(set -> System.out.printf("%s %.2f\n", set.getKey(), set.getValue()));
    }

    public void printMostFrequentUserForPizza() {
        orderedPizzas.stream().sorted(Comparator.comparing(Pizza::getPizzaName))
                .forEach(pizza -> {
                    System.out.println("Pizza: " + pizza.getPizzaName());
                    System.out.println("ID email frequency");
                    List<String> result = pizza.mostFrequentUsers(users);

                    for (User user : users) {
                        for (String s : result) {
                            if (user.getEmail().equals(s)) {
                                System.out.println(user.getId() + " " + user.getEmail());
                            }
                        }
                    }
                });
    }
}

