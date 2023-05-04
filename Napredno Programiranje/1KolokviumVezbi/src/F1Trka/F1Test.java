package F1Trka;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class LapTime implements Comparable<LapTime> {

    private final int mm;
    private final int ss;
    private final int nnn;

    public LapTime(String str) {
        String[] laps = str.split(":");
        mm = Integer.parseInt(laps[0]);
        ss = Integer.parseInt(laps[1]);
        nnn = Integer.parseInt(laps[2]);
    }

    @Override
    public int compareTo(LapTime o) {
        if (mm != o.mm)
            return mm - o.mm;
        if (ss != o.ss)
            return ss - o.ss;
        if (nnn != o.nnn)
            return nnn - o.nnn;

        return 0;
    }

    @Override
    public String toString() {
        return String.format("%01d:%02d:%03d", mm, ss, nnn);
    }
}

class F1Pilot implements Comparable<F1Pilot> {

    private final String pilotName;
    private final List<LapTime> laps;

    public F1Pilot(String str) {
        this.laps = new ArrayList<LapTime>();
        String[] splits = str.split("\\s+");

        this.pilotName = splits[0];
        this.laps.add(new LapTime(splits[1]));
        this.laps.add(new LapTime(splits[2]));
        this.laps.add(new LapTime(splits[3]));
    }

    public LapTime bestTime() {
        return Collections.min(laps);
    }

    @Override
    public int compareTo(F1Pilot o) {
        return bestTime().compareTo(o.bestTime());
    }

    @Override
    public String toString() {
        return String.format("%-10s%10s", pilotName, bestTime().toString());
    }
}

class F1Race {
    // vashiot kod ovde
    private final List<F1Pilot> pilots;

    public F1Race() {
        this.pilots = new ArrayList<F1Pilot>();
    }


    public void readResults(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()) {
            F1Pilot pilot = new F1Pilot(scanner.nextLine());
            this.pilots.add(pilot);
        }
    }

    public void printSorted(PrintStream out) {
        Collections.sort(pilots);

        for (int i = 0; i < pilots.size(); i++) {
            out.printf("%d. %s%n", i + 1, pilots.get(i).toString());
        }
    }
}