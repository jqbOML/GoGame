package GraGo.Klient;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIWynik extends AbstractWynik {
    GUIWynik(){
        JFrame zakonczenie = new JFrame("Gracz biały: Podaj wyniki");
        WynikGracza = new JTextArea(1, 10);
        WynikPrzeciwnika = new JTextArea(1, 10);
        okButton = new JButton("OK");

        zakonczenie.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        zakonczenie.setSize(400, 100);
        zakonczenie.setLocationRelativeTo(null);
        zakonczenie.setVisible(true);
        zakonczenie.setTitle("Podsumowanie: Gracz biały");
        zakonczenie.add(WynikGracza, BorderLayout.WEST);
        zakonczenie.add(okButton, BorderLayout.CENTER);
        WynikGracza.setBorder(new TitledBorder("TWÓJ WYNIK"));
        zakonczenie.add(WynikPrzeciwnika, BorderLayout.EAST);
        WynikPrzeciwnika.setBorder(new TitledBorder("WYNIK PZECIWNIKA"));
    }
}
