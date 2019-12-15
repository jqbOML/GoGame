package GraGo.Serwer;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Gracz implements Runnable  {
    int kolor; //1 - czarny, 2 - bialy
    private Serwer serwer;
    Socket socket;
    Scanner input;
    PrintWriter output;
    Gracz przeciwnik;
    boolean pass;

    Gracz(Socket socket, int kolor, Serwer serwer) {
        this.serwer = serwer;
        this.socket = socket;
        this.kolor = kolor;
    }

    @Override
    public void run() {
        try {
            serwer.polaczenieZGraczem(this);
            serwer.interpretujKomendy(this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            serwer.wyjscieZGry(this);
        }
    }

    public void ustawPrzeciwnika(Gracz przeciwnik){
        this.przeciwnik = przeciwnik;
    }
}
