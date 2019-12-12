import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Gracz implements Runnable {
    private Serwer serwer;
    int kolor; //1 - czarny, 2 - bialy
    Gracz przeciwnik;
    private Socket socket;
    private Scanner input;
    private PrintWriter outputString;
    boolean passowanie;
    //OutputStream outputObject;

    Gracz(Socket socket, int kolor, Serwer serwer) {
        this.serwer = serwer;
        this.socket = socket;
        this.kolor = kolor;
        System.out.println("Nowy gracz: "+kolor);
    }

    @Override
    public void run() {
        try {
            polaczenieZGraczem();
            interpretujKomendy();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (przeciwnik != null && przeciwnik.outputString != null) {
                System.out.println("EWAKUACJA");
                przeciwnik.outputString.println("PRZECIWNIK_WYSZEDL");
            }
            try {serwer.aktualnyGracz.socket.close();} catch (IOException ignored) {}
        }
    }

    private void polaczenieZGraczem() throws IOException {
        input = new Scanner(socket.getInputStream());
        outputString = new PrintWriter(socket.getOutputStream(), true);
        //outputObject = new ObjectOutputStream(socket.getOutputStream());
        outputString.println("WITAJ " + kolor);
        if (kolor == 1) {
            outputString.println("INFO Oczekuje na przeciwnika");
            serwer.ustawAktualnegoGracza(this);
        } else {
            ustawPrzeciwnika(serwer.aktualnyGracz);
            przeciwnik.ustawPrzeciwnika(this);
            przeciwnik.outputString.println("INFO Twoja kolej");
            outputString.println("INFO Runda przeciwnika, proszę czekać");
        }
    }

    private void interpretujKomendy() {
        while (input.hasNext()) {
            String polecenie = input.next();
            if (polecenie.startsWith("WYJSCIE")) {
                return;
            } else if (polecenie.startsWith("RUCH")) {
                try {
                    int x = Integer.parseInt(input.next());
                    int y = Integer.parseInt(input.next());
                    serwer.zweryfikujRuch(x, y, this);
                    serwer.czy_uduszone(x, y);
                    outputString.println("POPRAWNY_RUCH " + x + " " + y);
                    //outputObject.flush(plansza_go);
                    przeciwnik.outputString.println("RUCH_PRZECIWNIKA " + x + " " + y);
                    this.passowanie = false;

                /*if (false) {
                    outputString.println("ZWYCIESTWO");
                    przeciwnik.outputString.println("PORAZKA");
                }*/
                } catch (IllegalStateException e) {
                    outputString.println("INFO " + e.getMessage());
                }
            } else if (polecenie.startsWith("PASS")) {
                outputString.println("ZPASOWALES");
                przeciwnik.outputString.println("PRZECIWNIK_ZPASOWAL");
                this.passowanie = true;
                serwer.ustawAktualnegoGracza(przeciwnik);
            }
            if(this.passowanie && przeciwnik.passowanie)
            {
                outputString.println("KONIEC_GRY");
                przeciwnik.outputString.println("KONIEC_GRY");
            }
        }
    }

    public void ustawPrzeciwnika(Gracz przeciwnik){
        this.przeciwnik = przeciwnik;
    }
}
