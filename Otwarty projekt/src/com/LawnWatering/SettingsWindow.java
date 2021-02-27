package com.LawnWatering;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

public class SettingsWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    Settings settings = new Settings(this);

    // Color c_background = new Color(24, 0, 82);
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
    public Insets getInsets() {
        Insets squeeze = new Insets(30, 5, 0, 5);
        return squeeze;
    }

    /* Interaktywne elementy nasłuchiwaczy NIE PRZENOSIĆ!!!!! */
    // Period Panel
    JTextField period_value = new JTextField("1", 3);

    // Reflection Panel
    JCheckBox reflections_switch = new JCheckBox("", true);
    JLabel warning_message = new JLabel("");

    // Cycles Panel
    JButton decrease = new JButton("<");
    JButton decrease_x10 = new JButton("<<");
    JTextField amount = new JTextField("0", 3);

    JButton increase = new JButton(">");
    JButton increase_x10 = new JButton(">>");

    // Ok Panel
    JButton ok_button = new JButton("Zatwierdź");

    // Filename PAnel
    JButton choosen_file = new JButton("Wybierz");
    /* ******************************************************** */

    public SettingsWindow() {
        setTitle(Config.get("app_name") + " - ustawienia");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(500, 315);
        setResizable(false);

        ImageIcon icon = new ImageIcon(Config.get("window_icon_path"));
        setIconImage(icon.getImage());

        JMenuBar menubar = new WindowMenuBar(this);
        // menubar.setBackground( Color. );
        setJMenuBar(menubar);

        JPanel main = new JPanel();

        JPanel period_panel = setPeriodPanel();
        JPanel reflection_panel = setReflectionPanel();
        JPanel cycles_panel = setCyclesPanel();
        JPanel ok_panel = setOkPanel();
        JPanel filename_panel = setFilenamePanel();

        JPanel blank_panel = new JPanel();
        JLabel blank = new JLabel("", JLabel.CENTER);
        blank_panel.add(blank);
        blank_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        blank_panel.setBackground(c_background);

        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.add(filename_panel);
        main.add(period_panel);
        main.add(reflection_panel);
        main.add(cycles_panel);
        main.add(ok_panel);
        main.add(blank_panel);

        add(main);
        /* Uruchomienie okna na środku ekranu */
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel setPeriodPanel() {
        JPanel period_panel = new JPanel();
        period_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        period_panel.setBackground(c_background);

        var period_header = new JLabel("Ustaw czas pojedynczego cyklu (sekundy):", JLabel.CENTER);
        period_header.setFont(f_text);
        period_header.setForeground(c_text);

        period_value.setFont(new Font("Arial", Font.PLAIN, 15));
        period_value.setBorder(border);
        period_value.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        period_panel.add(period_header);
        period_panel.add(period_value);

        return period_panel;
    }

    private JPanel setReflectionPanel() {
        JPanel reflection_panel = new JPanel();
        reflection_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        reflection_panel.setBackground(c_background);

        var reflection_header = new JLabel("Odbicia od przeszkód:", JLabel.CENTER);
        reflection_header.setFont(f_text);
        reflection_header.setForeground(c_text);

        reflections_switch.setFont(new Font("Arial", Font.BOLD, 15));
        reflections_switch.setBackground(c_background);

        reflections_switch.addItemListener(settings);
        reflections_switch.setSelected(false);

        warning_message.setForeground(new Color(255, 0, 0));

        reflection_panel.add(reflection_header);
        reflection_panel.add(reflections_switch);
        reflection_panel.add(warning_message);

        return reflection_panel;
    }

    private JPanel setCyclesPanel() {
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(c_background);

        var choice_header_panel = new JPanel();
        choice_header_panel.setBackground(c_background);

        var info_label = new JLabel("Ustaw liczbę cykli:", JLabel.CENTER);
        info_label.setFont(f_text);
        info_label.setForeground(c_text);
        choice_header_panel.add(info_label);

        var choice_panel = new JPanel();
        choice_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        choice_panel.setBackground(c_background);

        amount.setFont(new Font("Arial", Font.PLAIN, 15));
        decrease_x10.addActionListener(settings);
        decrease.addActionListener(settings);
        increase.addActionListener(settings);
        increase_x10.addActionListener(settings);

        increase.setBackground(c_background);
        increase.setFont(f_text);
        increase.setForeground(c_text);

        increase_x10.setBackground(c_background);
        increase_x10.setFont(f_text);
        increase_x10.setForeground(c_text);

        decrease.setBackground(c_background);
        decrease.setFont(f_text);
        decrease.setForeground(c_text);

        decrease_x10.setBackground(c_background);
        decrease_x10.setFont(f_text);
        decrease_x10.setForeground(c_text);

        choice_panel.add(decrease_x10);
        choice_panel.add(decrease);
        choice_panel.add(amount);
        choice_panel.add(increase);
        choice_panel.add(increase_x10);

        main.add(choice_header_panel);
        main.add(choice_panel);

        return main;
    }

    private JPanel setOkPanel() {
        JPanel ok_panel = new JPanel();
        ok_panel.setBackground(c_background);
        ok_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        ok_panel.add(ok_button);

        ok_button.addActionListener(settings);
        ok_button.setBackground(c_background);
        ok_button.setFont(f_text);
        ok_button.setForeground(c_text);
        return ok_panel;
    }

    private JPanel setFilenamePanel() {
        JPanel filename_panel = new JPanel();
        filename_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filename_panel.setBackground(c_background);

        var filename_header = new JLabel("Plik z kształtem trawnika:", JLabel.CENTER);
        filename_header.setFont(f_text);
        filename_header.setForeground(c_text);

        choosen_file.setFont(new Font("Arial", Font.ITALIC, 12));
        choosen_file.addActionListener(settings);

        choosen_file.setBackground(c_background);
        choosen_file.setFont(f_text);
        choosen_file.setForeground(c_text);

        filename_panel.add(filename_header);
        filename_panel.add(choosen_file);

        return filename_panel;
    }
}
