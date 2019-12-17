package GraGo.Serwer;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

abstract class AbstractGracz {
    int kolor; //1 - czarny, 2 - bialy
    Serwer serwer;
    Socket socket;
    Scanner input;
    PrintWriter output;
    Gracz przeciwnik;
    boolean pass;

    void ustawPrzeciwnika(Gracz przeciwnik){
        this.przeciwnik = przeciwnik;
    };
    int wezKolor(){
        return kolor;
    };
}
