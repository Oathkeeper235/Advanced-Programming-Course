package MernaStanica;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde

class WeatherCondition implements Comparable<WeatherCondition> {
    private final float temperature;
    private final float wind;
    private final float humidity;
    private final float visibility;
    private final Date date;

    public WeatherCondition(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    @Override
    public int compareTo(WeatherCondition o) {
        if (Math.abs(date.getTime() - o.date.getTime()) < 150000)
            return 0;
        else return date.compareTo(o.date);
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s",
                temperature, wind, humidity, visibility, date.toString());
    }

    public float getTemperature() {
        return temperature;
    }

    public Date getDate() {
        return date;
    }
}

class WeatherStation {
    private final List<WeatherCondition> weatherConditions;
    private final int days;
    public static final long MS = 86400000;

    public WeatherStation(int days) {
        this.days = days;
        this.weatherConditions = new ArrayList<>();
    }

    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date) {
        WeatherCondition wc = new WeatherCondition(temperature, wind, humidity, visibility, date);

        for (WeatherCondition w : weatherConditions) {
            if (w.compareTo(wc) == 0)
                return;
        }

        weatherConditions.removeIf(i -> wc.getDate().getTime() - i.getDate().getTime() > days * MS);

        weatherConditions.add(wc);
    }

    public int total() {
        return weatherConditions.size();
    }

    public void status(Date from, Date to) {
        List<WeatherCondition> list = new ArrayList<>();

        for (WeatherCondition wc : weatherConditions) {
            if ((wc.getDate().after(from) || wc.getDate().equals(from)) && (wc.getDate().before(to) || wc.getDate().equals(to)))
                list.add(wc);
        }

        if (list.isEmpty())
            throw new RuntimeException();

        for (WeatherCondition wc : list)
            System.out.println(wc.toString());

        double average = list.stream()
                .mapToDouble(WeatherCondition::getTemperature).average().getAsDouble();

        System.out.printf("Average temperature: %.2f", average);
    }
}