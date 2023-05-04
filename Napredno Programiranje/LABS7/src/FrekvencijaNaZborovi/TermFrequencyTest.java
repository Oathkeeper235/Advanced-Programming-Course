package FrekvencijaNaZborovi;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[]{"во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја"};
        TermFrequency tf = new TermFrequency(System.in, stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde

class TermFrequency {

    private final HashMap<String, Integer> hashMap;
    private int brojac;

    public TermFrequency() {
        this.hashMap = new HashMap<>();
        this.brojac = 0;
    }

    public TermFrequency(InputStream inputStream, String[] stopWords) {
        Scanner sc = new Scanner(inputStream);

        this.hashMap = new HashMap<>();
        this.brojac = 0;

        while (sc.hasNext()) {
            String word = sc.next();
            word = word
                    .toLowerCase()
                    .replaceAll("[,.]", "")
                    .trim();

            if (!Arrays.asList(stopWords).contains(word) && !word.isEmpty()) {
                hashMap.computeIfPresent(word, (k, v) -> ++v);
                hashMap.putIfAbsent(word, 1);

                brojac++;
            }
        }
    }

    public int countTotal() {
        return brojac;
    }

    public int countDistinct() {
        return hashMap.size();
    }

    public List<String> mostOften(int k) {
        return hashMap
                .keySet()
                .stream()
                .sorted(Comparator.comparing(hashMap::get).reversed().thenComparing(Object::toString))
                .limit(k)
                .collect(Collectors.toList());
    }
}