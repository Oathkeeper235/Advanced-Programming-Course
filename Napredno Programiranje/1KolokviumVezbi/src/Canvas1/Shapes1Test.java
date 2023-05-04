package Canvas1;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shapes1Test {

    public static void main(String[] args) throws IOException {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}

class Canvas {
    public final String canvas_ID;
    public final String[] dimensions;

    public Canvas(String canvas_ID, String[] dimensions) {
        this.canvas_ID = canvas_ID;
        this.dimensions = dimensions;
    }

    public int perimeter() {
        int L = 0;
        for (String dimension : dimensions) {
            L += Integer.parseInt(dimension) * 4;
        }

        return L;
    }
}

class ShapesApplication {

    private final List<Canvas> canvasList;
    private int counter;

    public ShapesApplication() {
        this.canvasList = new ArrayList<>();
        this.counter = 0;
    }

    public int readCanvases(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String input = br.readLine();

        while (input != null) {
            String id = input.substring(0, 8);
            String dimensions = input.substring(9);
            String[] splits = dimensions.split("\\s+");

            canvasList.add(new Canvas(id, splits));

            input = br.readLine();
            counter += splits.length;
        }

        return counter;
    }

    public Canvas maxPerimeter() {
        Canvas max = canvasList.get(0);
        for (int i = 1; i < canvasList.size(); i++) {
            if (canvasList.get(i).perimeter() > max.perimeter())
                max = canvasList.get(i);
        }
        return max;
    }

    public void printLargestCanvasTo(PrintStream printStream) {
        printStream.printf("%s %d %d", maxPerimeter().canvas_ID,
                maxPerimeter().dimensions.length, maxPerimeter().perimeter());
    }
}