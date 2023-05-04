package LineProcessor;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Line {
    private final char[] line;
    private final char c;
    private String string;

    public Line() {
        this.line = new char[50];
        this.c = 'c';
    }

    public Line(String line, char c) {
        this.line = line.toCharArray();
        this.c = c;
        string = line;
    }

    public int cCounter() {
        int counter = 0;
        for (char value : this.line) {
            if (value == this.c)
                counter++;
        }
        return counter;
    }

    @Override
    public String toString() {
        return string;
    }
}

class LineProcessor {
    private final List<Line> lines;

    public LineProcessor() {
        this.lines = new ArrayList<Line>();
    }

    void readLines(InputStream is, PrintStream os, char c) {
        Scanner scanner = new Scanner(is);
        String str;

        while (scanner.hasNextLine()) {
            str = scanner.nextLine();
            this.lines.add(new Line(str, c));
        }

        Line max = lines.get(0);
        for (Line line : lines) {
            if (max.cCounter() <= line.cCounter())
                max = line;
        }
        os.println(max.toString());
    }
}

public class LineProcessorTest {
    public static void main(String[] args) {
        LineProcessor lineProcessor = new LineProcessor();

        lineProcessor.readLines(System.in, System.out, 'a');
    }
}