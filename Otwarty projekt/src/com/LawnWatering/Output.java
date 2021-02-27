package com.LawnWatering;

/* Dla pliku jpg */
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/* Dla pliku txt */
import java.io.*;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Output {
    final private String newline = System.getProperty("line.separator");

    public void export_jpg(int[][] lawn, double average, double stdv, String image_filename) {
        try {
            int height = lawn.length;
            int width = lawn[0].length;
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            int[] rgb;
            double max = average + stdv;

            double min;
            if (average - stdv < 0)
                min = 0.0;
            else
                min = average - stdv;

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    switch (lawn[y][x]) {
                        case 0:
                            img.setRGB(x, y, (new Color(255, 255, 255)).getRGB());
                            break;
                        case -1:
                            img.setRGB(x, y, (new Color(0, 0, 0)).getRGB());
                            break;
                        /*
                         * case 1: img.setRGB( x, y, (new Color(0, 255, 0)).getRGB() ); break; case 2:
                         * img.setRGB( x, y, (new Color( 0, 0, 255 )).getRGB() ); break; case 3:
                         * img.setRGB( x, y, (new Color(255, 0, 0)).getRGB() ); break;
                         */
                        default:
                            /*
                             * ? min - max h 60 - 150 s 50 - 75 v 90 - 75
                             */

                            if (lawn[y][x] < min) {
                                rgb = HSVtoRGB.calc(60, 0.5, 0.75);
                            } else if (lawn[y][x] < max) {
                                rgb = HSVtoRGB.calc(((lawn[y][x] - min) / (max - min)) * (150 - 60) + 60 // hue
                                        , (((lawn[y][x] - min) / (max - min)) * (75 - 50) + 50) / 100 // saturation
                                        , (((lawn[y][x] - min) / (max - min)) * (90 - 75) + 75) / 100 // value
                                );
                            } else
                                rgb = HSVtoRGB.calc(150, 0.75, 0.9);

                            img.setRGB(x, y, (new Color(rgb[0], rgb[1], rgb[2])).getRGB());
                    }
                }
            }
            
            File outputfile = new File(image_filename);
            ImageIO.write(img, "jpg", outputfile);
        } catch (IOException e) {
            // Ignoruj
        }
    }

    public void export_txt(ArrayList<Sprinkler> sprinklers_list) {
        File output = new File(Config.get("txt_path"));

        try {
            output.createNewFile();
            var writer = new FileOutputStream(output);

            for (Sprinkler spr : sprinklers_list) {
                if (spr.getType() != Sprinklers.SprinklerType.THREE_QUARTERS) {
                    if (spr.getDir() == Sprinklers.Direction.LEFT || spr.getDir() == Sprinklers.Direction.TOP)
                        writeLine(writer, "Type: " + spr.getType() + "\t\tDirection: " + spr.getDir()
                                + "\t\tLocation: (" + spr.getX() + ", " + spr.getY() + ")");
                    else
                        writeLine(writer, "Type: " + spr.getType() + "\t\tDirection: " + spr.getDir() + "\tLocation: ("
                                + spr.getX() + ", " + spr.getY() + ")");
                } else {
                    if (spr.getDir() == Sprinklers.Direction.LEFT || spr.getDir() == Sprinklers.Direction.TOP)
                        writeLine(writer, "Type: " + spr.getType() + "\tDirection: " + spr.getDir() + "\t\tLocation: ("
                                + spr.getX() + ", " + spr.getY() + ")");
                    else
                        writeLine(writer, "Type: " + spr.getType() + "\tDirection: " + spr.getDir() + "\tLocation: ("
                                + spr.getX() + ", " + spr.getY() + ")");
                }
            }

        } catch (IOException e) {
            Error.popErrorWindow("Nastąpił problem z zapisem koordynatów do pliku " + Config.get("txt_path"),
                    JFrame.EXIT_ON_CLOSE, true);
        }
    }

    private void writeLine(FileOutputStream stream, String lineToWrite) throws IOException {
        lineToWrite += newline;
        byte[] data = lineToWrite.getBytes();
        stream.write(data, 0, data.length);
    }

    public boolean create_dir(String dirname) {
        File output_dir = new File(dirname);
        if (output_dir.exists())
            return true;
        return output_dir.mkdirs();
    }
}