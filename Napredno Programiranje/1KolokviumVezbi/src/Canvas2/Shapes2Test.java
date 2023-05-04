package Canvas2;

import java.io.*;
import java.util.*;

public class Shapes2Test {

    public static void main(String[] args) throws IOException {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}

class IrregularCanvasException extends Exception {
    public IrregularCanvasException(String message) {
        super(message);
    }
}

class Canvas implements Comparable<Canvas> {
    private final String canvas_ID;
    private final List<Square> squares;
    private final List<Circle> circles;

    public Canvas(String canvas_ID, String[] dimensions) {
        this.canvas_ID = canvas_ID;

        this.squares = new ArrayList<>();
        this.circles = new ArrayList<>();

        for (int i = 0; i < dimensions.length; i += 2) {
            if (dimensions[i].equals("S")) {
                squares.add(new Square(Integer.parseInt(dimensions[i + 1])));
            }
            if (dimensions[i].equals("C")) {
                circles.add(new Circle(Integer.parseInt(dimensions[i + 1])));
            }
        }
    }

    public String getCanvas_ID() {
        return canvas_ID;
    }

    public int total_shapes() {
        return squares.size() + circles.size();
    }

    public int total_circles() {
        return circles.size();
    }

    public int total_squares() {
        return squares.size();
    }

    public double getTotalArea() {
        return squares.stream().mapToDouble(Square::getArea).sum() +
                circles.stream().mapToDouble(Circle::getArea).sum();
    }

    public double minArea() {
        double s, c;
        try {
            s = Collections.min(squares).getArea();
        } catch (NoSuchElementException e) {
            return Collections.min(circles).getArea();
        }
        try {
            c = Collections.min(circles).getArea();
        } catch (NoSuchElementException e) {
            return Collections.min(squares).getArea();
        }
        return Math.min(s, c);
    }

    public double maxArea() {
        double s, c;
        try {
            s = Collections.max(squares).getArea();
        } catch (NoSuchElementException e) {
            return Collections.max(circles).getArea();
        }
        try {
            c = Collections.max(circles).getArea();
        } catch (NoSuchElementException e) {
            return Collections.max(squares).getArea();
        }
        return Math.max(s, c);
    }

    public double averageArea() {
        return getTotalArea() / (squares.size() + circles.size());
    }

    @Override
    public int compareTo(Canvas o) {
        return Double.compare(getTotalArea(), o.getTotalArea());
    }
}

class Square implements Comparable<Square> {
    private final int a;

    public Square(int a) {
        this.a = a;
    }

    public double getArea() {
        return a * a;
    }

    @Override
    public int compareTo(Square o) {
        return Double.compare(getArea(), o.getArea());
    }
}

class Circle implements Comparable<Circle> {
    private final int r;

    public Circle(int r) {
        this.r = r;
    }

    public double getArea() {
        return Math.PI * r * r;
    }

    @Override
    public int compareTo(Circle o) {
        return Double.compare(getArea(), o.getArea());
    }
}

class ShapesApplication {
    private final List<Canvas> canvasList;
    // Najgolema dozvolena ploshtina na sekoja forma poedinecno:
    private final double maxArea;

    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
        this.canvasList = new ArrayList<>();
    }

    private void addCanvas(Canvas c) throws IrregularCanvasException {
        if (c.maxArea() > maxArea)
            throw new IrregularCanvasException(String.format("Canvas %s has a shape with area larger than %.2f", c.getCanvas_ID(), maxArea));
        canvasList.add(c);
    }

    public void readCanvases(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String input = br.readLine();

        while (input != null) {
            String id = input.substring(0, 8);
            String dimensions = input.substring(9);
            String[] splits = dimensions.split("\\s+");

            try {
                addCanvas(new Canvas(id, splits));
            } catch (IrregularCanvasException e) {
                System.out.println(e.getMessage());
            }

            input = br.readLine();
        }
    }

    public void printCanvases(PrintStream printStream) {
        canvasList.sort(Collections.reverseOrder());
        for (Canvas c : canvasList) {
            printStream.printf("%s %d %d %d %.2f %.2f %.2f\n", c.getCanvas_ID(), c.total_shapes(), c.total_circles(), c.total_squares(),
                    c.minArea(), c.maxArea(), c.averageArea());
        }
    }
}