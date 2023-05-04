package ArhivaSoLocalDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while (scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch (NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}

class NonExistingItemException extends Exception {
    public NonExistingItemException(String message) {
        super(message);
    }
}

class Archive {
    private final int id;
    private LocalDate dateArchived;

    public Archive(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDateArchived(LocalDate dateArchived) {
        this.dateArchived = dateArchived;
    }
}

class LockedArchive extends Archive {
    // Datum do koj ne smee da se otvori:
    private final LocalDate dateToOpen;

    public LockedArchive(int id, LocalDate dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    public LocalDate getDateToOpen() {
        return dateToOpen;
    }
}

class SpecialArchive extends Archive {
    // Maksimalen broj na dozvoleni otvaranja:
    private final int maxOpen;
    private int counter;

    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
        this.counter = 0;
    }

    public void incrementCounter() {
        this.counter++;
    }

    public int getMaxOpen() {
        return maxOpen;
    }

    public int getCounter() {
        return counter;
    }
}

class ArchiveStore {
    private final List<Archive> archives;
    private final StringBuilder sb;

    public ArchiveStore() {
        this.archives = new ArrayList<>();
        this.sb = new StringBuilder();
    }

    public void archiveItem(Archive item, LocalDate date) {
        item.setDateArchived(date);
        archives.add(item);
        sb.append(String.format("Item %d archived at %s%n", item.getId(), date.toString()));
    }

    public void openItem(int id, LocalDate date) throws NonExistingItemException {
        Archive a = null;

        for (Archive archive : archives) {
            if (archive.getId() == id) {
                a = archive;
                break;
            }
        }

        if (a == null) {
            throw new NonExistingItemException(String.format("Item with id %d doesn't exist", id));
        } else if (a instanceof LockedArchive) {
            if (date.isBefore(((LockedArchive) a).getDateToOpen()))
                sb.append(String.format("Item %d cannot be opened before %s%n", id, ((LockedArchive) a).getDateToOpen().toString()));
            else
                sb.append(String.format("Item %d opened at %s%n", id, date.toString()));
        } else {
            if (((SpecialArchive) a).getCounter() == ((SpecialArchive) a).getMaxOpen())
                sb.append(String.format("Item %d cannot be opened more than %d times%n", id, ((SpecialArchive) a).getMaxOpen()));
            else {
                sb.append(String.format("Item %d opened at %s%n", id, date.toString()));
                ((SpecialArchive) a).incrementCounter();
            }
        }
    }

    public String getLog() {
        return sb.toString();
    }
}