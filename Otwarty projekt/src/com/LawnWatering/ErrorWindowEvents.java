package com.LawnWatering;

import java.awt.event.*;
import javax.swing.JFrame;

public class ErrorWindowEvents implements ActionListener {
    ErrorWindow gui;

    public ErrorWindowEvents(ErrorWindow in) {
        gui = in;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("Rozumiem")) {
            if (gui.getDefaultCloseOperation() == JFrame.DISPOSE_ON_CLOSE)
                gui.dispose();
            else
                System.exit(1);
        }
    }

}

class Error {
    /*
     * JFrame.DISPOSE_ON_CLOSE - wyłącza jedynie okno po skrzyżykowaniu
     * JFrame.EXIT_ON_CLOSE - wyłącza program po skrzyżykowaniu
     *
     */
    public static void popErrorWindow(String error_message, int mode, boolean wait) {
        ErrorWindow.setLookAndFeel();
        var inputError = new ErrorWindow(error_message, mode);

        if (wait)
            WindowManager.waitUntilClosed(inputError);
    }
}
