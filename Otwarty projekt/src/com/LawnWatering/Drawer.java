package com.LawnWatering;

public class Drawer implements Sprinklers {
    public boolean contains_quarter(int quarter, SprinklerType type, Direction direction) {
        switch (type) {
            case FULL:
                return true;

            case THREE_QUARTERS:
                return quarter != (Direction.numDir(direction) - 1) % 4;

            case HALF:
                return quarter == Direction.numDir(direction) || quarter == (Direction.numDir(direction) + 1) % 4;

            case QUARTER:
                return quarter == Direction.numDir(direction);
        }

        return false;
    }

    private void fix_value_artifacts(int[][] tab, Sprinkler sprinkler, int r) {
        Direction direction = sprinkler.getDir();
        SprinklerType type = sprinkler.getType();
        int x = sprinkler.getX();
        int y = sprinkler.getY();

        int HEIGHT = tab.length - 1;
        int WIDTH = tab[0].length - 1;

        switch (SprinklerType.numType(type)) {
            case 1:
                tab[y][x]++;
                for (int i = 0; i < r + 1; i++) {
                    if ((y + i < HEIGHT) && (tab[y + i][x] != -1)) {
                        tab[y + i][x]--;
                    }
                    if ((y - i >= 0) && (y - i < HEIGHT) && (tab[y - i][x] != -1)) {
                        tab[y - i][x]--;
                    }
                    if ((x + i < WIDTH) && (tab[y][x + i] != -1)) {
                        tab[y][x + i]--;
                    }
                    if ((x - i >= 0) && (x - i < WIDTH) && (tab[y][x - i] != -1)) {
                        tab[y][x - i]--;
                    }
                }
                break;

            case 2:
                tab[y][x] -= 2;
                for (int i = 1; i <= r; i++) {
                    if ((x - i >= 0) && (x - i < WIDTH) && (tab[y][x - i] != -1)) {
                        tab[y][x - i]--;
                    }
                }

                switch (Direction.numDir(direction)) {
                    case 1:
                        for (int i = 1; i <= r; i++) {
                            if ((y + i < HEIGHT) && (tab[y + i][x] != -1)) {
                                tab[y + i][x]--;
                            }

                            if ((x + i < WIDTH) && (tab[y][x + i] != -1)) {
                                tab[y][x + i]--;
                            }
                        }
                        break;
                    case 2:
                        for (int i = 1; i <= r; i++) {
                            if ((y + i < HEIGHT) && (tab[y + i][x] != -1)) {
                                tab[y + i][x]--;
                            }
                        }
                        break;
                    case 3:
                        for (int i = 1; i <= r; i++) {
                            if ((y - i > 0) && (tab[y - i][x] != -1)) {
                                tab[y - i][x]--;
                            }
                        }
                        break;

                    case 4:
                        for (int i = 1; i <= r; i++) {
                            if ((x - i > 0) && (tab[y][x - i] != -1)) {
                                tab[y][x - i]++;
                            }
                            if ((x + i < WIDTH) && (tab[y][x + i] != -1)) {
                                tab[y][x + i]--;
                            }
                            if ((y + i < HEIGHT) && (tab[y + i][x] != -1)) {
                                tab[y + i][x]--;
                            }
                            if ((y - i > 0) && (tab[y + i][x] != -1)) {
                                tab[y - i][x]--;
                            }
                        }
                        break;
                }
                break;

            case 3:
                tab[y][x]--;

                for (int i = 1; i < r + 1; i++) {
                    if (Direction.numDir(direction) != 1 && Direction.numDir(direction) != 3 && (y - i >= 0)
                            && (y - i < HEIGHT) && (tab[y - i][x] != -1)) {
                        tab[y - i][x] -= 2;
                    }
                }

                switch (Direction.numDir(direction)) {
                    case 1:
                        for (int i = 1; i <= r; i++) {
                            if ((y - i > 0) && (tab[y - i][x] != -1)) {
                                tab[y - i][x] += 2;
                            }

                            if ((x - i < WIDTH) && (tab[y][x + i] != -1)) {
                                tab[y][x + i]--;
                            }
                        }
                        break;

                    case 2:
                        for (int i = 1; i <= r; i++) {
                            if ((y + i < HEIGHT) && (tab[y + i][x] != -1)) {
                                tab[y + i][x]--;
                            }
                            if ((y - i > 0) && (tab[y - i][x] != -1)) {
                                tab[y - i][x] += 2;
                            }
                        }
                        break;

                    case 3:
                        for (int i = 1; i <= r; i++) {
                            if ((x - i > 0) && (tab[y][x - i] != -1)) {
                                tab[y][x - i]--;
                            }
                        }
                        break;

                    case 4:
                        for (int i = 1; i <= r; i++) {
                            if ((y - i > 0) && (tab[y - i][x] != -1)) {
                                tab[y - i][x]++;
                            }
                        }
                        break;
                }
                break;
        }
    }

