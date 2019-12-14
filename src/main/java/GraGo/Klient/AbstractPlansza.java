package GraGo.Klient;

import javax.swing.*;

abstract class AbstractPlansza extends JPanel {
    static int ROZMIAR_PLANSZY; //ROZMIAR_PLANSZY = 'x' - dla planszy o 'x' pól szerokości i 'x' wysokości
    int kolorGracza; //1 - czarny, 2 - biały
    JFrame ramka;
    JLabel belkaStatusu; //wyświetlanie komunikatów INFO
    JButton passButton;
    JButton zakonczGreButton;
    JLabel[][] pole;
    static int ROZMIAR_POLA;
    /**
     * Tablica planszaKamieni[ROZMIAR_PL][]
     * 0 - puste pole, 1 - czarny kamien, 2 - biały kamien
     */
    int[][] planszaKamieni = new int[ROZMIAR_PLANSZY][ROZMIAR_PLANSZY];
    GUIPlansza.Tekstury tekstury = new GUIPlansza.Tekstury();
}
