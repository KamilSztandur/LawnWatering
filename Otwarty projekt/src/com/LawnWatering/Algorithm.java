package com.LawnWatering;

import java.util.Arrays;

public class Algorithm implements Sprinklers {
    MainWindow gui;
    Lawn lawn;
    SprinklersList list;

    final Drawer drawer = new Drawer();

    public Algorithm(MainWindow gui) {
        this.gui = gui;
        this.lawn = gui.lawn;
        this.list = gui.list;
    }

    private int getDirectionHorizontalOffset(Direction direction) {
        return direction == Direction.LEFT ? -1 : direction == Direction.RIGHT ? 1 : 0;
    }

    private int getDirectionVerticalOffset(Direction direction) {
        return direction == Direction.TOP ? -1 : direction == Direction.BOTTOM ? 1 : 0;
    }

    private int getEdgeHorizontalOffset(Direction direction, boolean external) {
        direction = external ? direction.rotatedRight() : direction;

        return direction == Direction.LEFT || direction == Direction.BOTTOM ? 1 : 0;
    }

    private int getEdgeVerticalOffset(Direction direction, boolean external) {
        direction = external ? direction.rotatedRight() : direction;

        return direction == Direction.LEFT || direction == Direction.TOP ? 1 : 0;
    }

    private int getPoint(int x, int y, int[][] shape, int width, int height) {
        return x >= 0 && x < width && y >= 0 && y < height ? shape[y][x] : -1;
    }

