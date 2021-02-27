package com.LawnWatering;

public class Stats {
    public double avg(int[][] lawn) throws ArithmeticException {
        int h = lawn.length;
        int w = lawn[0].length;

        double n = 0;
        double sum = 0.0;

        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                if (lawn[y][x] != -1) {
                    sum += lawn[y][x];
                    n++;
                }

        try {
            return sum / n;
        } catch (ArithmeticException ea) {
            return -1;
        }
    }

    public double std_deviation(int[][] lawn, double average) throws ArithmeticException {
        int h = lawn.length;
        int w = lawn[0].length;

        double n = 0;
        double sum = 0.0;

        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                if (lawn[y][x] != -1) {
                    sum += Math.pow(lawn[y][x] - average, 2);
                    n++;
                }

        return Math.sqrt(sum / n);
    }

    public double std_deviation(int[][] lawn) throws ArithmeticException {
        int h = lawn.length;
        int w = lawn[0].length;

        double n = h * w;
        double sum = 0.0;
        double average = avg(lawn);

        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                if (lawn[y][x] != -1) {
                    sum += Math.pow(lawn[y][x] - average, 2);
                    n++;
                }

        try {
            return Math.sqrt(sum / n);
        } catch (ArithmeticException ea) {
            return -1;
        }
    }
}