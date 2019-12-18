package GraGo.Serwer;

import java.io.IOException;
import java.net.ServerSocket;

public class Main{
    public static void main(String[] args) throws Exception {

        Serwer serwerGo = new Serwer();
        new Thread(serwerGo).start();
        try (ServerSocket listener = new ServerSocket(58902)) {
            System.out.println("Serwer Go aktywny...");
            while (true) {
                Gracz gracz = new Gracz(listener.accept(),serwerGo);
                new Thread(gracz).start();
                serwerGo.oczekujacyGracze.add(gracz);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
