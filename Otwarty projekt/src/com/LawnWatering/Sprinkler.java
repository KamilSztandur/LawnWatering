package com.LawnWatering;

import java.util.ArrayList;

interface Sprinklers {
    enum Direction {
        LEFT(0), TOP(1), RIGHT(2), BOTTOM(3);

        private final int directionNumber;

        private Direction(int directionNumber) {
            this.directionNumber = directionNumber;
        }

        /* Pozyskiwanie numeru !!!!!!! */
        public static int numDir(Direction dir) {
            return dir.directionNumber;
        }

        /* Pozyskiwanie enum !!!!!!! */
        public static Direction getDir(int n) {
            switch (n) {
                case 0:
                    return Direction.LEFT;
                case 1:
                    return Direction.TOP;
                case 2:
                    return Direction.RIGHT;
                case 3:
                    return Direction.BOTTOM;
            }

            return null;
        }

        public Direction rotatedRight() {
            return rotatedRight(1);
        }

        public Direction rotatedRight(int times) {
            return getDir((numDir(this) + times) % 4);
        }
    }

    enum SprinklerType {
        FULL(1), THREE_QUARTERS(2), HALF(3), QUARTER(4);

        private final int typeNumber;

        private SprinklerType(int typeNumber) {
            this.typeNumber = typeNumber;
        }

        /* Pozyskiwanie numeru !!!!!!! */
        public static int numType(SprinklerType type) {
            return type.typeNumber;
        }

        public static SprinklerType getType(int n) {
            switch (n) {
                case 1:
                    return SprinklerType.FULL;
                case 2:
                    return SprinklerType.THREE_QUARTERS;
                case 3:
                    return SprinklerType.HALF;
                case 4:
                    return SprinklerType.QUARTER;
            }

            return null;
        }
    }

    /* Promienie poszczególnych zraszaczy */
    final int FULL_RADIUS = 200;
    final int THREE_QUARTERS_RADIUS = 300;
    final int HALF_RADIUS = 400;
    final int QUARTER_RADIUS = 500;

    /* Okresy obrotu poszczególnych zraszaczy (w cyklach) */
    final int FULL_CYCLE = 4;
    final int HALF_AND_QUARTER_CYCLE = 3;
    final int HALF_CYCLE = 2;
    final int QUARTER_CYCLE = 1;
}

public class Sprinkler implements Sprinklers {
    private int x;
    private int y;
    private Direction dir;
    private SprinklerType type;

    static int sprinklers_amount = 0;

    public Sprinkler(int x, int y, Direction dir, SprinklerType type) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDir() {
        return dir;
    }

    public SprinklerType getType() {
        return type;
    }
}

class SprinklersList {

    private ArrayList<Sprinkler> list;

    public SprinklersList() {
        list = new ArrayList<>(2000);
    }

    public void add(MainWindow gui, Sprinkler spr) {
        list.add(spr);
        Sprinkler.sprinklers_amount++;
        gui.updateSprinklersAmount(Sprinkler.sprinklers_amount);
        gui.printNewAction("Ustawiłem zraszacz: " + " " + spr.getType() + " " + spr.getDir() + " (" + spr.getX() + ", "
                + spr.getY() + ")");
    }

    public ArrayList<Sprinkler> getList() {
        return list;
    }
}
