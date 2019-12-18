import GraGo.KomunikatyKlienta;
import GraGo.KomunikatySerwera;
import GraGo.Serwer.Gracz;
import GraGo.Serwer.Serwer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PolaczenieTest  {
    private Serwer serwer = new Serwer();
    Socket socket = new Socket();

    @Test
    public void KomendyDoKlientaTest() throws IOException {


        socket = new Socket("127.0.0.1", 58900);

        Scanner input;
        input = new Scanner(socket.getInputStream());
        String polecenieOdSerwera = input.next(); //polecenie które klient dostaje od serwera po połączeniu się
        assertEquals("WITAJ", polecenieOdSerwera);

    }

   /* @Test
    public void KomendyDoSerweraTest() throws IOException {
        PrintWriter out;
        Gracz graczTest = new Gracz(socket, 1, serwer);
        socket = new Socket("127.0.0.1", 58901);

        out = new PrintWriter(socket.getOutputStream(), true);
        out.println(KomunikatyKlienta.PASS); //klient wysyła wiadomosć

        serwer.interpretujKomendy(graczTest);

        assertEquals(true, graczTest.pass); //polecenie które serwer dostaje od klienta


    }*/
}
