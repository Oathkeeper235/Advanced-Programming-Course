package BlokovskaStruktura;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BlockContainerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for (int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);
        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);
        BlockContainer<String> stringBC = new BlockContainer<>(size);
        String lastString = null;
        for (int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}

// Вашиот код овде

class BlockContainer<T extends Comparable<T>> {
    private final List<TreeSet<T>> container;
    private final int maxSize;

    public BlockContainer(int n) {
        this.container = new ArrayList<>();
        this.maxSize = n;
    }

    public void add(T a) {
        if (container.isEmpty()) {
            TreeSet<T> newBlock = new TreeSet<>();
            newBlock.add(a);
            container.add(newBlock);
        } else if (container.get(container.size() - 1).size() < maxSize) {
            container.get(container.size() - 1).add(a);
        } else {
            TreeSet<T> newBlock = new TreeSet<>();
            newBlock.add(a);
            container.add(newBlock);
        }
    }

    public boolean remove(T a) {
        TreeSet<T> block = container.get(container.size() - 1);
        boolean removed = block.remove(a);
        if (block.isEmpty()) {
            container.remove(block);
        }

        return removed;
    }

    public void sort() {
        List<T> elements = new ArrayList<>();

        container.forEach(elements::addAll);

        elements = elements.stream().sorted().collect(Collectors.toList());

        container.clear();

        elements.forEach(this::add);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        AtomicInteger i = new AtomicInteger(0);

        container.forEach(block -> {
            sb.append("[");
            block.forEach(x -> {
                if (block.last().equals(x)) {
                    sb.append(x);
                } else sb.append(x).append(", ");
            });
            i.getAndIncrement();
            if (i.get() == container.size())
                sb.append("]");
            else sb.append("],");
        });

        return sb.toString();
    }
}