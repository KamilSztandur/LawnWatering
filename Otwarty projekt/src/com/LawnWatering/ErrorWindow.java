package com.LawnWatering;

import java.awt.*;
import javax.swing.*;

public class ErrorWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    ErrorWindowEvents events = new ErrorWindowEvents(this);
    String error_message;

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // Ignore
        }
    }

    public ErrorWindow(String error_message, int mode) {
        this.error_message = error_message;

        setTitle(Config.get("app_name") + " - błąd");
        setDefaultCloseOperation(mode);
        setResizable(false);

        ImageIcon icon = new ImageIcon(Config.get("window_icon_path"));
        setIconImage(icon.getImage());

        JPanel main = new JPanel();
        BoxLayout mainLayout = new BoxLayout(main, BoxLayout.Y_AXIS);
        main.setLayout(mainLayout);
        add(main);

        JPanel blank_panel = new JPanel();
        JLabel blank = new JLabel("", JLabel.CENTER);
        blank_panel.add(blank);

        var error = setErrorPanel();
        var ok = setOkPanel();

        main.add(blank_panel);
        main.add(error);
        main.add(ok);
        add(main);
        pack();

        /* Uruchomienie okna na środku ekranu */
        setLocationRelativeTo(null);

        setVisible(true);
    }

    private JPanel setErrorPanel() {
        JPanel main = new JPanel();

        main.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel resultLabel;
        JLabel resultInfoLabel;

        resultLabel = new JLabel("Błąd!", JLabel.CENTER);
        resultInfoLabel = new JLabel(error_message);

        resultLabel.setFont(new Font("Arial", Font.BOLD, 13));
        resultInfoLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        main.add(resultLabel);
        main.add(resultInfoLabel);

        return main;
    }

    private JPanel setOkPanel() {
        JPanel ok_panel = new JPanel();
        ok_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        var ok_button = new JButton("Rozumiem");
        ok_panel.add(ok_button);

        ok_button.addActionListener(events);

        return ok_panel;
    }
}
