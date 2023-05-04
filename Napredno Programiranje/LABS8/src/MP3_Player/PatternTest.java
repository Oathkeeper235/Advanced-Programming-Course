package MP3_Player;

import java.util.ArrayList;
import java.util.List;

public class PatternTest {
    public static void main(String[] args) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}

// Vasiot kod ovde

class Song {
    private final String title;
    private final String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title=" + title +
                ", artist=" + artist +
                '}';
    }
}

class MP3Player {
    private final List<Song> songs;
    private int currentIndex;
    private boolean paused;
    private boolean stopped;

    public MP3Player(List<Song> songs) {
        this.songs = songs;
        this.currentIndex = 0;
        this.paused = true;
        this.stopped = false;
    }

    public void pressPlay() {
        if (paused) {
            System.out.println("Song " + currentIndex + " is playing");
            paused = false;
            stopped = false;
        } else {
            System.out.println("Song is already playing");
        }
    }

    public void pressStop() {
        if (stopped) {
            System.out.println("Songs are already stopped");
            currentIndex = 0;
        }

        if (!stopped && paused) {
            System.out.println("Songs are stopped");
            currentIndex = 0;
            stopped = true;
        }

        if (!paused && !stopped) {
            System.out.println("Song " + currentIndex + " is paused");
            paused = true;
        }
    }

    public void pressFWD() {
        System.out.println("Forward...");

        paused = true;
        if (currentIndex + 1 != songs.size())
            currentIndex++;
        else
            currentIndex = 0;
    }

    public void pressREW() {
        System.out.println("Reward...");

        paused = true;
        if (currentIndex - 1 != -1)
            currentIndex--;
        else
            currentIndex = songs.size() - 1;
    }

    public void printCurrentSong() {
        System.out.println("Song{title=" + songs.get(currentIndex).getTitle()
                + ", artist=" + songs.get(currentIndex).getArtist() + "}");
    }

    @Override
    public String toString() {
        return "MP3Player{" +
                "currentSong = " + currentIndex +
                ", songList = " + songs +
                "}";
    }
}