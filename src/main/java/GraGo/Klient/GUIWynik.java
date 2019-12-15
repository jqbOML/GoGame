package GraGo.Klient;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIWynik extends JPanel {
    JTextArea podajWynikTy = new JTextArea(1, 10);
    JTextArea podajWynikOn = new JTextArea(1, 10);
    JButton okButton = new JButton("OK");
    JFrame zakonczenie = new JFrame("Podaj wyniki");

    GUIWynik(){

            zakonczenie.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            zakonczenie.setSize(400, 100);
            zakonczenie.setLocationRelativeTo(null);
            zakonczenie.setVisible(true);
            zakonczenie.setTitle("Podsumowanie: Gracz biały");
            zakonczenie.add(podajWynikTy, BorderLayout.WEST);
            zakonczenie.add(okButton, BorderLayout.CENTER);
            podajWynikTy.setBorder(new TitledBorder("TWÓJ WYNIK"));
            zakonczenie.add(podajWynikOn, BorderLayout.EAST);
            podajWynikOn.setBorder(new TitledBorder("WYNIK PZECIWNIKA"));
    }
}
