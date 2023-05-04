package Fakultet;

import java.util.*;
import java.util.stream.Collectors;

class OperationNotAllowedException extends Exception {
    public OperationNotAllowedException(String ID, int term) {
        super(String.format("Student %s already has 3 grades in term %d", ID, term));
    }

    public OperationNotAllowedException(int term, String ID) {
        super(String.format("Term %d is not possible for student with ID %s", term, ID));
    }
}

class Grade {
    private final int term;
    private final String course;
    private final int grade;

    public Grade(int term, String course, int grade) {
        this.term = term;
        this.course = course;
        this.grade = grade;
    }

    public String getCourse() {
        return course;
    }

    public int getGrade() {
        return grade;
    }
}

class Student {
    private final String id;
    private final int yearsOfStudies;
    private final Map<Integer, List<Grade>> grades;

    public Student(String id, int yearsOfStudies) {
        this.id = id;
        this.yearsOfStudies = yearsOfStudies;
        this.grades = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public int getYearsOfStudies() {
        return yearsOfStudies;
    }

    public void addGrade(int term, String courseName, int grade) throws OperationNotAllowedException {
        grades.putIfAbsent(term, new ArrayList<>());

        if (grades.get(term).size() == 3)
            throw new OperationNotAllowedException(id, term);

        if ((yearsOfStudies == 3 && term > 6) || (yearsOfStudies == 4 && term > 8))
            throw new OperationNotAllowedException(term, id);

        grades.get(term).add(new Grade(term, courseName, grade));
    }

    public int getPassed() {
        return grades
                .values()
                .stream()
                .mapToInt(List::size)
                .sum();
    }

    public double getAverage() {
        return grades
                .values()
                .stream()
                .flatMapToDouble(list -> list.stream().mapToDouble(Grade::getGrade))
                .average()
                .orElse(0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Student: %s%n", id));

        int terms = yearsOfStudies == 3 ? 6 : 8;

        for (int i = 1; i <= terms; i++) {
            sb.append(String.format("Term %d%n", i));

            List<Grade> list = grades.get(i);

            if (list != null) {
                sb.append(String.format("Courses: %d%n", list.size()));
                sb.append(String.format("Average grade for term: %.2f%n", list
                        .stream()
                        .mapToInt(Grade::getGrade)
                        .average()
                        .orElse(0)));
            } else {
                sb.append("Courses: 0\n");
                sb.append("Average grade for term: 5.00\n");
            }
        }

        sb.append(String.format("Average grade: %.2f%n", getAverage()));
        sb.append(String.format("Courses attended: %s", grades
                .values()
                .stream()
                .flatMap(list -> list.stream().map(Grade::getCourse))
                .sorted(String::compareTo)
                .collect(Collectors.joining(","))));

        return sb.toString();
    }
}

class Course {
    private final String name;
    private final IntSummaryStatistics summary;

    public Course(String name) {
        this.name = name;
        this.summary = new IntSummaryStatistics();
    }

    public void addGrade(int grade) {
        summary.accept(grade);
    }

    public String getName() {
        return name;
    }

    public long getStudents() {
        return summary.getCount();
    }

    public double getAverage() {
        return summary.getAverage();
    }
}

class Faculty {
    private final Map<String, Student> students;
    private final Map<String, Course> courses;
    private final List<String> logs;

    public Faculty() {
        this.students = new HashMap<>();
        this.courses = new HashMap<>();
        this.logs = new ArrayList<>();
    }

    void addStudent(String id, int yearsOfStudies) {
        students.putIfAbsent(id, new Student(id, yearsOfStudies));
    }

    void addGradeToStudent(String studentId, int term, String courseName, int grade) throws OperationNotAllowedException {
        try {
            students.get(studentId).addGrade(term, courseName, grade);
        } catch (OperationNotAllowedException e) {
            System.out.println(e.getMessage());
        }

        Student student = students.get(studentId);

        courses.putIfAbsent(courseName, new Course(courseName));
        courses.get(courseName).addGrade(grade);

        if ((student.getYearsOfStudies() == 3 && student.getPassed() == 18) || (student.getYearsOfStudies() == 4 && student.getPassed() == 24)) {
            logs.add(String.format("Student with ID %s graduated with average grade %.2f in %d years.", student.getId(), student.getAverage(), student.getYearsOfStudies()));

            students.remove(studentId);
        }
    }

    String getFacultyLogs() {
        return String.join("\n", logs);
    }

    String getDetailedReportForStudent(String id) {
        return students.get(id).toString();
    }

    void printFirstNStudents(int n) {
        students
                .values()
                .stream()
                .sorted(Comparator.comparing(Student::getPassed).thenComparing(Student::getAverage).thenComparing(Student::getId).reversed())
                .limit(n)
                .forEach(s -> System.out.printf("Student: %s Courses passed: %d Average grade: %.2f%n", s.getId(), s.getPassed(), Math.max(5.0, s.getAverage())));
    }

    void printCourses() {
        courses
                .values()
                .stream()
                .filter(c -> c.getStudents() > 0)
                .sorted(Comparator.comparing(Course::getStudents).thenComparing(Course::getAverage).thenComparing(Course::getName))
                .forEach(c -> System.out.printf("%s %d %.2f%n", c.getName(), c.getStudents(), Math.max(5.0, c.getAverage())));
    }
}

public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();

        if (testCase == 1) {
            System.out.println("TESTING addStudent AND printFirstNStudents");
            Faculty faculty = new Faculty();
            for (int i = 0; i < 10; i++) {
                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
            }
            faculty.printFirstNStudents(10);

        } else if (testCase == 2) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            try {
                faculty.addGradeToStudent("123", 7, "NP", 10);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
            try {
                faculty.addGradeToStudent("1234", 9, "NP", 8);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        } else if (testCase == 3) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (testCase == 4) {
            System.out.println("Testing addGrade for graduation");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            int counter = 1;
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            counter = 1;
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
            faculty.printFirstNStudents(2);
        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            if (testCase == 5)
                faculty.printFirstNStudents(10);
            else if (testCase == 6)
                faculty.printFirstNStudents(3);
            else
                faculty.printFirstNStudents(20);
        } else if (testCase == 8 || testCase == 9) {
            System.out.println("TESTING DETAILED REPORT");
            Faculty faculty = new Faculty();
            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
            int grade = 6;
            int counterCounter = 1;
            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
                for (int j = 1; j < 3; j++) {
                    try {
                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
                    } catch (OperationNotAllowedException e) {
                        e.printStackTrace();
                    }
                    grade++;
                    if (grade == 10)
                        grade = 5;
                    ++counterCounter;
                }
            }
            System.out.println(faculty.getDetailedReportForStudent("student1"));
        } else if (testCase == 10) {
            System.out.println("TESTING PRINT COURSES");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            faculty.printCourses();
        } else if (testCase == 11) {
            System.out.println("INTEGRATION TEST");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }

            }

            for (int i = 11; i < 15; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= 3; k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("DETAILED REPORT FOR STUDENT");
            System.out.println(faculty.getDetailedReportForStudent("student2"));
            try {
                System.out.println(faculty.getDetailedReportForStudent("student11"));
                System.out.println("The graduated students should be deleted!!!");
            } catch (NullPointerException e) {
                System.out.println("The graduated students are really deleted");
            }
            System.out.println("FIRST N STUDENTS");
            faculty.printFirstNStudents(10);
            System.out.println("COURSES");
            faculty.printCourses();
        }
    }
}
