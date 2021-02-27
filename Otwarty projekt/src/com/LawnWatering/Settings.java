package com.LawnWatering;

import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Settings implements ActionListener, ItemListener {
    SettingsWindow gui;
    File input_file;

    public Settings(SettingsWindow in) {
        gui = in;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if (command.equals("Zatwierdź")) {
            if (input_file == null) {
                Error.popErrorWindow("Wskaż plik txt z opisem trawnika.", JFrame.DISPOSE_ON_CLOSE, false);
                return;
            } else {
                Config.set("Input_filename", input_file.getAbsolutePath());
            }

            if (gui.reflections_switch.isSelected())
                Config.set("Reflections", "true");
            else
                Config.set("Reflections", "false");

            try {
                String t = gui.period_value.getText();
                String n = gui.amount.getText();

                /* Weryfikacja czy wpisany przez użytkownika tekst jest liczbą */
                Math.abs(Double.parseDouble(t));
                Math.abs(Integer.parseInt(n));

                Config.set("Cycles_period", t);
                Config.set("Cycles_amount", n);
                gui.dispose();
            } catch (NumberFormatException e) {
                Error.popErrorWindow("Proszę uzupełnić ilość oraz czas trwania cykli wyłącznie liczbami.",
                        JFrame.DISPOSE_ON_CLOSE, false);
            }

            return;
        }

        if (command.equals(">>")) {
            modifyField(gui.amount, 4);
            return;
        }

        if (command.equals(">")) {
            modifyField(gui.amount, 3);
            return;
        }

        if (command.equals("<")) {
            modifyField(gui.amount, 2);
            return;
        }

        if (command.equals("<<")) {
            modifyField(gui.amount, 1);
            return;
        }

        if (command.equals("Wybierz") || command.equals(Config.get("Input_filename"))) {
            getFile();
            gui.choosen_file.setText(input_file.getName());
        }

    }

    private void modifyField(JTextField field, int mode) {
        int n = 0;

        switch (mode) {
            case 1:
                n = -10;
                break;
            case 2:
                n = -1;
                break;
            case 3:
                n = 1;
                break;
            case 4:
                n = 10;
                break;
        }
        int num = Integer.parseInt(field.getText());
        if (num + n < 0)
            num = 0;
        else
            num += n;
        field.setText("" + num);
    }

    void getFile() {
        JFileChooser browser = new JFileChooser(new java.io.File("."));
        FileNameExtensionFilter onlytxt = new FileNameExtensionFilter("Pliki tekstowe (.txt .text)", "txt", "text");
        browser.setFileFilter(onlytxt);

        int returnValue = browser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = browser.getSelectedFile();
            input_file = selectedFile;
            Config.set("Input_filename", input_file.getAbsolutePath());
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            gui.warning_message.setText("<html> <font size=3> <b> ⚠ Ta opcja znacząco wpływa na wydajność!</font></b>");
        } else {
            gui.warning_message.setText("");
        }
    }
}