package com.LawnWatering;

import java.io.*;
import javax.swing.JFrame;

public class Lawn {
    File input_file;
    /* Wielkość rzeczywista trawnika (output img) */
    private final int HEIGHT = 4000;
    private final int WIDTH = 8000;

    /* Wielkość trawnika w pliku txt */
    private final int INPUT_HEIGHT = 40;
    private final int INPUT_WIDTH = 80;

    /* Wielkość trawnika w animacji */
    private final int GIF_HEIGHT = 400;
    private final int GIF_WIDTH = 800;

    public int[][] parse_lawn = new int[INPUT_HEIGHT][INPUT_WIDTH];
    public int[][] gif_lawn = new int[GIF_HEIGHT][GIF_WIDTH];
    public int[][] upscale_lawn = new int[HEIGHT][WIDTH];

    private int average = 0;
    private int stdv = 0;

    public Lawn( File input_file ) {
        this.input_file = input_file;
        try {
            parse_shape();
            upscale_shape();
        } catch (final FileNotFoundException FNFe) {
            Error.popErrorWindow("Nie odnaleziono pliku z opisem trawnika.", JFrame.EXIT_ON_CLOSE, true);
        } catch (final IOException IOe) {
            Error.popErrorWindow("Wystąpił problem przy czytaniu wejściowego pliku txt", JFrame.EXIT_ON_CLOSE, true);
        }
    }

    /* Function creating parsed lawn shape for algorithm optimization */
    private void parse_shape() throws FileNotFoundException, IOException {
        FileReader fr = new FileReader( input_file );
        BufferedReader br = new BufferedReader(fr);

        String line;
        for (int y = 0; y < this.INPUT_HEIGHT; y++) {
            line = br.readLine();
            char[] lineChars = line.toCharArray();

            for (int x = 0; x < this.INPUT_WIDTH; x++) {
                if (lineChars[x] == '*' || lineChars[x] == '-') {
                    this.parse_lawn[y][x] = (lineChars[x] == '*') ? 0 : -1;
                } else {
                    Error.popErrorWindow("W pliku wejściowym pojawiły się niedozwolone znaki.", JFrame.EXIT_ON_CLOSE,
                            false);
                }
            }
        }
    }

    /* Function upscaling parsed lawn shape */
    private void upscale_shape() {
        for (int y = 0; y < this.INPUT_HEIGHT; y++) {
            for (int x = 0; x < this.INPUT_WIDTH; x++) {
                for (int l = 0; l < 100; l++) {
                    for (int m = 0; m < 100; m++) {
                        this.upscale_lawn[100 * y + l][100 * x + m] = this.parse_lawn[y][x];
                    }
                }
            }
        }
    }

    /*
     * NAJLEPSZE ALE BARDZO CZASOCHŁONNE !!!!!!!!!!!!!!!!!!!!!!!!!! public void
     * update_gif_lawn(){ int[][] lawn_fragment = new int[10][10]; int average;
     * 
     * for( int y = 0; y < GIF_HEIGHT; y++ ) { for( int x = 0; x < GIF_WIDTH; x++ )
     * { for( int l = 0; l < 10; l++ ) { for( int m = 0; m < 10; m++) {
     * 
     * for( int fy = 0; fy < 10; fy++ ) for( int fx = 0; fx < 10; fx++ )
     * lawn_fragment[fy][fx] = upscale_lawn[10*y + fy ][10*x + fx];
     * 
     * average = (int) ( Stats.avg( lawn_fragment ) );
     * 
     * this.gif_lawn[y][x] = average; } } } } }
     */

    /*
     * Znacznie bardziej wydajny algorytm, liczący średnią z narożników i pixela
     * środkowego bloku 10x10
     */
    public void update_gif_lawn() {
        int[] lawn_fragment = new int[8];
        int current_average;
        int dx = 10;
        int dy = 10;
        int sum;

        for (int y = 0; y < this.GIF_HEIGHT; y++) {
            for (int x = 0; x < this.GIF_WIDTH; x++) {
                sum = 0;
                /* Poprawa OutOfBorder dla marginesów */
                if (y == this.GIF_HEIGHT - 1) {
                    dy = 9;
                }

                if (x == this.GIF_WIDTH - 1) {
                    dx = 9;
                }

                /* Środek kwadratu 10x10 */
                lawn_fragment[0] = this.upscale_lawn[10 * y + 5][10 * x + 5];
                lawn_fragment[1] = this.upscale_lawn[10 * y + 5][10 * x + 6];
                lawn_fragment[2] = this.upscale_lawn[10 * y + 6][10 * x + 5];
                lawn_fragment[3] = this.upscale_lawn[10 * y + 6][10 * x + 6];

                /* Narożniki kwadratu 10x10 - można wywalić, to zwiększy wydajność o 16% */
                lawn_fragment[4] = this.upscale_lawn[10 * y][10 * x];
                lawn_fragment[5] = this.upscale_lawn[10 * y + dy][10 * x];
                lawn_fragment[6] = this.upscale_lawn[10 * y][10 * x + dx];
                lawn_fragment[7] = this.upscale_lawn[10 * y + dy][10 * x + dx];

                /*
                 * WAŻNE!!! Warto użyć tej funkcji do liczenia średniej z jednowymiarowej
                 * tablicy zamiast funkcji Stats.avg() ze względu na znacznie większą szybkość.
                 */
                for (int j = 0; j < 8; j++) {
                    sum += lawn_fragment[j];
                }
                current_average = (int) (sum / 8);

                /*
                 * Nie trzeba przywracać 10 do y, bo jak dotrze do końca linijek, kończy pracę i
                 * nie przechodzi do "następnej"
                 */
                if (x == GIF_WIDTH - 1) {
                    dx = 10;
                }
                this.gif_lawn[y][x] = current_average;
            }
        }
    }

    public int getAverage() {
        return average;
    }

    public int getstdv() {
        return stdv;
    }

    public void updateAverage() {
        var calculator = new Stats();
        this.average = (int) calculator.avg(this.upscale_lawn);
    }

    public void updateStdv() {
        var calculator = new Stats();
        this.stdv = (int) calculator.std_deviation(this.upscale_lawn, this.average);
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getINPUT_HEIGHT() {
        return INPUT_HEIGHT;
    }

    public int getINPUT_WIDTH() {
        return INPUT_WIDTH;
    }

}
