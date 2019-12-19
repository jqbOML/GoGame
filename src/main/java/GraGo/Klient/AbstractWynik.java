package GraGo.Klient;

import javax.swing.*;

abstract class AbstractWynik {
    JFrame zakonczenie;

    /**
     * Wpisz swoją liczbę punktów
     */
    JTextArea WynikGracza;

    /**
     * Wpisz liczbę punktów przeciwnika
     */
    JTextArea WynikPrzeciwnika;

    /**
     * Wyslij wynik do serwera
     */
    JButton okButton;
}
