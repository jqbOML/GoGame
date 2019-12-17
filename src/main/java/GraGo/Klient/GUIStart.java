package GraGo.Klient;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GUIStart extends AbstractStart {
    GUIStart()
    {
        oknoStartowe = new JFrame("Gra GO okno startowe");
        botButton = new JButton("BOT");
        przeciwnikButton = new JButton("PRZECIWNIK");
        oknoStartowe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        oknoStartowe.setSize(300, 100);
        oknoStartowe.setLocationRelativeTo(null);
        oknoStartowe.setVisible(true);

        oknoStartowe.add(przeciwnikButton);
        oknoStartowe.add(botButton);
        oknoStartowe.setLayout(new FlowLayout());
    }

}
