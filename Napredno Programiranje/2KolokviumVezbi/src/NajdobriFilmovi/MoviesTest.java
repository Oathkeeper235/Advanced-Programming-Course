package NajdobriFilmovi;

import java.util.*;
import java.util.stream.Collectors;

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde

class Movie {
    private final String title;
    private final List<Integer> ratings;

    public Movie(String title, int[] ratings) {
        this.ratings = new ArrayList<>();
        this.title = title;
        Arrays.stream(ratings).forEach(i -> getRatings().add(i));
    }

    public String getTitle() {
        return title;
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public int getRatingsAmount() {
        return ratings.size();
    }

    public double getAverageRating() {
        return ratings.stream().mapToInt(Integer::intValue).average().orElse(0);
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings", title, getAverageRating(), getRatingsAmount());
    }
}

class MoviesList {
    private final List<Movie> movies;

    public MoviesList() {
        this.movies = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings) {
        this.movies.add(new Movie(title, ratings));
    }

    public List<Movie> top10ByAvgRating() {
        return movies.stream().sorted(Comparator.comparing(Movie::getAverageRating).reversed().thenComparing(Movie::getTitle)).limit(10).collect(Collectors.toList());
    }

    public double getRatingCoef(Movie movie) {
        return movie.getAverageRating() * movie.getRatingsAmount() / movies.stream().mapToInt(Movie::getRatingsAmount).max().orElse(0);
    }

    // просечен ретјтинг на филмот x вкупно број на рејтинзи на филмот / максимален број на рејтинзи (од сите филмови во листата)
    public List<Movie> top10ByRatingCoef() {
        return movies.stream().sorted(Comparator.comparing(this::getRatingCoef).reversed().thenComparing(Movie::getTitle)).limit(10).collect(Collectors.toList());
    }
}