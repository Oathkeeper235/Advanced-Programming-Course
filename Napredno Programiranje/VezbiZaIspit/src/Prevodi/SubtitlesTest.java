package Prevodi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SubtitlesTest {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(br);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

// Вашиот код овде

class Subtitle {
    private final int number;
    private String startTime;
    private String endTime;
    private final String text;

    public Subtitle(int number, String startTime, String endTime, String text) {
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = text;
    }

    @Override
    public String toString() {
        return number + "\n" +
                startTime + " --> " + endTime + "\n" +
                text + "\n";
    }

    public void changeTime(int ms) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss,SSS");

        Date time1 = formatter.parse(startTime);
        time1.setTime(time1.getTime() + ms);
        startTime = formatter.format(time1);

        Date time2 = formatter.parse(endTime);
        time2.setTime(time2.getTime() + ms);
        endTime = formatter.format(time2);

    }
}

class Subtitles {
    private final List<Subtitle> subtitles;

    public Subtitles() {
        this.subtitles = new ArrayList<>();
    }

    public int loadSubtitles(BufferedReader br) throws IOException {
        int counter = 0;
        String input = br.readLine();

        while (input != null) {
            int N = Integer.parseInt(input);
            input = br.readLine();
            String[] splits = input.split(" --> ");
            input = br.readLine();
            StringBuilder text = new StringBuilder();
            text.append(input);

            input = br.readLine();
            while (input != null && !input.equals("") && !input.equals(" ")) {
                text.append("\n").append(input);
                input = br.readLine();
            }

            subtitles.add(new Subtitle(N, splits[0], splits[1], text.toString()));

            counter++;
            input = br.readLine();
        }

        return counter;
    }

    public void print() {
        subtitles.forEach(System.out::println);
    }

    public void shift(int ms) {
        subtitles.forEach(i -> {
            try {
                i.changeTime(ms);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }
}