    public void draw_circle_with_reflections(int[][] tab, Sprinkler sprinkler) {
        Direction direction = sprinkler.getDir();
        SprinklerType type = sprinkler.getType();
        int x = sprinkler.getX();
        int y = sprinkler.getY();

        int HEIGHT = tab.length - 1;
        int WIDTH = tab[0].length - 1;

        int r = 0, r2;
        int point_x, point_y;

        switch (type) {
            case FULL:
                r = Sprinklers.FULL_RADIUS;
                break;
            case THREE_QUARTERS:
                r = Sprinklers.THREE_QUARTERS_RADIUS;
                break;
            case HALF:
                r = Sprinklers.HALF_RADIUS;
                break;
            case QUARTER:
                r = Sprinklers.QUARTER_RADIUS;
                break;
        }

        r2 = (int) (Math.pow(r, 2));
        for (int i = 0; i <= r; i++) {
            for (int j = 0; j <= r; j++) {
                if ((int) (Math.pow(i, 2)) + (int) (Math.pow(j, 2)) <= r2) {
                    /* GÓRA-LEWO */
                    if (contains_quarter(0, type, direction) && ((y - i) < HEIGHT) && ((y - i) >= 0)
                            && ((x - j) < WIDTH) && ((x - j) >= 0)) {
                        if (tab[y - i][x - j] == -1) {
                            int d = 0;
                            int e = 0;

                            while (y - i + d >= 0 && y - i + d < HEIGHT && tab[y - i + d][x - j] == -1) {
                                ++d;
                            }

                            while (x - j + e >= 0 && x - j + e < WIDTH && tab[y - i][x - j + e] == -1) {
                                ++e;
                            }

                            if (d < e) {
                                point_y = y - i + 2 * d;
                                point_x = x - j;

                                if ((point_y >= 0) && (point_y < HEIGHT) && (tab[point_y][point_x] != -1)) {
                                    tab[point_y][point_x]++;
                                }
                            } else {
                                point_y = y - i;
                                point_x = x - j + 2 * e;

                                if ((point_x >= 0) && (point_x < WIDTH) && (tab[point_y][point_x] != -1)) {
                                    tab[point_y][point_x]++;
                                }
                            }
                        } else {
                            tab[y - i][x - j]++;
                        }
                    }

                    /* GÓRA - PRAWO */
                    if (contains_quarter(1, type, direction) && ((y - i) < HEIGHT) && ((y - i) >= 0)
                            && ((x + j) < WIDTH) && ((x + j) >= 0)) {
                        if (tab[y - i][x + j] == -1) {
                            int d = 0;
                            int e = 0;

                            while (y - i + d >= 0 && y - i + d < HEIGHT && tab[y - i + d][x + j] == -1) {
                                ++d;
                            }

                            while (x + j - e >= 0 && x + j - e < WIDTH && tab[y - i][x + j - e] == -1) {
                                ++e;
                            }

                            if (d < e) {
                                point_y = y - i + 2 * d;
                                point_x = x + j;

                                if ((point_y >= 0) && (point_y < HEIGHT) && (tab[point_y][point_x] != -1)) {
                                    tab[point_y][point_x]++;
                                }
                            } else {

                                point_y = y - i;
                                point_x = x + j - 2 * e;

                                if ((point_x >= 0) && (point_x < WIDTH) && (tab[point_y][point_x] != -1)) {
                                    tab[point_y][point_x]++;
                                }
                            }
                        } else {
                            tab[y - i][x + j]++;
                        }
                    }

                    /* DÓŁ - PRAWO */
                    if (contains_quarter(2, type, direction) && ((y + i) < HEIGHT) && ((y + i) >= 0)
                            && ((x + j) < WIDTH) && ((x + j) >= 0)) {
                        if (tab[y + i][x + j] == -1) {
                            int d = 0;
                            int e = 0;

                            while (y + i - d >= 0 && y + i - d < HEIGHT && tab[y + i - d][x + j] == -1) {
                                ++d;
                            }

                            while (x + j - e >= 0 && x + j - e < WIDTH && tab[y + i][x + j - e] == -1) {
                                ++e;
                            }

                            if (d < e) {
                                point_y = y + i - 2 * d;
                                point_x = x + j;

                                if (point_y < HEIGHT && point_y >= 0 && tab[point_y][point_x] != -1) {
                                    tab[point_y][point_x]++;
                                }
                            } else {
                                point_y = y + i;
                                point_x = x + j - 2 * e;

                                if (point_x < 8000 && point_x >= 0 && tab[point_y][point_x] != -1) {
                                    tab[point_y][point_x]++;
                                }
                            }
                        } else {
                            tab[y + i][x + j]++;
                        }
                    }

                    /* DÓŁ - LEWO */
                    if (contains_quarter(3, type, direction) && ((y + i) < HEIGHT) && ((y + i) >= 0)
                            && ((x - j) < WIDTH) && ((x - j) >= 0)) {
                        if (tab[y + i][x - j] == -1) {
                            int d = 0;
                            int e = 0;

                            while (y + i - d >= 0 && y + i - d < HEIGHT && tab[y + i - d][x - j] == -1) {
                                ++d;
                            }

                            while (x - j + e >= 0 && x - j + e < WIDTH && tab[y + i][x - j + e] == -1) {
                                ++e;
                            }

                            if (d < e) {

                                point_y = y + i - 2 * d;
                                point_x = x - j;

                                if (point_y >= 0 && point_y < HEIGHT && tab[point_y][point_x] != -1) {
                                    tab[point_y][point_x]++;
                                }
                            } else {

                                point_y = y + i;
                                point_x = x - j + 2 * e;

                                if (point_x >= 0 && point_x < WIDTH && tab[point_y][point_x] != -1) {
                                    tab[point_y][point_x]++;
                                }
                            }
                        } else {
                            tab[y + i][x - j]++;
                        }
                    }
                }
            }
        }
        /* Skorygowanie komórek, które powiększyły sie kilka razy przez przypadek */
        fix_value_artifacts(tab, sprinkler, r);
    }

