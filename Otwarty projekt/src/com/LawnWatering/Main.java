package com.LawnWatering;

import java.io.File;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        Config.createNewProperties();
        var output = new Output();
        var sprinklers_list = new SprinklersList();
        
        /* Uruchomienie okna ustawień */
        SettingsWindow.setLookAndFeel();
        var settingsWindow = new SettingsWindow();
        WindowManager.waitUntilClosed(settingsWindow);

        var lawn = new Lawn( settingsWindow.settings.input_file );

        /* Wygenerowanie pierwszej klatki */
        lawn.update_gif_lawn();
        GIF.createWorkdir();
        output.export_jpg(lawn.gif_lawn, lawn.getAverage(), lawn.getstdv(),
                Config.get("frame_path") + File.separator + "frame.jpg");

        /* Uruchomienie okna z animacją */
        MainWindow.setLookAndFeel();
        var mainWindow = new MainWindow(lawn, sprinklers_list);

        /* Czekanie aż program zakończy rozstawianie zraszaczy i animację */
        while (Config.get("success").equals("false")) {
            try {
                Thread.sleep(100);
                System.out.println("I'm waiting until launch");
            } catch (InterruptedException e) {
                // Ignore
            }
        }

        /* Rozpoczęcie generowania outputu */
        if (!output.create_dir(Config.get("output_dirname"))) {
            Error.popErrorWindow("Nie udało się utworzyć folderu " + Config.get("output_dirname"), JFrame.EXIT_ON_CLOSE,
                    true);
        }

        mainWindow.printNewAction("Eksportuję plik " + Config.get("txt_path") + " z listą zraszaczy");
        mainWindow.updateAverage();
        mainWindow.updateStdv();
        output.export_txt(sprinklers_list.getList());
        mainWindow.printNewAction("Sukces.");

        mainWindow.printNewAction("Eksportuję obrazek " + Config.get("img_path"));
        output.export_jpg(lawn.upscale_lawn, lawn.getAverage(), lawn.getstdv(), Config.get("img_path"));
        mainWindow.printNewAction("Sukces.");

        mainWindow.printNewAction("Zapisuję animację " + Config.get("gif_path") + "...");
        GIF.generateGIF();
        mainWindow.printNewAction("Sukces.");

        mainWindow.printNewAction("Kończę pracę...");
        GIF.cleanWorkdir();

        /* Pokazanie końcowego okna i posprzątanie */
        ResultWindow.setLookAndFeel();
        new ResultWindow();
        Config.deleteFile();
        mainWindow.printNewAction("Sukces.");
    }   
}
