package GrupaAnagrami;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static boolean isAnagram(String s1, String s2) {
        char[] c1 = s1.toCharArray();
        char[] c2 = s2.toCharArray();

        Arrays.sort(c1);
        Arrays.sort(c2);

        return Arrays.equals(c1, c2);
    }


    public static void findAll(InputStream inputStream) {
        // Vasiod kod ovde
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        Map<String, List<String>> map = new TreeMap<>();

        br.lines().forEach(line -> {
            String key = map.keySet().stream().filter(i -> isAnagram(line, i)).findFirst().orElse(null);

            if (key == null) {
                List<String> list = new ArrayList<>();
                list.add(line);
                map.put(line, list);
            } else {
                map.get(key).add(line);
            }
        });

        //print
        map.values().stream().filter(i -> i.size() >= 5).forEach(i -> System.out.println(String.join(" ", i)));
    }
}
