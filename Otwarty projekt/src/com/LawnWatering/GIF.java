package com.LawnWatering;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.*;
import javax.imageio.stream.*;
import javax.swing.*;

public class GIF extends Thread implements Sprinklers {
    MainWindow gui;
    Lawn lawn;
    SprinklersList list;
    Thread thr;

    static int frames_number = 1;
    static long t;
    static int n;

    boolean reflections = false;

    static int cycles_counter = 0;

    public GIF(MainWindow window) {
        this.gui = window;
        this.lawn = gui.lawn;
        this.list = gui.list;

        /* Brzydko, ale przyśpiesza wydajność */
        if (Config.get("Reflections").equals("true"))
            this.reflections = true;

        thr = new Thread(this, "GIF");

        GIF.t = (int) (Double.parseDouble(Config.get("Cycles_period")) * 1000);
        GIF.n = Integer.parseInt(Config.get("Cycles_amount"));

        thr.start();
    }

    @Override
    public void run() {
        this.setPriority(Thread.MAX_PRIORITY);

        long time1;
        while (frames_number - 1 < n) {
            gui.printNewAction("Generuję klatkę " + frames_number + "/" + n);
            time1 = System.currentTimeMillis();

            drawCircles();

            try {
                gui.updateStdv();
                gui.updateAverage();
            } catch (ArithmeticException ae) {
                Error.popErrorWindow("Nastąpiło dzielenie przez zero (ilość komórek w tablicy trawnika).",
                        JFrame.EXIT_ON_CLOSE, true);
            }

            if (System.currentTimeMillis() - time1 < t) {
                try {
                    Thread.sleep(time1 + t - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    // ignore
                }
            } else {
                gui.printNewAction("Uwaga! Następują opóźnienia w generowaniu kolejnej klatki.");
            }

            lawn.update_gif_lawn();

            var output = new Output();
            output.export_jpg(lawn.gif_lawn, lawn.getAverage(), lawn.getstdv(),
                    Config.get("frame_path") + File.separator + "frame" + Integer.toString(GIF.frames_number) + ".jpg");

            gui.image_label.setIcon(new ImageIcon(Config.get("frame_path") + File.separator + "frame"
                    + Integer.toString(GIF.frames_number) + ".jpg"));
            frames_number++;
        }

        gui.stop.setEnabled(false);
        gui.launch.setEnabled(false);
        Config.set("success", "true");
        thr.stop();
    }

    public static void generateGIF() {
        try {
            BufferedImage first = ImageIO.read(new File(Config.get("frame_path") + File.separator + "frame.jpg"));
            ImageOutputStream output = new FileImageOutputStream(new File(Config.get("gif_path")));

            GifSequenceWriter writer = new GifSequenceWriter(output, first.getType(), (int) t, true);
            writer.writeToSequence(first);

            File[] images = new File[frames_number];
            for (int i = 0; i < frames_number; i++) {
                images[i] = new File(
                        Config.get("frame_path") + File.separator + "frame" + Integer.toString(i + 1) + ".jpg");
            }

            for (int i = 0; i < frames_number - 1; i++) {
                System.out.println("Klatka nr. " + (i + 1));
                BufferedImage next = ImageIO.read(images[i]);
                writer.writeToSequence(next);
            }

            writer.close();
            output.close();
        } catch (IOException ex) {
            Error.popErrorWindow("Nastąpił błąd przy tworzeniu pliku .gif.", JFrame.DISPOSE_ON_CLOSE, true);
        }
    }

    public static void createWorkdir() {
        Output output = new Output();

        if (!output.create_dir(Config.get("frame_path"))) {
            Error.popErrorWindow("Nie udało się utworzyć folderu " + Config.get("frame_path"), JFrame.EXIT_ON_CLOSE,
                    true);
        }
    }

    public static void cleanWorkdir() {
        File workdir = new File(Config.get("frame_path"));
        for (File file : workdir.listFiles()) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }

        workdir.delete();
    }

    public void drawCircles() {
        var drawer = new Drawer();
        ArrayList<Sprinkler> sprinklers_to_draw = new ArrayList<>();

        /*
         * Wybranie typów zraszaczy z listy, które mają zostać "uruchomione" w danym
         * cyklu
         */
        for (int i = 0; i < list.getList().size(); i++) {
            Sprinkler spr = list.getList().get(i);

            if (spr.getType() == SprinklerType.QUARTER)
                sprinklers_to_draw.add(spr);
            if (GIF.cycles_counter % 2 == 0 && spr.getType() == SprinklerType.HALF)
                sprinklers_to_draw.add(spr);
            if (GIF.cycles_counter % 3 == 0 && GIF.cycles_counter != 0 && spr.getType() == SprinklerType.THREE_QUARTERS)
                sprinklers_to_draw.add(spr);
            if (GIF.cycles_counter % 4 == 0 && GIF.cycles_counter != 0 && spr.getType() == SprinklerType.FULL)
                sprinklers_to_draw.add(spr);
        }

        if (this.reflections) {
            for (Sprinkler sprinkler : sprinklers_to_draw)
                drawer.draw_circle_with_reflections(this.lawn.upscale_lawn, sprinkler);
        } else {
            for (Sprinkler sprinkler : sprinklers_to_draw)
                drawer.draw_circle_without_reflections(this.lawn.upscale_lawn, sprinkler);
        }

        sprinklers_to_draw.clear();
        GIF.cycles_counter++;
    }
}