    public void draw_circle_without_reflections(int[][] tab, Sprinkler sprinkler) {
        Direction direction = sprinkler.getDir();
        SprinklerType type = sprinkler.getType();
        int x = sprinkler.getX();
        int y = sprinkler.getY();

        // System.out.println( "Set: " + direction + " " + type + " (" + x + ", " + y
        // +")" );

        int HEIGHT = tab.length;
        int WIDTH = tab[0].length;

        int r = 0, r2;

        switch (type) {
            case FULL:
                r = Sprinklers.FULL_RADIUS;
                break;
            case THREE_QUARTERS:
                r = Sprinklers.THREE_QUARTERS_RADIUS;
                break;
            case HALF:
                r = Sprinklers.HALF_RADIUS;
                break;
            case QUARTER:
                r = Sprinklers.QUARTER_RADIUS;
                break;
        }

        r2 = (int) (Math.pow(r, 2));
        for (int i = 0; i <= r; i++) {
            for (int j = 0; j <= r; j++) {
                if ((int) (Math.pow(i, 2)) + (int) (Math.pow(j, 2)) <= r2) {
                    /* GÓRA-LEWO */
                    if (contains_quarter(0, type, direction) && ((y - i) < HEIGHT) && ((y - i) >= 0)
                            && ((x - j) < WIDTH) && ((x - j) >= 0)) {
                        if (tab[y - i][x - j] != -1) {
                            tab[y - i][x - j]++;
                        }
                    }

                    /* GÓRA - PRAWO */
                    if (contains_quarter(1, type, direction) && ((y - i) < HEIGHT) && ((y - i) >= 0)
                            && ((x + j) < WIDTH) && ((x + j) >= 0)) {
                        if (tab[y - i][x + j] != -1) {
                            tab[y - i][x + j]++;
                        }
                    }

                    /* DÓŁ - PRAWO */
                    if (contains_quarter(2, type, direction) && ((y + i) < HEIGHT) && ((y + i) >= 0)
                            && ((x + j) < WIDTH) && ((x + j) >= 0)) {
                        if (tab[y + i][x + j] != -1) {
                            tab[y + i][x + j]++;
                        }
                    }

                    /* DÓŁ - LEWO */
                    if (contains_quarter(3, type, direction) && ((y + i) < HEIGHT) && ((y + i) >= 0)
                            && ((x - j) < WIDTH) && ((x - j) >= 0)) {
                        if (tab[y + i][x - j] != -1) {
                            tab[y + i][x - j]++;
                        }
                    }
                }
            }
        }
        /* Skorygowanie komórek, które powiększyły sie kilka razy przez przypadek */
        fix_value_artifacts(tab, sprinkler, r);
    }
}
