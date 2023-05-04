package PrvKolokviumTermin1Prva;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LineProcessorTest {
    public static void main(String[] args) {
        LineProcessor lineProcessor = new LineProcessor();

        try {
            lineProcessor.readLines(System.in, System.out, 'a');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Line {
    private final List<String> strings;

    public Line(List<String> strings) {
        this.strings = strings;
    }

    public int countC(char c) {
        AtomicInteger counter = new AtomicInteger(0);
        strings.forEach(string -> {
            char[] chars = string.toCharArray();
            for (char aChar : chars) {
                if (Character.toLowerCase(aChar) == c)
                    counter.getAndIncrement();
            }
        });

        return counter.get();
    }

    public List<String> getStrings() {
        return strings;
    }
}

class LineProcessor {
    private final List<Line> strings;

    public LineProcessor() {
        this.strings = new ArrayList<>();
    }

    public void readLines (InputStream is, OutputStream os, char c) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = br.readLine();

        while (line != null) {
            String[] splits = line.split(" ");
            strings.add(new Line(Arrays.asList(splits)));
            line = br.readLine();
        }

        Line max = strings.get(0);
        for (int i = 1; i < strings.size(); i++) {
            if (max.countC(c) <= strings.get(i).countC(c))
                max = strings.get(i);
        }

        max.getStrings().forEach(i -> System.out.print(i + " "));
    }
}
