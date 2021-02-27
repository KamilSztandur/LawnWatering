package com.LawnWatering;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.swing.border.Border;

public class MainWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    MainWindowEvents events = new MainWindowEvents(this);
    Lawn lawn;
    SprinklersList list;

    Color c_background = new Color(41, 41, 41);
    Color c_text = new Color(255, 255, 255);
    Border border = BorderFactory.createLineBorder(c_background);
    Font f_text = new Font("Abadi MT Condensed Light", Font.PLAIN, 14);

    static String current_action_text = "Czekam na zgodę na rozpoczęcie pracy.";

    /* Interaktywne elementy nasłuchiwaczy NIE PRZENOSIĆ!!!!! */
    // Menu
    JTextField period_value = new JTextField("1", 2);

    // Set On/Off Panel
    JButton launch = new JButton("Rozpocznij");
    JButton stop = new JButton("Przerwij");

    // Image Panel
    ImageIcon img = new ImageIcon(Config.get("frame_path") + File.separator + "frame.jpg");
    JLabel image_label = new JLabel(img);

    // Stats Panel
    JTextField sprinkler_amount = new JTextField("0", 5);
    JTextField average = new JTextField("0", 5);
    JTextField stdv = new JTextField("0", 5);

    // Current Action Panel
    JTextArea action_history = new JTextArea(current_action_text, 6, 40);

    /* ******************************************************** */

    public MainWindow(Lawn lawn, SprinklersList list) {
        this.lawn = lawn;
        this.list = list;
        setTitle(Config.get("app_name"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 695);
        setResizable(false);

        JMenuBar menubar = new WindowMenuBar(this);
        setJMenuBar(menubar);

        ImageIcon icon = new ImageIcon(Config.get("window_icon_path"));
        setIconImage(icon.getImage());

        JPanel mainPanel = new JPanel();
        BoxLayout mainLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(mainLayout);
        mainPanel.setBackground(c_background);
        add(mainPanel);

        var onOff = setOnOffPanel();
        var image = setImagePanel();
        var stats = setStatsPanel();
        var currentAction = setCurrentActionPanel();

        JPanel blank_panel = new JPanel();
        JLabel blank = new JLabel("", JLabel.CENTER);
        blank_panel.add(blank);
        blank_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        blank_panel.setBackground(c_background);

        mainPanel.add(onOff);
        mainPanel.add(image);
        mainPanel.add(stats);
        mainPanel.add(currentAction);
        mainPanel.add(blank_panel);

        /* Uruchomienie okna na środku ekranu */
        setLocationRelativeTo(null);
        setVisible(true);

        if (Config.get("fullscreen").equals("true")) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setUndecorated(true);
        }
    }

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // Ignore
        }
    }

    private JPanel setImagePanel() {
        JPanel main = new JPanel();
        main.setLayout(new FlowLayout(FlowLayout.CENTER));
        main.setBackground(c_background);

        image_label.setBounds(10, 10, 800, 400);

        main.add(image_label);

        return main;
    }

    private JPanel setCurrentActionPanel() {
        JPanel main = new JPanel();
        FlowLayout mainLayout = new FlowLayout();
        main.setBackground(c_background);

        action_history.setEditable(false);
        action_history.setFont(new Font("Arial", Font.ITALIC, 13));
        action_history.setLineWrap(true);
        action_history.setWrapStyleWord(true);

        JScrollPane action_history_lab = new JScrollPane(action_history, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        action_history_lab.setBorder(border);

        main.add(action_history_lab);
        main.setLayout(mainLayout);

        return main;
    }

    private JPanel setOnOffPanel() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttons.setBackground(c_background);

        stop.setEnabled(false);
        stop.addActionListener(events);
        stop.setBackground(c_background);
        stop.setFont(f_text);
        stop.setForeground(c_text);

        launch.addActionListener(events);
        launch.setBackground(c_background);
        launch.setFont(f_text);
        launch.setForeground(c_text);

        buttons.add(launch);
        buttons.add(stop);

        return buttons;
    }

    private JPanel setStatsPanel() {
        JPanel main = new JPanel();
        main.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        main.setBackground(c_background);

        JLabel sprinkler_amount_header = new JLabel("Liczba zraszaczy:", JLabel.RIGHT);
        sprinkler_amount_header.setFont(f_text);
        sprinkler_amount_header.setForeground(c_text);
        sprinkler_amount.setEditable(false);

        JLabel average_header = new JLabel("Średnia nawodnienia:", JLabel.RIGHT);
        average_header.setFont(f_text);
        average_header.setForeground(c_text);
        average.setEditable(false);

        JLabel stdv_header = new JLabel("Odchylenie standardowe:", JLabel.RIGHT);
        stdv_header.setFont(f_text);
        stdv_header.setForeground(c_text);
        stdv.setEditable(false);

        main.add(sprinkler_amount_header);
        main.add(sprinkler_amount);

        main.add(average_header);
        main.add(average);

        main.add(stdv_header);
        main.add(stdv);

        return main;
    }

    public void printNewAction(String action) {
        current_action_text = action + "\n" + current_action_text;
        action_history.setText(current_action_text);
    }

    public void updateSprinklersAmount(int n) {
        sprinkler_amount.setText("" + n);
    }

    public void updateAverage() {
        lawn.updateAverage();
        average.setText("" + lawn.getAverage());
    }

    public void updateStdv() {
        lawn.updateStdv();
        stdv.setText("" + lawn.getstdv());
    }
}
