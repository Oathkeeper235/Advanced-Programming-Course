package Komponenti;

import java.time.Period;
import java.util.*;

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if (what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде

class InvalidPositionException extends Exception {
    public InvalidPositionException(String message) {
        super(message);
    }
}

class Component implements Comparable<Component> {
    private String color;
    private final int weight;
    private final Set<Component> internalComponents;

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        this.internalComponents = new TreeSet<>();
    }

    public void addComponent(Component component) {
        internalComponents.add(component);
    }

    @Override
    public int compareTo(Component o) {
        int compare = weight - o.weight;

        if (compare != 0)
            return compare;
        else compare = color.compareTo(o.color);

        return compare;
    }

    public int getWeight() {
        return weight;
    }

    public Set<Component> getInternalComponents() {
        return internalComponents;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return String.format("%d:%s", weight, color);
    }
}

class Window {
    private final String name;
    private final Map<Integer, Component> components;

    public Window(String name) {
        this.name = name;
        this.components = new TreeMap<>();
    }

    public void addComponent(int position, Component component) throws InvalidPositionException {
        if (components.containsKey(position))
            throw new InvalidPositionException(String.format("Invalid position %d, alredy taken!", position));

        components.put(position, component);
    }

    public void changeColor(int weight, String color) {
        for (Component component : components.values()) {
            changeColorRecursive(weight, color, component);
        }
    }

    private void changeColorRecursive(int weight, String color, Component component) {
        if (component.getWeight() < weight)
            component.setColor(color);

        Set<Component> set = component.getInternalComponents();

        for (Component internalComponent : set) {
            changeColorRecursive(weight, color, internalComponent);
        }
    }

    public void swichComponents(int pos1, int pos2) {
        Component c1 = components.get(pos1);
        Component c2 = components.get(pos2);

        components.put(pos1, c2);
        components.put(pos2, c1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("WINDOW ").append(name).append("\n");
        components.forEach((k, v) -> sb.append(String.format("%d:%s", k, buildString(v, 0))));

        return sb.toString();
    }

    private String buildString(Component component, int level) {
        StringBuilder sb = new StringBuilder();
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < level * 3; i++) {
            str.append("-");
        }

        sb.append(String.format("%s%s%n", str, component));

        for (Component c : component.getInternalComponents()) {
            sb.append(buildString(c, level + 1));
        }

        return sb.toString();
    }
}