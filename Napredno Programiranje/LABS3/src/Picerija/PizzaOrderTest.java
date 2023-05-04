package Picerija;

import java.util.*;

interface Item {
    int getPrice();

    String getType();
}

class InvalidExtraTypeException extends Exception {
    public InvalidExtraTypeException() {
        super("Extra Type Not On Menu");
    }
}

class InvalidPizzaTypeException extends Exception {
    public InvalidPizzaTypeException() {
        super("Pizza Type Not On Menu");
    }
}

class ItemOutOfStockException extends Exception {
    public ItemOutOfStockException(Item item) {
        super(item.getType() + "Out Of Stock");
    }
}

class OrderLockedException extends Exception {
    public OrderLockedException() {
        super("Order is Locked");
    }
}

class EmptyOrder extends Exception {
    public EmptyOrder() {
        super("Order is Empty");
    }
}

class ExtraItem implements Item {

    private final String type;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        if (type.equals("Coke") || type.equals("Ketchup"))
            this.type = type;
        else throw new InvalidExtraTypeException();
    }

    @Override
    public int getPrice() {
        switch (type) {
            case "Coke":
                return 5;
            case "Ketchup":
                return 3;
        }
        return 0;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtraItem extraItem = (ExtraItem) o;
        return Objects.equals(type, extraItem.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}

class PizzaItem implements Item {

    private final String type;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        if (type.equals("Standard") || type.equals("Pepperoni") || type.equals("Vegetarian"))
            this.type = type;
        else throw new InvalidPizzaTypeException();
    }

    @Override
    public int getPrice() {
        switch (type) {
            case "Standard":
                return 10;
            case "Pepperoni":
                return 12;
            case "Vegetarian":
                return 8;
        }
        return 0;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaItem pizzaItem = (PizzaItem) o;
        return Objects.equals(type, pizzaItem.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}

class Product {
    private final Item item;
    private int count;

    public Product(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

class Order {

    private final List<Product> orders;
    private boolean locked;

    public Order() {
        this.orders = new ArrayList<>();
        this.locked = false;
    }

    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if (count > 10)
            throw new ItemOutOfStockException(item);
        if (locked)
            throw new OrderLockedException();

        for (Product pr : orders) {
            if (pr.getItem().equals(item)) {
                pr.setCount(count);
                return;
            }
        }
        orders.add(new Product(item, count));
    }

    public void removeItem(int idx) throws ArrayIndexOutOfBoundsException, OrderLockedException {
        if (locked)
            throw new OrderLockedException();
        if (idx < 0 || idx >= orders.size())
            throw new ArrayIndexOutOfBoundsException(idx);

        orders.remove(idx);
    }

    public int getPrice() {
        return orders.stream().mapToInt(i -> i.getItem().getPrice() * i.getCount()).sum();
    }

    public void lock() throws EmptyOrder {
        if (orders.size() > 0)
            locked = true;
        else throw new EmptyOrder();
    }

    public void displayOrder() {
        StringBuilder sb = new StringBuilder();

        orders.stream().forEach(i -> sb.append(String.format("%3d.%-15sx%2d%5d$%n", orders.indexOf(i) + 1, i.getItem().getType(), i.getCount(), i.getItem().getPrice() * i.getCount())));
        sb.append(String.format("%-22s%5d$", "Total:", getPrice()));

        System.out.println(sb);
    }
}

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}