package AdministrationChatSystem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;

class NoSuchRoomException extends Exception {
    public NoSuchRoomException(String string) {
        super(String.format("Room %s doesn't exists!", string));
    }
}

class NoSuchUserException extends Exception {
    public NoSuchUserException(String string) {
        super(String.format("User %s doesn't exists!", string));
    }
}

class ChatRoom {
    private final String name;
    private final TreeSet<String> usernames;

    public ChatRoom(String name) {
        this.name = name;
        this.usernames = new TreeSet<>();
    }

    public void addUser(String username) {
        this.usernames.add(username);
    }

    public void removeUser(String username) {
        this.usernames.remove(username);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");

        if (usernames.isEmpty())
            sb.append("EMPTY\n");
        else {
            for (String name : usernames) {
                sb.append(name).append("\n");
            }
        }

        return sb.toString();
    }

    public boolean hasUser(String username) {
        return this.usernames.contains(username);
    }

    public int numUsers() {
        return this.usernames.size();
    }

    public String getName() {
        return name;
    }
}

class ChatSystem {
    private final TreeMap<String, ChatRoom> rooms;
    private final TreeSet<String> users;

    public ChatSystem() {
        this.rooms = new TreeMap<>();
        this.users = new TreeSet<>();
    }

    public void addRoom(String roomName) {
        rooms.put(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName) {
        rooms.remove(roomName);
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if (rooms.containsKey(roomName))
            return rooms.get(roomName);
        else
            throw new NoSuchRoomException(roomName);
    }

    public ChatRoom leastUsers() {
        return rooms.values().stream()
                .min(Comparator.comparing(ChatRoom::numUsers).thenComparing(ChatRoom::getName))
                .orElse(null);
    }

    public void register(String userName) {
        ChatRoom leastUsers = leastUsers();

        users.add(userName);

        if (leastUsers != null) {
            leastUsers.addUser(userName);
        }
    }

    public void registerAndJoin(String userName, String roomName) {
        users.add(userName);

        if (rooms.containsKey(roomName))
            rooms.get(roomName).addUser(userName);
    }

    public void joinRoom(String userName, String roomName) throws NoSuchUserException, NoSuchRoomException {
        if (!users.contains(userName))
            throw new NoSuchUserException(userName);

        if (rooms.containsKey(roomName))
            rooms.get(roomName).addUser(userName);
        else
            throw new NoSuchRoomException(roomName);
    }

    public void leaveRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if (!users.contains(username))
            throw new NoSuchUserException(username);

        if (rooms.containsKey(roomName))
            rooms.get(roomName).removeUser(username);
        else
            throw new NoSuchRoomException(roomName);
    }

    public void followFriend(String username, String friend_username) throws NoSuchUserException {
        if (!users.contains(username))
            throw new NoSuchUserException(username);

        for (ChatRoom cr : rooms.values()) {
            if (cr.hasUser(friend_username))
                cr.addUser(username);
        }
    }
}

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr.addUser(jin.next());
                if (k == 1) cr.removeUser(jin.next());
                if (k == 2) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if (n == 0) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr2.addUser(jin.next());
                if (k == 1) cr2.removeUser(jin.next());
                if (k == 2) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if (k == 1) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while (true) {
                String cmd = jin.next();
                if (cmd.equals("stop")) break;
                if (cmd.equals("print")) {
                    System.out.println(cs.getRoom(jin.next()) + "\n");
                    continue;
                }
                for (Method m : mts) {
                    if (m.getName().equals(cmd)) {
                        String params[] = new String[m.getParameterTypes().length];
                        for (int i = 0; i < params.length; ++i) params[i] = jin.next();
                        m.invoke(cs, params);
                    }
                }
            }
        }
    }

}
