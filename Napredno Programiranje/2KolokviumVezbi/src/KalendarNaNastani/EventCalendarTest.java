package KalendarNaNastani;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}

// vashiot kod ovde

class WrongDateException extends Exception {
    public WrongDateException(String message) {
        super("Wrong date: " + message);
    }
}

class Event implements Comparable<Event> {
    private final String name;
    private final String location;
    private final Date date;

    public Event(String name, String location, Date date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public static int getKey(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    @Override
    public int compareTo(Event o) {
        int compare = date.compareTo(o.date);

        return compare == 0 ? name.compareTo(o.name) : compare;
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("dd MMM, yyyy HH:mm");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        return String.format("%s at %s, %s", df.format(date), location, name);
    }
}

class EventCalendar {
    private final int year;
    private final HashMap<Integer, TreeSet<Event>> events;
    private final HashMap<Integer, Integer> eventsCount;

    public EventCalendar(int year) {
        this.year = year;
        this.events = new HashMap<>();
        this.eventsCount = new HashMap<>();

        IntStream.range(1, 13).forEach(i -> eventsCount.put(i, 0));
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (calendar.get(Calendar.YEAR) != year) {
            DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyy");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));

            throw new WrongDateException(df.format(date));
        }

        Event event = new Event(name, location, date);
        int key = Event.getKey(date);

        TreeSet<Event> eventsValue = events.computeIfAbsent(key, k -> new TreeSet<>());
        eventsValue.add(event);
        eventsCount.computeIfPresent(calendar.get(Calendar.MONTH) + 1, (k, v) -> v + 1);
    }

    public void listEvents(Date date) {
        TreeSet<Event> eventsValue = events.get(Event.getKey(date));

        if (eventsValue != null) {
            eventsValue.forEach(System.out::println);
        } else {
            System.out.println("No events on this day!");
        }
    }

    public void listByMonth() {
        eventsCount.forEach((k, v) -> System.out.printf("%d : %d%n", k, v));
    }
}