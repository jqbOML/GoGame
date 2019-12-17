package GraGo.Serwer;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Gracz extends AbstractGracz implements Runnable  {
    Gracz(Socket socket, int kolor, Serwer serwer) {
        this.serwer = serwer;
        this.socket = socket;
        this.kolor = kolor;
        System.out.println("Kolor gracza: "+kolor);
    }

    @Override
    public void run() {
        try {
            serwer.polaczenieZGraczami(this);
            serwer.interpretujKomendy(this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            serwer.wyjscieZGry(this);
        }
    }
}
