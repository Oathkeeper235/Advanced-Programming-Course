package VleznaZadaca;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NumbersProcessorTest {

    public static void main(String[] args) throws IOException {


        NumberProcessor numberProcessor = new NumberProcessor();

        numberProcessor.readRows(System.in);

        numberProcessor.printMaxFromRows(System.out);


    }
}

class Numbers {
    private final List<Integer> numbers;

    public Numbers() {
        this.numbers = new ArrayList<>();
    }

    public Numbers(List<Integer> numbers) {
        this.numbers = numbers;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public int findMax() {
        boolean bool = true;
        int max = -100000;
        for (Integer integer : numbers) {
            if (integer >= max)
                max = integer;
        }
        int counter = 0;
        for (Integer number : numbers) {
            if (number == max)
                counter++;
        }
        int secondCounter = 0;
        for (int i = 0; i < numbers.size(); i++) {
            for (Integer number : numbers) {
                if (numbers.get(i).equals(number))
                    secondCounter++;
            }
            if (secondCounter > counter) {
                bool = false;
                break;
            }

            secondCounter = 0;
        }

        if (bool)
            return max;

        return 0;
    }
}

class NumberProcessor {
    private final List<Numbers> rows;

    public NumberProcessor() {
        this.rows = new ArrayList<>();
    }

    public void readRows(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = br.readLine();

        while (line != null) {
            String[] splits = line.split(" ");
            List<Integer> numbers = new ArrayList<>();
            for (String split : splits) {
                numbers.add(Integer.parseInt(split));
            }
            rows.add(new Numbers(numbers));
            line = br.readLine();
        }
    }

    public void printMaxFromRows(OutputStream os) {
        rows.forEach(row -> {
            if (row.findMax() != 0)
                System.out.println(row.findMax());
        });
    }
}