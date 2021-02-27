package com.LawnWatering;

public class HSVtoRGB {

    /*
     * H <0, 360> s <0, 1> v <0, 1>
     */

    public static int[] calc(double h, double s, double v) {
        h /= 60;
        double m, n, f;
        int i;

        double[] hsv = new double[3];
        double[] rgb = new double[3];
        int[] rgb_int = new int[3];

        hsv[0] = h;
        hsv[1] = s;
        hsv[2] = v;

        if (hsv[0] == -1) {
            rgb[0] = rgb[1] = rgb[2] = hsv[2];

            for (int j = 0; j < 3; j++)
                rgb_int[j] = (int) Math.ceil(rgb[j] * 255);

            return rgb_int;
        }

        i = (int) (Math.floor(hsv[0]));
        f = hsv[0] - i;

        if (i % 2 == 0)
            f = 1 - f;

        m = hsv[2] * (1 - hsv[1]);
        n = hsv[2] * (1 - hsv[1] * f);

        switch (i) {
            case 6:
            case 0:
                rgb[0] = hsv[2];
                rgb[1] = n;
                rgb[2] = m;
                break;
            case 1:
                rgb[0] = n;
                rgb[1] = hsv[2];
                rgb[2] = m;
                break;
            case 2:
                rgb[0] = m;
                rgb[1] = hsv[2];
                rgb[2] = n;
                break;
            case 3:
                rgb[0] = m;
                rgb[1] = n;
                rgb[2] = hsv[2];
                break;
            case 4:
                rgb[0] = n;
                rgb[1] = m;
                rgb[2] = hsv[2];
                break;
            case 5:
                rgb[0] = hsv[2];
                rgb[1] = m;
                rgb[2] = n;
                break;
        }

        for (int j = 0; j < 3; j++)
            rgb_int[j] = (int) Math.ceil(rgb[j] * 255);

        return rgb_int;
    }
}