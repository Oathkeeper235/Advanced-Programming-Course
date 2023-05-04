package GenerickiKlaster;

import java.util.*;
import java.util.stream.Collectors;

/**
 * January 2016 Exam problem 2
 */

public class ClusterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}

// your code here

class Point2D {
    private final long id;
    private final float x;
    private final float y;

    public Point2D(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public long getId() {
        return id;
    }

    public float Distance(Point2D p) {
        return (float) Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
    }

    @Override
    public String toString() {
        return String.format("%d", id);
    }
}

class WrappedElement<T extends Point2D> {
    public T element;
    public float distance;

    public WrappedElement(T element, float distance) {
        this.element = element;
        this.distance = distance;
    }

    public float getDistance() {
        return distance;
    }
}

class Cluster<T extends Point2D> {
    private final Map<Long, T> elements;

    public Cluster() {
        this.elements = new HashMap<>();
    }

    public void addItem(T element) {
        elements.put(element.getId(), element);
    }

    public void near(long id, int top) {
        T target = elements.get(id);

        List<WrappedElement> ls = elements.values().stream()
                .filter(x -> x.getId() != target.getId())
                .map(x -> new WrappedElement(x, x.Distance(target)))
                .sorted(Comparator.comparing(WrappedElement::getDistance))
                .limit(top)
                .collect(Collectors.toList());

        for (int i = 0; i < ls.size(); i++) {
            System.out.format("%d. %s -> %.3f\n", i + 1, ls.get(i).element, ls.get(i).distance);
        }
    }
}