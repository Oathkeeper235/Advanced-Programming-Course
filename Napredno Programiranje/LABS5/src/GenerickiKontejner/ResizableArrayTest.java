package GenerickiKontejner;

import java.sql.Array;
import java.util.Arrays;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.stream.IntStream;

class ResizableArray<T> {
    private T[] elements;
    private int counter;

    public ResizableArray() {
        this.elements = (T[]) new Object[1];
        this.counter = 0;
    }

    public void addElement(T element) {
        if (counter >= elements.length)
            elements = Arrays.copyOf(elements, elements.length * 2);

        elements[counter] = element;
        counter++;
    }

    public boolean removeElement(T element) {
        for (int i = 0; i < counter; i++) {
            if (elements[i].equals(element)) {
                System.arraycopy(elements, i + 1, elements, i, counter - i - 1);
                counter--;
                elements[counter] = null;

                if (counter < elements.length / 2)
                    elements = Arrays.copyOf(elements, elements.length / 2);

                return true;
            }
        }

        return false;
    }

    public boolean contains(T element) {
        return Arrays.stream(elements).limit(counter).anyMatch(i -> i.equals(element));
    }

    public Object[] toArray() {
        return elements;
    }

    public boolean isEmpty() {
        return counter == 0;
    }

    public int count() {
        return counter;
    }

    public T elementAt(int i) {
        if (i < 0 || i >= counter)
            throw new ArrayIndexOutOfBoundsException();

        return elements[i];
    }

    public static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src) {
        T[] a = (T[]) Arrays.copyOf(src.toArray(), src.counter);

        for (T t : a) {
            dest.addElement(t);
        }
    }
}

class IntegerArray extends ResizableArray<Integer> {

    public IntegerArray() {
        super();
    }

    private IntStream getStream() {
        return Arrays.stream(toArray()).limit(count()).mapToInt(Integer.class::cast);
    }

    public double sum() {
        return getStream().sum();
    }

    public double mean() {
        return sum() / count();
    }

    public int countNonZero() {
        return (int) getStream().filter(i -> i != 0).count();
    }

    public IntegerArray distinct() {
        IntegerArray ia = new IntegerArray();

        getStream().distinct().forEach(ia::addElement);

        return ia;
    }

    public IntegerArray increment(int o) {
        IntegerArray ia = new IntegerArray();

        getStream().map(i -> i + o).forEach(ia::addElement);

        return ia;
    }
}

public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if (test == 0) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while (jin.hasNextInt()) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if (test == 1) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for (int i = 0; i < 4; ++i) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if (test == 2) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while (jin.hasNextInt()) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if (a.sum() > 100)
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if (test == 3) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for (int w = 0; w < 500; ++w) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k = 2000;
                int t = 1000;
                for (int i = 0; i < k; ++i) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for (int i = 0; i < t; ++i) {
                    a.removeElement(k - i - 1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}
