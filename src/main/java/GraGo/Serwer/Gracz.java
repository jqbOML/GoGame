package GraGo.Serwer;

import java.net.Socket;

public class Gracz extends AbstractGracz implements Runnable  {
    public Gracz(Socket socket, int kolor, Serwer serwer) {
        this.serwer = serwer;
        this.socket = socket;
        this.kolor = kolor;
        System.out.println("Nowy gracz: "+kolor);
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
