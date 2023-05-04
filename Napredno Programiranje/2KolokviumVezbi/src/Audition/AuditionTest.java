package Audition;

import java.util.*;

class Candidate implements Comparable<Candidate> {
    private final String city;
    private final String code;
    private final String name;
    private final int age;

    public Candidate(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    @Override
    public int compareTo(Candidate c) {
        if (name.equals(c.name)) {
            if (age == c.age)
                return code.compareTo(c.code);

            return Integer.compare(age, c.age);
        }

        return name.compareTo(c.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Candidate c = (Candidate) obj;
        return city.equals(c.city) && code.equals(c.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, code);
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }
}

class Audition {
    private final Set<Candidate> candidates;

    public Audition() {
        this.candidates = new HashSet<>();
    }

    public void addParticpant(String city, String code, String name, int age) {
        candidates.add(new Candidate(city, code, name, age));
    }

    public void listByCity(String city) {
        candidates.stream().sorted().filter(i -> i.getCity().equals(city)).forEach(System.out::println);
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}