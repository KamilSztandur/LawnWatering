package com.LawnWatering;

import java.awt.event.*;

public class Result implements ActionListener {
    ResultWindow gui;

    public Result(ResultWindow in) {
        gui = in;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("Ok")) {
            gui.dispose();
            System.exit(0);
        }
    }

}
