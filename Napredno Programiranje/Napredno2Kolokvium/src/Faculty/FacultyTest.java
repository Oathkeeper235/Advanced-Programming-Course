package Faculty;

import java.util.*;

public class FacultyTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Faculty faculty = new Faculty();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s++");
            if (parts[0].equals("addInfo")) {
                String courseId = parts[1];
                String studentId = parts[2];
                int totalPoints = Integer.parseInt(parts[3]);
                faculty.addInfo(courseId, studentId, totalPoints);
            } else if (parts[0].equals("printCourseReport")) {
                String courseId = parts[1];
                String comparator = parts[2];
                boolean descending = Boolean.parseBoolean(parts[3]);
                faculty.printCourseReport(courseId, comparator, descending);
            } else if (parts[0].equals("printStudentReport")) { //printStudentReport
                String studentId = parts[1];
                faculty.printStudentReport(studentId);
            } else {
                String courseId = parts[1];
                Map<Integer, Integer> grades = faculty.gradeDistribution(courseId);
                grades.forEach((key, value) -> System.out.println(String.format("%2d -> %3d", key, value)));
            }
        }
    }
}

class Student {
    private final String index;
    private final int totalPoints;

    public Student(String index, int totalPoints) {
        this.index = index;
        this.totalPoints = totalPoints;
    }

    public String getIndex() {
        return index;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getGrade() {
        if (totalPoints >= 50 && totalPoints < 60) {
            return 6;
        }
        if (totalPoints >= 60 && totalPoints < 70) {
            return 7;
        }
        if (totalPoints >= 70 && totalPoints < 80) {
            return 8;
        }
        if (totalPoints >= 80 && totalPoints < 90) {
            return 9;
        }
        if (totalPoints >= 90 && totalPoints < 100) {
            return 10;
        }

        return 5;
    }

    @Override
    public String toString() {
        return String.format("%s %d (%d)", index, totalPoints, getGrade());
    }
}

class Faculty {
    private final Map<String, List<Student>> map;

    public Faculty() {
        this.map = new HashMap<>();
    }

    public void addInfo(String courseId, String studentId, int totalPoints) {
        List<Student> list = new ArrayList<>();
        map.putIfAbsent(courseId, list);
        map.get(courseId).add(new Student(studentId, totalPoints));
    }

    public void printCourseReport(String courseId, String comparator, boolean descending) {
        if (comparator.equals("byId")) {
            if (!descending) {
                map.get(courseId).stream().sorted(Comparator.comparing(Student::getIndex)).forEach(System.out::println);
            } else {
                map.get(courseId).stream().sorted(Comparator.comparing(Student::getIndex).reversed()).forEach(System.out::println);
            }
        } else {
            if (!descending) {
                map.get(courseId).stream().sorted(Comparator.comparing(Student::getGrade).thenComparing(Student::getTotalPoints).thenComparing(Student::getIndex)).forEach(System.out::println);
            } else {
                map.get(courseId).stream().sorted(Comparator.comparing(Student::getGrade).thenComparing(Student::getTotalPoints).thenComparing(Student::getIndex).reversed()).forEach(System.out::println);
            }
        }
    }

    public int maxPointsForSubject(String string, String id) {
        return map.get(string).stream().filter(i -> i.getIndex().equals(id)).max(Comparator.comparing(Student::getTotalPoints)).get().getTotalPoints();
    }

    public void printStudentReport(String studentId) {
        map.entrySet().stream().filter(i -> i.getValue().stream().anyMatch(s -> s.getIndex().equals(studentId))).sorted(Comparator.comparing(String::valueOf)).forEach(stringListEntry -> System.out.printf("%s %d (%d)%n", stringListEntry.getKey(), maxPointsForSubject(stringListEntry.getKey(), studentId), stringListEntry.getValue().stream().filter(i -> i.getIndex().equals(studentId)).max(Comparator.comparing(Student::getGrade)).get().getGrade()));
    }

    Map<Integer, Integer> gradeDistribution(String courseId) {
        Map<Integer, Integer> resultMap = new HashMap<>();

        for (int k = 5; k <= 10; k++) {
            int finalK = k;
            resultMap.put(finalK, Math.toIntExact(map.get(courseId).stream().filter(i -> i.getGrade() == finalK).count()));
        }

        for (int i = 5; i <= 10; i++) {
            if (resultMap.get(i) == 0)
                resultMap.remove(i);
        }

        return resultMap;
    }
}
