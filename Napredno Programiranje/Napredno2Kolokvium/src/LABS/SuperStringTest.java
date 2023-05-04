package LABS;

import java.util.*;
import java.util.stream.Collectors;

class SuperString {

    private LinkedList<String> list;
    private LinkedList<Integer> undo_stack;

    public SuperString() {
        list = new LinkedList<String>();
        undo_stack = new LinkedList<Integer>();
    }

    public void append(String s) {
        list.addLast(s);
        undo_stack.addFirst(1);
    }

    public void insert(String s) {
        list.addFirst(s);
        undo_stack.addFirst(-1);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : list) sb.append(s);
        return sb.toString();
    }

    public boolean contains(String s) {
        return toString().contains(s);
    }

    //TODO: implement method
    public void reverse() {
        Collections.reverse(list);

        list = list.stream().map(i -> new StringBuilder(i).reverse().toString()).collect(Collectors.toCollection(LinkedList::new));
    }

    //TODO: implement method
    public void removeLast(int k) {
        for (int i = 0; i < k; i++) {
            if (undo_stack.getFirst() == 1) {
                list.removeLast();
                undo_stack.removeFirst();
                continue;
            }
            if (undo_stack.getFirst() == -1) {
                list.removeFirst();
                undo_stack.removeFirst();
            }
        }
    }
}

public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            SuperString s = new SuperString();
            while (true) {
                int command = jin.nextInt();
                if (command == 0) {
                    s.append(jin.next());
                }
                if (command == 1) {
                    s.insert(jin.next());
                }
                if (command == 2) {
                    System.out.println(s.contains(jin.next()));
                }
                if (command == 3) {
                    s.reverse();
                }
                if (command == 4) {
                    System.out.println(s);
                }
                if (command == 5) {
                    s.removeLast(jin.nextInt());
                }
                if (command == 6) {
                    break;
                }
            }
        }
    }
}
