package MernaStanica;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

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
            ws.addMeasurement(temp, wind, hum, vis, date);
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

class Measurement {
    private final float temperature;
    private final float wind;
    private final float humidity;
    private final float visibility;
    private final Date date;

    public Measurement(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    public float getTemperature() {
        return temperature;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s", temperature, wind, humidity, visibility, date.toString());
    }
}

class WeatherStation {
    private final List<Measurement> measurements;
    private final long days;

    public WeatherStation(int days) {
        this.measurements = new ArrayList<>();
        this.days = (days * 86400000L);
    }

    public void addMeasurement(float temperature, float wind, float humidity, float visibility, Date date) {
        for (Measurement m : measurements) {
            if (Math.abs(date.getTime() - m.getDate().getTime()) < (2.5 * 60000))
                return;
        }

        measurements.removeIf(m -> date.getTime() - m.getDate().getTime() > days);

        measurements.add(new Measurement(temperature, wind, humidity, visibility, date));
    }

    public int total() {
        return measurements.size();
    }

    public void status(Date from, Date to) throws RuntimeException {
        int counter = 0;
        float sum = 0;
        for (Measurement m : measurements) {
            if ((m.getDate().after(from) || m.getDate().equals(from)) && (m.getDate().before(to) || m.getDate().equals(to))) {
                System.out.println(m);
                sum += m.getTemperature();
                counter++;
            }
        }

        if (counter == 0)
            throw new RuntimeException();

        System.out.printf("Average temperature: %.2f", sum / counter);
    }
}