package Aerodromi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde

class Flight {
    private final String from;
    private final String to;
    private final int time;
    private final int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    @Override
    public String toString() {
        int end = time + duration;
        return String.format("%s-%s %02d:%02d-%02d:%02d %s%dh%02dm", from, to, time / 60, time % 60, (end / 60) % 24, end % 60, (end / 60) / 24 > 0 ? "+1d " : "", duration / 60, duration % 60);
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }
}

class Airport {
    private final String name;
    private final String country;
    private final String code;
    private final int passengers;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)%n%s%n%d", name, code, country, passengers);
    }

    public String getCode() {
        return code;
    }
}

class Airports {
    private final List<Airport> airports;
    private final List<Flight> flights;

    public Airports() {
        this.airports = new ArrayList<>();
        this.flights = new ArrayList<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        airports.add(new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        flights.add(new Flight(from, to, time, duration));
    }

    public void showFlightsFromAirport(String code) {
        airports.stream().filter(i -> i.getCode().equals(code)).forEach(System.out::println);

        List<Flight> flightsSorted = flights.stream().filter(i -> i.getFrom().equals(code)).sorted(Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime)).collect(Collectors.toList());

        for (int i = 0; i < flightsSorted.size(); i++) {
            System.out.print(i + 1 + ". ");
            System.out.println(flightsSorted.get(i));
        }
    }

    public void showDirectFlightsFromTo(String from, String to) {
        List<Flight> flightsSorted = flights.stream().filter(i -> i.getFrom().equals(from) && i.getTo().equals(to)).sorted(Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime)).collect(Collectors.toList());
        if (flightsSorted.isEmpty())
            System.out.println("No flights from " + from + " to " + to);
        else
            flightsSorted.forEach(System.out::println);
    }

    public void showDirectFlightsTo(String to) {
        flights.stream().filter(i -> i.getTo().equals(to)).sorted(Comparator.comparing(Flight::getTime).thenComparing(Flight::getDuration)).forEach(System.out::println);
    }
}
