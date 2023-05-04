package VremenskaPrognoza;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

interface Subscriber {
    void setMeasurements(float temperature, float humidity, float pressure);
}

class ForecastDisplay implements Subscriber {
    private float prevPressure;

    public ForecastDisplay(WeatherDispatcher weatherDispatcher) {
        this.prevPressure = 0;
        weatherDispatcher.register(this);
    }

    @Override
    public void setMeasurements(float temperature, float humidity, float pressure) {
        if (pressure > prevPressure) {
            System.out.println("Forecast: Improving");
        } else if (pressure == prevPressure) {
            System.out.println("Forecast: Same");
        } else {
            System.out.println("Forecast: Cooler");
        }

        prevPressure = pressure;
    }
}

class CurrentConditionsDisplay implements Subscriber {
    public CurrentConditionsDisplay(WeatherDispatcher wd) {
        wd.register(this);
    }

    @Override
    public void setMeasurements(float temperature, float humidity, float pressure) {
        System.out.printf("Temperature: %.1fF%nHumidity: %.1f%%%n", temperature, humidity);
    }
}

class WeatherDispatcher {
    private final Set<Subscriber> subscribers;

    public WeatherDispatcher() {
        this.subscribers = new HashSet<>();
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        subscribers.forEach(subscriber -> subscriber.setMeasurements(temperature, humidity, pressure));
        System.out.println();
    }

    public void remove(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void register(Subscriber subscriber) {
        subscribers.add(subscriber);
    }
}

public class WeatherApplication {

    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if (parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if (operation == 1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if (operation == 2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if (operation == 3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if (operation == 4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}