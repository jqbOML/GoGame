package GraGo.Klient;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GUIStart extends JPanel {
    JFrame oknoStartowe = new JFrame("Gra GO okno startowe");
    JButton botButton = new JButton("BOT");
    JButton przeciwnikButton = new JButton("PRZECIWNIK");

    GUIStart()
    {
        oknoStartowe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        oknoStartowe.setSize(400, 400);
        oknoStartowe.setBackground(Color.GREEN);
        oknoStartowe.setLocationRelativeTo(null);
        oknoStartowe.setVisible(true);
        oknoStartowe.add(przeciwnikButton, BorderLayout.NORTH);
        oknoStartowe.add(botButton, BorderLayout.SOUTH);

    }

}
