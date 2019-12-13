import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Gracz implements Runnable {
    private Serwer serwer;
    int kolor; //1 - czarny, 2 - bialy
    Gracz przeciwnik;
    private Socket socket;
    private Scanner input;
    private PrintWriter outputString;
    boolean pass;
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
                    outputString.println("POPRAWNY_RUCH " + x + " " + y);
                    serwer.przegladajPlansze();
                    //outputObject.flush(plansza_go);
                    //System.out.println("kamienie do usunięcia:" + Arrays.deepToString(new ArrayList[]{serwer.przegladajPlansze()}));
                    przeciwnik.outputString.println("RUCH_PRZECIWNIKA " + x + " " + y);
                    this.pass = false;

                /*if (false) {
                    outputString.println("ZWYCIESTWO");
                    przeciwnik.outputString.println("PORAZKA");
                }*/
                } catch (IllegalStateException e) {
                    outputString.println("INFO " + e.getMessage());
                }
            }else if (polecenie.startsWith("ZWYCIESTWO")) {
                outputString.println("ZWYCIESTWO");
                przeciwnik.outputString.println("PORAZKA");
            }else if (polecenie.startsWith("PORAZKA")) {
                    outputString.println("PORAZKA");
                    przeciwnik.outputString.println("ZWYCIESTWO");
            }else if (polecenie.startsWith("REMIS")) {
                outputString.println("REMIS");
                przeciwnik.outputString.println("REMIS");
            }
            else if (polecenie.startsWith("PASS")) {
                outputString.println("SPASOWALES");
                przeciwnik.outputString.println("PRZECIWNIK_SPASOWAL");
                this.pass = true;
                serwer.ustawAktualnegoGracza(przeciwnik);
            }
            if(this.pass && przeciwnik.pass)
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
