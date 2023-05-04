package StudentskiDosiea;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * January 2016 Exam problem 1
 */
public class StudentRecordsTest {
    public static void main(String[] args) throws IOException {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}

// your code here

class Record {
    private final String code;
    private final List<Integer> grades;

    public Record(String code, List<Integer> grades) {
        this.code = code;
        this.grades = grades;
    }

    public String getCode() {
        return code;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    public double getAverage() {
        return grades.stream().mapToDouble(Integer::intValue).average().orElse(0);
    }

    public int getNumOfTens() {
        return grades.stream().filter(i -> i == 10).mapToInt(Integer::intValue).sum();
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", code, getAverage());
    }
}

class StudentRecords {
    private final Map<String, List<Record>> recordMap;

    public StudentRecords() {
        this.recordMap = new HashMap<>();
    }

    public int readRecords(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line = br.readLine();
        int counter = 1;

        while (line != null) {
            String[] splits = line.split(" ");

            String code = splits[0];
            String direction = splits[1];
            List<Integer> grades = new ArrayList<>();

            for (int i = 2; i < splits.length; i++) {
                grades.add(Integer.parseInt(splits[i]));
            }

            recordMap.putIfAbsent(direction, new ArrayList<>());
            recordMap.get(direction).add(new Record(code, grades));

            line = br.readLine();
            counter++;
        }

        counter--;
        return counter;
    }

    public void writeTable(OutputStream outputStream) {
        recordMap.keySet().forEach(key -> {
            System.out.println(key);
            recordMap.get(key).stream().sorted(Comparator.comparing(Record::getAverage).reversed().thenComparing(Record::getCode)).forEach(System.out::println);
        });
    }

    public void writeDistribution(OutputStream outputStream) {
        recordMap.keySet().forEach(key -> {
            System.out.println(key);

            AtomicInteger six = new AtomicInteger(0);
            AtomicInteger seven = new AtomicInteger(0);
            AtomicInteger eight = new AtomicInteger(0);
            AtomicInteger nine = new AtomicInteger(0);
            AtomicInteger ten = new AtomicInteger(0);

            recordMap.get(key).forEach(record -> record.getGrades().forEach(grade -> {
                switch (grade) {
                    case 6:
                        six.getAndIncrement();
                        break;
                    case 7:
                        seven.getAndIncrement();
                        break;
                    case 8:
                        eight.getAndIncrement();
                        break;
                    case 9:
                        nine.getAndIncrement();
                        break;
                    default:
                        ten.getAndIncrement();
                        break;
                }
            }));

            StringBuilder sb = new StringBuilder();

            sb.append("6 | ");
            for (int i = 0; i < six.get(); i += 10)
                sb.append("*");
            sb.append("(").append(six.get()).append(")\n");

            sb.append("7 | ");
            for (int i = 0; i < seven.get(); i += 10)
                sb.append("*");
            sb.append("(").append(seven.get()).append(")\n");

            sb.append("8 | ");
            for (int i = 0; i < eight.get(); i += 10)
                sb.append("*");
            sb.append("(").append(eight.get()).append(")\n");

            sb.append("9 | ");
            for (int i = 0; i < nine.get(); i += 10)
                sb.append("*");
            sb.append("(").append(nine.get()).append(")\n");

            sb.append("10 | ");
            for (int i = 0; i < ten.get(); i += 10)
                sb.append("*");
            sb.append("(").append(ten.get()).append(")\n");

            System.out.print(sb);
        });
    }
}