package Unikatniiminja;

import java.util.*;

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde


class Names {
    private final Map<String, Integer> map;

    public Names() {
        this.map = new TreeMap<>();
    }

    public void addName(String name) {
        map.putIfAbsent(name, 0);
        map.compute(name, (k, v) -> v + 1);
    }

    private static int uniqueLetters(String s) {
        Set<Character> set = new HashSet<>();

        for (char c : s.toLowerCase().toCharArray()) {
            set.add(c);
        }

        return set.size();
    }

    public void printN(int n) {
        map.entrySet().stream().filter(i -> i.getValue() >= n).forEach(i -> System.out.printf("%s (%d) %d%n", i.getKey(), i.getValue(), uniqueLetters(i.getKey())));
    }

    public String findName(int len, int x) {
        Map<String, Integer> treeMap = new TreeMap<>();
        map.entrySet().stream().filter(i -> i.getKey().length() < len).forEach(i -> treeMap.put(i.getKey(), i.getValue()));

        int i = 0;
        for (String str : treeMap.keySet()) {
            if (x % treeMap.size() == i)
                return str;

            i++;
        }

        return null;
    }
}