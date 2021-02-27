package com.LawnWatering;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

public class WindowMenuBar extends JMenuBar implements ActionListener {
    private static final long serialVersionUID = 1L;

    JFrame gui;
    String app_name = Config.get("app_name");

    public WindowMenuBar(JFrame window) {
        this.gui = window;

        JMenu lawn_watering = new JMenu(app_name);
        JMenu help = new JMenu("Pomoc");

        /* Lawn Watering */
        var fullscreen_pos = new JMenuItem("Tryb pełnoekranowy");
        fullscreen_pos.addActionListener(this);

        var exit_pos = new JMenuItem("Zakończ");
        exit_pos.addActionListener(this);

        /* Pomoc */
        var startingup_pos = new JMenuItem("Rozpoczęcie pracy z programem");
        startingup_pos.addActionListener(this);

        var authors_pos = new JMenuItem("Twórcy");
        authors_pos.addActionListener(this);

        /* Pomoc - Wyjście */
        JMenu submenu_output = new JMenu("Wyjście");
        var image_pos = new JMenuItem("Wyjściowy obrazek");
        image_pos.addActionListener(this);

        var txt_pos = new JMenuItem("Wyjściowy plik tekstowy");
        txt_pos.addActionListener(this);

        var gif_pos = new JMenuItem("Wyjściowa animacja");
        gif_pos.addActionListener(this);

        submenu_output.add(image_pos);
        submenu_output.add(txt_pos);
        submenu_output.add(gif_pos);

        help.add(startingup_pos);
        help.add(submenu_output);
        help.add(authors_pos);

        lawn_watering.add(fullscreen_pos);
        lawn_watering.add(exit_pos);

        this.add(lawn_watering);
        this.add(help);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if (command.equals("Tryb pełnoekranowy")) {
            if (Config.get("fullscreen").equals("true")) {
                Config.set("fullscreen", "false");
                gui.setExtendedState(JFrame.NORMAL);
                gui.setUndecorated(false);
            } else {
                Config.set("fullscreen", "true");
                gui.setExtendedState(JFrame.MAXIMIZED_BOTH);
                gui.setUndecorated(true);
            }
        }

        if (command.equals("Zakończ")) {
            System.exit(0);
        }

        if (command.equals("Rozpoczęcie pracy z programem")) {
            InfoWindow.setLookAndFeel();
            new InfoWindow(app_name + command // Tytuł okna
                    ,
                    "<html>\n" + "<strong>Dla poprawnego działania programu należy:</strong>\n" + "<ul>"
                            + "<li>wskazać plik txt zawierający <strong>40 linijek po 80 znaków tekstowych</strong> (trawnik <strong>*</strong> lub przeszkoda <strong>-</strong>)\n"
                            + "<li>wprowadzić w odpowiednie pole czas trwania pojedynczego cyklu w sekundach <strong>(ułamki oddzielać kropką)</strong>\n"
                            + "<li>wprowadzić w odpowiednie pole liczbę pożądanych cykli\n" + "</ul>"
                            + "<br /><strong>Przykład:</strong>\n",
                    "sample_txt.png");
        }

        if (command.equals("Wyjściowy obrazek")) {
            InfoWindow.setLookAndFeel();
            new InfoWindow(Config.get("app_name") + " - " + command // Tytuł okna
                    ,
                    "<html>\n" + "<strong>Wyjściowy obrazek:</strong><br /><br />\n"
                            + "Wyjściowy obrazek <strong>jpg</strong> o rozmiarach 4000x8000 pixeli zostanie zapisany w folderze <strong>"
                            + Config.get("output_dirname")
                            + "</strong>. Kolor każdego piksela będzie reprezentować <br />"
                            + "stopień nawodnienia reprezentowanego fragmentu trawnika. Sposób kolorowania opiera się na średniej arytmetycznej i odchyleniu <br />"
                            + "standardowym oraz został dokładnie opisany w dokumentacji dołączonej do programu.<br />"
                            + "<br /><strong>Przykład:</strong>\n",
                    "sample_output_img.jpg");
        }

        if (command.equals("Wyjściowy plik tekstowy")) {
            InfoWindow.setLookAndFeel();
            new InfoWindow(Config.get("app_name") + " - " + command // Tytuł okna
                    ,
                    "<html>\n" + "<strong>Wyjściowy plik tekstowy:</strong><br /><br />\n"
                            + "Zapisany na końcu pracy programu plik tekstowy w folderze <strong>"
                            + Config.get("output_dirname") + "</strong> zawiera <br />"
                            + "listę wszystkich rozstawionych przez algorytm. Znajdują się tam wszystkie potrzebne szczegółowe informacje <br />"
                            + "dot. ich rozstawienia. Jest tam informacja o typie, kierunku oraz koordynatach każdego zraszacza według schematu:<br />"
                            + "<br /><strong>Przykład:</strong>\n",
                    "sample_output_txt.png");
        }

        if (command.equals("Wyjściowa animacja")) {
            InfoWindow.setLookAndFeel();
            new InfoWindow(Config.get("app_name") + " - " + command // Tytuł okna
                    ,
                    "<html>\n" + "<strong>Wyjściowa animacja:</strong><br /><br />\n"
                            + "Wyjściowa animacja stanowi zapis w formacie gif (o wymiarach 400x800) animacji wyświetlanej w oknie w czasie pracy programu. Zaletą takiego<br />"
                            + "rozwiązania jest korekta czasu wyświetlania kolejnych klatek, która w przypadku animacji na żywo może ulec spowolnieniu przez<br />"
                            + "ograniczenia sprzętowe użytkownika.<br />\n<br />"
                            + "<br /><strong>Przykład:</strong>\n",
                    "sample_animation.gif");
        }

        if (command.equals("Twórcy")) {
            InfoWindow.setLookAndFeel();
            new InfoWindow(Config.get("app_name") + " - " + command // Tytuł okna
                    , "<html>\n" + "<strong>Twórcy:</strong>\n" + "<ul>" + "<li>Jakub Grenda 307345\n"
                            + "<li>Robert Odrowąż-Sypniewski 298893\n" + "<li>Kamil Sztandur 307354\n" + "</ul>",
                    null);
        }
    }
}

class InfoWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    String info_message;
    String image_name;

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // Ignore
        }
    }

    public InfoWindow(String title, String info_message, String image_name) {
        this.info_message = info_message;
        this.image_name = image_name;

        setTitle(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        ImageIcon icon = new ImageIcon(Config.get("window_icon_path"));
        setIconImage(icon.getImage());

        JPanel main = new JPanel();
        BoxLayout mainLayout = new BoxLayout(main, BoxLayout.Y_AXIS);
        main.setLayout(mainLayout);

        JPanel blank_panel = new JPanel();
        JLabel blank = new JLabel("", JLabel.CENTER);
        blank_panel.add(blank);

        var info = setInfoPanel();
        var sample_image = setSampleImagePanel();

        main.add(blank_panel);
        main.add(info);
        main.add(sample_image);
        add(main);

        pack();
        /* Uruchomienie okna na środku ekranu */
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel setInfoPanel() {
        JPanel main = new JPanel();
        main.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel info;

        info = new JLabel(info_message, JLabel.LEFT);
        info.setFont(new Font("Arial", Font.PLAIN, 13));

        main.add(info);

        return main;
    }

    private JPanel setSampleImagePanel() {
        JPanel main = new JPanel();
        main.setLayout(new FlowLayout(FlowLayout.CENTER));

        ImageIcon result_image = new ImageIcon(Config.get("resources_dirname") + File.separator + image_name);
        JLabel imageLabel = new JLabel(result_image);

        main.add(imageLabel);

        return main;
    }
}
