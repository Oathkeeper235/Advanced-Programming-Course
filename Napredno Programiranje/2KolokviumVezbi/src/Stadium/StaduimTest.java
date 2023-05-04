package Stadium;

import java.util.*;

class SeatTakenException extends Exception {
    public SeatTakenException() {
        super();
    }
}

class SeatNotAllowedException extends Exception {
    public SeatNotAllowedException() {
        super();
    }
}

class Sector {
    private final String code;
    private final int numOfSeats;
    private final Map<Integer, Boolean> map;
    private int type;

    public Sector(String code, int numOfSeats) {
        this.type = 0;
        this.map = new HashMap<>();
        this.code = code;
        this.numOfSeats = numOfSeats;
    }

    public String getCode() {
        return code;
    }

    public Map<Integer, Boolean> getMap() {
        return map;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFreeSeats() {
        return numOfSeats - map.size();
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", code, getFreeSeats(), numOfSeats, ((double) map.size() / numOfSeats) * 100.0);
    }
}

class Stadium {
    private final Map<String, Sector> sectorMap;

    public Stadium() {
        this.sectorMap = new HashMap<>();
    }

    public void createSectors(String[] sectorNames, int[] sizes) {
        for (int i = 0; i < sectorNames.length; i++) {
            sectorMap.put(sectorNames[i], new Sector(sectorNames[i], sizes[i]));
        }
    }

    public void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        Map<Integer, Boolean> seatsMap = sectorMap.get(sectorName).getMap();
        int sectorType = sectorMap.get(sectorName).getType();

        if (seatsMap.containsKey(seat))
            throw new SeatTakenException();
        if ((sectorType == 1 && type == 2) || (type == 1 && sectorType == 2))
            throw new SeatNotAllowedException();

        if (type != 0 && sectorType == 0)
            sectorMap.get(sectorName).setType(type);

        seatsMap.put(seat, true);
    }

    public void showSectors() {
        sectorMap.values().stream().sorted(Comparator.comparing(Sector::getFreeSeats).reversed().thenComparing(Sector::getCode)).forEach(System.out::println);
    }
}

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium();
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}
