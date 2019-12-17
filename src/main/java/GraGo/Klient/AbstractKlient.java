package GraGo.Klient;

import javax.swing.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

abstract class AbstractKlient {
    Socket socket;
    Scanner in;
    PrintWriter out;
    AbstractStart startGUI;
    AbstractGra planszaGUI;
    AbstractWynik wynikGUI;
    JLabel wybranePole;
    int[] wynik = new int[2];
    String przeciwnik; //Gracz lub Bot

    abstract void odbierajKomendy() throws Exception;
    abstract void wysylajKomendy();
}
