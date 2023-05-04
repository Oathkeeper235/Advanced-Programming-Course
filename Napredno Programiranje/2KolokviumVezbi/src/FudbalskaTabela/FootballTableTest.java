package FudbalskaTabela;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Partial exam II 2016/2017
 */
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here

class FootballTeam {
    private final String name;
    private int wins;
    private int draws;
    private int losses;
    private int goalsScored;
    private int goalsTaken;

    public FootballTeam(String name, int goalsScored, int goalsTaken, int wins, int draws, int losses) {
        this.name = name;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.goalsScored = goalsScored;
        this.goalsTaken = goalsTaken;
    }

    public void addWins(int number) {
        wins += number;
    }

    public void addDraws(int number) {
        draws += number;
    }

    public void addLosses(int number) {
        losses += number;
    }

    public void addGoals(int number1, int number2) {
        goalsScored += number1;
        goalsTaken += number2;
    }

    public int totalPlays() {
        return wins + draws + losses;
    }

    public int getGoalDifference() {
        return goalsScored - goalsTaken;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return wins * 3 + draws;
    }

    @Override
    public String toString() {
        // odigrani natprevari, pobedi, neresheni, izgubeni, osvoeni poeni
        return String.format("%-15s%5d%5d%5d%5d%5d", name, totalPlays(), wins, draws, losses, getPoints());
    }
}

class FootballTable {
    private final Map<String, FootballTeam> teams;

    public FootballTable() {
        this.teams = new HashMap<>();
    }

    private void checkGame(String team, int homeGoals, int awayGoals) {
        if (!teams.containsKey(team)) {
            if (homeGoals > awayGoals)
                teams.put(team, new FootballTeam(team, homeGoals, awayGoals, 1, 0, 0));
            else if (homeGoals == awayGoals)
                teams.put(team, new FootballTeam(team, homeGoals, awayGoals, 0, 1, 0));
            else
                teams.put(team, new FootballTeam(team, homeGoals, awayGoals, 0, 0, 1));
        } else {
            teams.get(team).addGoals(homeGoals, awayGoals);
            if (homeGoals > awayGoals)
                teams.get(team).addWins(1);
            else if (homeGoals == awayGoals)
                teams.get(team).addDraws(1);
            else
                teams.get(team).addLosses(1);
        }
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        checkGame(homeTeam, homeGoals, awayGoals);
        checkGame(awayTeam, awayGoals, homeGoals);
    }

    public void printTable() {
        List<FootballTeam> list = teams.values().stream()
                .sorted(Comparator.comparing(FootballTeam::getPoints).thenComparing(FootballTeam::getGoalDifference).reversed().thenComparing(FootballTeam::getName))
                .collect(Collectors.toList());

        list.forEach(i -> System.out.printf("%2d. %s%n", list.indexOf(i) + 1, i.toString()));
    }
}