import java.util.*;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class ObjectCanNotBeMovedException extends Exception {
    public ObjectCanNotBeMovedException(String message) {
        super(message);
    }
}

class MovableObjectNotFittableException extends Exception {
    public MovableObjectNotFittableException(String message) {
        super(message);
    }
}

interface Movable {
    void moveUp() throws ObjectCanNotBeMovedException;

    void moveDown() throws ObjectCanNotBeMovedException;

    void moveLeft() throws ObjectCanNotBeMovedException;

    void moveRight() throws ObjectCanNotBeMovedException;

    int getCurrentXPosition();

    int getCurrentYPosition();

    TYPE getType();
}

class MovablePoint implements Movable {
    private int x;
    private int y;
    private final int xSpeed;
    private final int ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    private void tryMoving(int x, int y) throws ObjectCanNotBeMovedException {
        if (this.x + x > MovablesCollection.maxX || this.x + x < 0 || this.y + y > MovablesCollection.maxY || this.y + y < 0) {
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds", this.x + x, this.y + y));
        }
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        tryMoving(0, ySpeed);
        y = y + ySpeed;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        tryMoving(0, -ySpeed);
        y = y - ySpeed;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        tryMoving(-xSpeed, 0);
        x = x - xSpeed;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        tryMoving(xSpeed, 0);
        x = x + xSpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public String toString() {
        return "Movable point with coordinates (" + x + "," + y + ")";
    }

    @Override
    public TYPE getType() {
        return TYPE.POINT;
    }
}

class MovableCircle implements Movable {
    private final int radius;
    private final MovablePoint center;

    public MovableCircle(int radius, MovablePoint centar) {
        this.radius = radius;
        this.center = centar;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        center.moveUp();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        center.moveDown();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        center.moveLeft();
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        center.moveRight();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    @Override
    public String toString() {
        return "Movable circle with center coordinates (" + getCurrentXPosition() + "," + getCurrentYPosition() + ") and radius " + radius;
    }

    @Override
    public TYPE getType() {
        return TYPE.CIRCLE;
    }

    public int getRadius() {
        return radius;
    }
}

class MovablesCollection {
    private final List<Movable> movable;
    public static int maxX = 0;
    public static int maxY = 0;

    public MovablesCollection(int x_MAX, int y_MAX) {
        maxX = x_MAX;
        maxY = y_MAX;
        movable = new ArrayList<>();
    }

    private boolean canFit(Movable m) {
        int x = m.getCurrentXPosition();
        int y = m.getCurrentYPosition();
        int r = 0;

        if (m.getType() == TYPE.CIRCLE)
            r = ((MovableCircle) m).getRadius();

        return x - r >= 0 && x + r <= MovablesCollection.maxX && y - r >= 0 && y + r <= MovablesCollection.maxY;
    }

    public void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        if (!canFit(m)) {
            if (m.getType() == TYPE.POINT)
                throw new MovableObjectNotFittableException(String.format("Movable point with center (%d,%d) can not be fitted into the collection", m.getCurrentXPosition(), m.getCurrentYPosition()));
            else
                throw new MovableObjectNotFittableException(String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection", m.getCurrentXPosition(), m.getCurrentYPosition(), ((MovableCircle) m).getRadius()));

        }

        movable.add(m);
    }

    public void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) throws ObjectCanNotBeMovedException {
        for (Movable object : movable) {
            if (object.getType() == type) {
                if (direction == DIRECTION.UP) {
                    object.moveUp();
                } else if (direction == DIRECTION.DOWN) {
                    object.moveDown();
                } else if (direction == DIRECTION.LEFT) {
                    object.moveLeft();
                } else if (direction == DIRECTION.RIGHT) {
                    object.moveRight();
                }
            }
        }
    }

    public static void setxMax(int maxX) {
        MovablesCollection.maxX = maxX;
    }

    public static void setyMax(int maxY) {
        MovablesCollection.maxY = maxY;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Collection of movable objects with size ")
                .append(movable.size()).append(":\n");

        for (Movable object : movable) {
            sb.append(object.toString()).append("\n");
        }

        return sb.toString();
    }
}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try {
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try {
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        } catch (ObjectCanNotBeMovedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        } catch (ObjectCanNotBeMovedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        } catch (ObjectCanNotBeMovedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        } catch (ObjectCanNotBeMovedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());


    }


}
