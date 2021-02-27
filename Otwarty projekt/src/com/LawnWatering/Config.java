package com.LawnWatering;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;
import javax.swing.JFrame;

public class Config {
    private static final String config_filename = "config.txt";

    static public void createNewProperties() {
        deleteFile();
        Properties props = new Properties();

        props.setProperty("app_name", "Lawn Watering Madness");
        props.setProperty("fullscreen", "false");

        props.setProperty("resources_dirname", "resources");
        props.setProperty("frames_workdir", "temp");
        props.setProperty("output_dirname", "output");

        props.setProperty("txt_path", props.getProperty("output_dirname") + File.separator + "koordynaty.txt");
        props.setProperty("img_path", props.getProperty("output_dirname") + File.separator + "trawnik.jpg");
        props.setProperty("gif_path", props.getProperty("output_dirname") + File.separator + "animation.gif");
        props.setProperty("frame_path",
                props.getProperty("resources_dirname") + File.separator + props.getProperty("frames_workdir"));

        props.setProperty("window_icon_path", props.getProperty("resources_dirname") + File.separator + "icon.png");

        /* Settings */
        props.setProperty("Cycles_amount", "0");
        props.setProperty("Cycles_period", "0");
        props.setProperty("Reflections", "true");
        props.setProperty("Input_filename", "null");

        props.setProperty("success", "false");

        saveToFile(props);
    }

    public static void saveToFile(Properties config) {
        Path PropertyFile = Paths.get(config_filename);
        try {
            Writer propWriter = Files.newBufferedWriter(PropertyFile);
            config.store(propWriter, "Lawn Watering - Application Properties");
            propWriter.close();
        } catch (IOException Ex) {
            Error.popErrorWindow("Nastąpił problem przy zapisywaniu ustawień do pliku " + config_filename + ".",
                    JFrame.EXIT_ON_CLOSE, true);
        }
    }

    public static void deleteFile() {
        File config = new File(config_filename);
        config.delete();
    }

    public static Properties readFile() {
        Properties my_props = new Properties();
        Path propertyFile = Paths.get(config_filename);

        try {
            Reader propReader = Files.newBufferedReader(propertyFile);
            my_props.load(propReader);
            propReader.close();
        } catch (IOException ex) {
            Error.popErrorWindow("Nastąpił problem przy czytaniu ustawień z pliku " + config_filename + ".",
                    JFrame.EXIT_ON_CLOSE, true);
        }

        return my_props;
    }

    public static void set(String key, String value) {
        Properties current_props = readFile();
        current_props.setProperty(key, value);
        saveToFile(current_props);
    }

    public static String get(String key) {
        return readFile().getProperty(key);
    }

    public static String getConfigFilename() {
        return config_filename;
    }
}
