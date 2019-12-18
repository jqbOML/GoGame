package GraGo.Serwer;

import java.io.IOException;
import java.net.Socket;

public class Gracz extends AbstractGracz implements Runnable  {
    boolean chceGracZBotem;
    boolean wGrze = false;
    boolean gotowDoGry = false;
    public Gracz(Socket socket, int kolor, Serwer serwer) {
        this.serwer = serwer;
        this.socket = socket;
        this.kolor = kolor;
        System.out.println("Nowy gracz: "+kolor);
    }
    public Gracz(Socket socket,  Serwer serwer) {
        this.serwer = serwer;
        this.socket = socket;


    }

    @Override
    public void run() {
        try {
            serwer.polaczenieZGraczami(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(kolor==-1){
            synchronized (this){
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            gra.interpretujKomendy(this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            gra.wyjscieZGry(this);
        }
    }
}
