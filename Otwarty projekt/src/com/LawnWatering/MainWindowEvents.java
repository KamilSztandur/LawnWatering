package com.LawnWatering;

import java.awt.event.*;

public class MainWindowEvents implements ActionListener {
    MainWindow gui;
    GIF gif;

    public MainWindowEvents(MainWindow in) {
        gui = in;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if (command.equals("Rozpocznij")) {
            gui.printNewAction("Rozpocząłem pracę programu...");
            gui.launch.setEnabled(false);

            var algorithm = new Algorithm(gui);
            algorithm.setSprinklers();

            gui.stop.setEnabled(true);

            gif = new GIF(gui);

            gui.launch.setText("Wznów");
            return;
        }

        if (command.equals("Wznów")) {
            gui.launch.setEnabled(false);
            gui.stop.setEnabled(true);
            gif.thr.resume();

            gui.printNewAction("Wznowiłem pracę programu.");
            return;
        }

        if (command.equals("Przerwij")) {
            gui.stop.setEnabled(false);

            gif.thr.suspend();

            gui.launch.setEnabled(true);

            gui.printNewAction("Przerwałem pracę programu.");
            return;
        }
    }
}
