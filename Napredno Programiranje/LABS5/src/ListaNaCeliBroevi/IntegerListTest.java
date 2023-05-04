package ListaNaCeliBroevi;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.List;

class IntegerList {

    private final ArrayList<Integer> list;

    public IntegerList() {
        this.list = new ArrayList<Integer>();
    }

    public IntegerList(Integer... numbers) {
        this.list = new ArrayList<Integer>();
        for (int i = 0; i < numbers.length; i++) {
            list.add(i, numbers[i]);
        }
    }

    public void add(int el, int idx) {
        if (idx >= list.size())
            for (int i = list.size(); i < idx; i++) {
                this.list.add(i, 0);
            }

        this.list.add(idx, el);
    }

    public int remove(int idx) {
        if (idx < 0 || idx >= list.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return this.list.remove(idx);
    }

    public void set(int el, int idx) {
        if (idx < 0 || idx >= list.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }

        this.list.set(idx, el);
    }

    public int get(int idx) {
        if (idx < 0 || idx >= list.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return this.list.get(idx);
    }

    public int size() {
        return this.list.size();
    }

    public int count(int el) {
        int k = 0;
        for (Integer integer : list) {
            if (integer == el)
                k++;
        }

        return k;
    }

    public void removeDuplicates() {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int s = i + 1; s < list.size(); s++) {
                if (Objects.equals(this.list.get(i), this.list.get(s))) {
                    list.remove(i);
                    i--;
                    break;
                }
            }
        }
    }

    public int sumFirst(int k) {
        if (k < 0 || k >= list.size()) {
            k = list.size();
        }

        int sum = 0;
        for (int i = 0; i < k; i++) {
            sum += this.list.get(i);
        }
        return sum;
    }

    public int sumLast(int k) {
        if (k < 0 || k >= list.size()) {
            k = list.size();
        }

        int sum = 0;
        for (int i = list.size() - k; i < list.size(); i++) {
            sum += this.list.get(i);
        }
        return sum;
    }

    public void shiftRight(int idx, int k) {
        if (idx < 0 || idx >= list.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }

        int a = this.list.get(idx);
        this.list.remove(idx);
        this.list.add((idx + k) % (list.size() + 1), a);
    }

    public void shiftLeft(int idx, int k) {
        if (idx < 0 || idx >= list.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }

        int a = this.list.get(idx);
        this.list.remove(idx);
        this.list.add(Math.floorMod(idx - k, list.size() + 1), a);
    }

    public IntegerList addValue(int value) {
        IntegerList il = new IntegerList();

        for (int i = 0; i < list.size(); i++) {
            il.add(list.get(i) + value, i);
        }

        return il;
    }
}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test standard methods
            int subtest = jin.nextInt();
            if (subtest == 0) {
                IntegerList list = new IntegerList();
                while (true) {
                    int num = jin.nextInt();
                    if (num == 0) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if (num == 1) {
                        list.remove(jin.nextInt());
                    }
                    if (num == 2) {
                        print(list);
                    }
                    if (num == 3) {
                        break;
                    }
                }
            }
            if (subtest == 1) {
                int n = jin.nextInt();
                Integer[] a = new Integer[n];
                for (int i = 0; i < n; ++i) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if (k == 1) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer[] a = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if (num == 1) {
                    list.removeDuplicates();
                }
                if (num == 2) {
                    print(list.addValue(jin.nextInt()));
                }
                if (num == 3) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
        if (k == 2) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer[] a = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if (num == 1) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if (num == 2) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if (num == 3) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if (il.size() == 0) System.out.print("EMPTY");
        for (int i = 0; i < il.size(); ++i) {
            if (i > 0) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}