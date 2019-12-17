package GraGo.Klient;

import javax.swing.*;
import java.awt.*;

abstract class AbstractGra extends JPanel {
    JFrame ramka;
    static int ROZMIAR_PLANSZY;
    static int ROZMIAR_POLA;
    int kolorGracza; //1 - czarny, 2 - biały

    /**
     * belkaStatusu - wyświetlanie komunikatów INFO z serwera
     */
    JLabel belkaStatusu;

    /**
     * Każdy JLabel pole odpowiada jednemu przecięciu na planszy, gdzie kładzione są kamienie
     */
    JLabel[][] pole;

    /**
     * Pomocnicza tablica planszaKamieni[][] do wczytywania odpowiedniej grafiki
     * 0 - puste pole, 1 - czarny kamien, 2 - biały kamien
     */
    int[][] planszaKamieni = new int[ROZMIAR_PLANSZY][ROZMIAR_PLANSZY];
    GUIPlansza.Tekstury tekstury;
    JButton passButton;
    JButton zakonczGreButton;

    static int obliczRozmiarPola(){
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); //pobranie parametrów rozdzielczości ekranu
        return (d.height / ROZMIAR_PLANSZY);
    }

    abstract void ustawKolorGracza(int kolor);
}