    private void setPoint(int x, int y, int value, int[][] shape, int width, int height) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            shape[y][x] = value;
        }
    }

    private boolean checkLine(int[][] array, int x, int y, int length, Direction direction, int value, int width,
            int height) {
        int horizontal_offset = getDirectionHorizontalOffset(direction);
        int vertical_offset = getDirectionVerticalOffset(direction);

        for (int i = 0; i < length; i++) {
            if (getPoint(x + horizontal_offset * i, y + vertical_offset * i, array, width, height) != value) {
                return false;
            }
        }

        return true;
    }

    private void setLine(int x, int y, int length, Direction direction, int value, int[][] array, int width,
            int height) {
        int horizontal_offset = getDirectionHorizontalOffset(direction);
        int vertical_offset = getDirectionVerticalOffset(direction);

        for (int i = 0; i < length; i++) {
            setPoint(x + horizontal_offset * i, y + vertical_offset * i, value, array, width, height);
        }
    }

    private int[][] copyArray(int[][] array, int width, int height) {
        int[][] copy = new int[height][width];

        for (int i = 0; i < height; i++) {
            copy[i] = Arrays.copyOf(array[i], array[i].length);
        }

        return copy;
    }

    private boolean checkFacing(int[][] array, int x, int y, Direction direction, int width, int height) {
        int horizontalOffset = getDirectionHorizontalOffset(direction)
                + getDirectionHorizontalOffset(direction.rotatedRight());
        int verticalOffset = getDirectionVerticalOffset(direction)
                + getDirectionVerticalOffset(direction.rotatedRight());

        return getPoint(x + horizontalOffset, y + verticalOffset, array, width, height) == 0;
    }

    private boolean checkEdgeSprinkler(SprinklerType type, int[][] array, int x, int y, Direction direction, int width,
            int height) {
        if (!checkFacing(array, x, y, direction, width, height)) {
            return false;
        }

        if (type == SprinklerType.QUARTER && checkLine(array, x, y, 5, direction, 1, width, height)
                && checkLine(array, x, y, 5, direction.rotatedRight(), 1, width, height)) {
            setLine(x, y, 5, direction, 2, array, width, height);
            setLine(x, y, 5, direction.rotatedRight(), 2, array, width, height);
            return true;
        } else if (type == SprinklerType.HALF && checkLine(array, x, y, 4, direction, 1, width, height)
                && checkLine(array, x, y, 4 + 1, direction.rotatedRight(2), 1, width, height)) {
            setLine(x, y, 4, direction, 2, array, width, height);
            setLine(x, y, 4 + 1, direction.rotatedRight(2), 2, array, width, height);
            return true;
        } else if (type == SprinklerType.THREE_QUARTERS && checkLine(array, x, y, 3, direction, 1, width, height)
                && checkLine(array, x, y, 3, direction.rotatedRight(3), 1, width, height)) {
            setLine(x, y, 3, direction, 2, array, width, height);
            setLine(x, y, 3, direction.rotatedRight(3), 2, array, width, height);
            return true;
        } else {
            return false;
        }
    }

    private boolean check_edge_point(int[][] shape, int x, int y) {
        if (getPoint(x, y, shape, lawn.getINPUT_WIDTH(), lawn.getINPUT_HEIGHT()) != 0) {
            return false;
        }

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (getPoint(x + i, y + j, shape, lawn.getINPUT_WIDTH(), lawn.getINPUT_HEIGHT()) != 0
                        && !(i == 0 && j == 0)) {
                    return true;
                }
            }
        }

        return false;
    }

    private void findEdges(int[][] shape, int[][] edges) {
        for (int x = 0; x < lawn.getINPUT_WIDTH(); x++) {
            for (int y = 0; y < lawn.getINPUT_HEIGHT(); y++) {
                if (check_edge_point(shape, x, y)) {
                    edges[y][x] = 1;
                }
            }
        }
    }

    private void addEdgeSprinklers(SprinklerType type, int[][] shape, int[][] lawn) {
        for (int x = 0; x < this.lawn.getINPUT_WIDTH(); x++) {
            for (int y = 0; y < this.lawn.getINPUT_HEIGHT(); y++) {
                for (Direction direction : Direction.values()) {
                    int horizontalOffset = getEdgeHorizontalOffset(direction, type == SprinklerType.THREE_QUARTERS);
                    int verticalOffset = getEdgeVerticalOffset(direction, type == SprinklerType.THREE_QUARTERS);

                    if (checkEdgeSprinkler(type, shape, x, y, direction, this.lawn.getINPUT_WIDTH(),
                            this.lawn.getINPUT_HEIGHT())) {
                        final Sprinkler sprinkler = new Sprinkler((x + horizontalOffset) * 100,
                                (y + verticalOffset) * 100, direction, type);

                        this.drawer.draw_circle_without_reflections(lawn, sprinkler);

                        this.list.add(this.gui, sprinkler);
                    }
                }
            }
        }
    }

    private void addFulls(int[][] lawn) {
        for (int x = 0; x < this.lawn.getWIDTH(); x++) {
            for (int y = 0; y < this.lawn.getHEIGHT(); y++) {
                if (lawn[y][x] == 0) {
                    final Sprinkler sprinkler = new Sprinkler(x, y, Direction.LEFT, SprinklerType.FULL);

                    this.drawer.draw_circle_without_reflections(lawn, sprinkler);

                    this.list.add(this.gui, sprinkler);
                }
            }
        }
    }

    /* Losowość - do wymiany */
    public void setSprinklers() {
        gui.printNewAction("Rozpoczynam rozstawianie zraszaczy...");

        final int[][] lawnArray = copyArray(this.lawn.upscale_lawn, this.lawn.getWIDTH(), this.lawn.getHEIGHT());
        final int[][] edges = copyArray(this.lawn.parse_lawn, this.lawn.getINPUT_WIDTH(), this.lawn.getINPUT_HEIGHT());

        findEdges(this.lawn.parse_lawn, edges);

        addEdgeSprinklers(SprinklerType.QUARTER, edges, lawnArray);
        addEdgeSprinklers(SprinklerType.THREE_QUARTERS, edges, lawnArray);
        addEdgeSprinklers(SprinklerType.HALF, edges, lawnArray);

        addFulls(lawnArray);

        gui.printNewAction("Sukces.");
    }
}
