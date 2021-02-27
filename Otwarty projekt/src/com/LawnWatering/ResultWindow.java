package com.LawnWatering;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.Border;

public class ResultWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    Result result = new Result(this);

    Color c_background = new Color(41, 41, 41);
    Color c_text = new Color(255, 255, 255);
    Border border = BorderFactory.createLineBorder(c_background);
    Font f_text = new Font("Abadi MT Condensed Light", Font.PLAIN, 14);

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // Ignore
        }
    }

    @Override
    public Insets getInsets() { // 50 15 0 15
        Insets squeeze = new Insets(0, 0, 0, 0);
        return squeeze;
    }

    public ResultWindow() {
        setTitle("Lawn Watering Madness");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setResizable(false);

        ImageIcon icon = new ImageIcon(Config.get("window_icon_path"));
        setIconImage(icon.getImage());

        JPanel main = new JPanel();
        BoxLayout mainLayout = new BoxLayout(main, BoxLayout.Y_AXIS);
        main.setBackground(c_background);

        main.setLayout(mainLayout);
        add(main);

        JPanel blank_panel = new JPanel();
        JLabel blank = new JLabel("<html><br /><br />", JLabel.CENTER);
        blank_panel.add(blank);
        blank_panel.setBackground(c_background);

        var resultImage = setResultImagePanel();
        var result = setResultPanel();
        var ok = setOkPanel();

        main.add(blank_panel);
        main.add(resultImage);
        main.add(result);
        main.add(ok);

        add(main);
        /* Uruchomienie okna na środku ekranu */
        setLocationRelativeTo(null);

        setVisible(true);

    }

    private JPanel setResultImagePanel() {
        JPanel main = new JPanel();
        main.setLayout(new FlowLayout(FlowLayout.CENTER));
        main.setBackground(c_background);

        var checkmarkLabel = new JLabel("✅", JLabel.CENTER);
        checkmarkLabel.setForeground(c_text);
        checkmarkLabel.setFont( new Font( "Serif", Font.PLAIN,  50));
        main.add(checkmarkLabel);

        return main;
    }

    private JPanel setResultPanel() {
        JPanel main = new JPanel();
        main.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        main.setBackground(c_background);

        JLabel resultInfoLabel;

        resultInfoLabel = new JLabel(
                "<html><strong>Sukces!</strong><br />Plik obrazka 4000x8000 trawnika, animacja podlewania<br />oraz lista zraszaczy znajdują się w folderze <strong>\""
                        + Config.get("output_dirname") + "\"</strong>.",
                JLabel.CENTER);

        resultInfoLabel.setFont(f_text);
        resultInfoLabel.setForeground(c_text);

        main.add(resultInfoLabel);

        return main;
    }

    private JPanel setOkPanel() {
        JPanel ok_panel = new JPanel();
        ok_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        ok_panel.setBackground(c_background);

        var ok_button = new JButton("Ok");
        ok_button.setBackground(c_background);
        ok_button.setFont(f_text);
        ok_button.setForeground(c_text);

        ok_panel.add(ok_button);

        ok_button.addActionListener(result);

        return ok_panel;
    }
}
