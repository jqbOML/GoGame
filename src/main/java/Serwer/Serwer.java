package Serwer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Serwer {
    private Kamien[][] plansza_go = new Kamien[19][19];
    private Gracz aktualnyGracz;

    Serwer(){
        try (ServerSocket listener = new ServerSocket(58901)) {
            System.out.println("Serwer Go aktywny...");
            ExecutorService pool = Executors.newFixedThreadPool(100);
            while (true) {
                //socket = listener.accept();
                //Gracz gracz1 = new Gracz(listener.accept(), 2, this);
                //Gracz gracz2 = new Gracz(listener.accept(), 2, this);

                pool.execute(new Gracz(listener.accept(), 1, this)); //czarny
                pool.execute(new Gracz(listener.accept(), 2, this)); //bialy
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void polaczenieZGraczem(Gracz gracz) throws IOException {
        gracz.input = new Scanner(gracz.socket.getInputStream());
        gracz.output = new PrintWriter(gracz.socket.getOutputStream(), true);
        gracz.output.println("WITAJ " + gracz.kolor);
        if (gracz.kolor == 1) {
            gracz.output.println("INFO Oczekuje na przeciwnika");
            ustawAktualnegoGracza(gracz);
        } else {
            gracz.ustawPrzeciwnika(aktualnyGracz);
            aktualnyGracz.ustawPrzeciwnika(gracz);
            aktualnyGracz.output.println("INFO Twoja kolej");
            gracz.output.println("INFO Runda przeciwnika, proszę czekać");
        }
    }

    void interpretujKomendy(Gracz gracz) {
        while (gracz.input.hasNext()) {
            String polecenie = gracz.input.next();
            if (polecenie.startsWith("WYJSCIE")) {
                return;
            } else if (polecenie.startsWith("RUCH")) {
                try {
                    int x = Integer.parseInt(gracz.input.next());
                    int y = Integer.parseInt(gracz.input.next());
                    zweryfikujRuch(x, y, gracz);
                    gracz.output.println("POPRAWNY_RUCH " + x + " " + y);
                    gracz.przeciwnik.output.println("RUCH_PRZECIWNIKA " + x + " " + y);
                    gracz.pass = false;

                /*if () {
                    output.println("ZWYCIESTWO");
                    przeciwnik.output.println("PORAZKA");
                }*/
                } catch (IllegalStateException e) {
                    gracz.output.println("INFO " + e.getMessage());
                }
            }else if (polecenie.startsWith("ZWYCIESTWO")) {
                gracz.output.println("ZWYCIESTWO");
                gracz.przeciwnik.output.println("PORAZKA");
            }else if (polecenie.startsWith("PORAZKA")) {
                gracz.output.println("PORAZKA");
                gracz.przeciwnik.output.println("ZWYCIESTWO");
            }else if (polecenie.startsWith("REMIS")) {
                gracz.output.println("REMIS");
                gracz.przeciwnik.output.println("REMIS");
            }else if (polecenie.startsWith("PASS")) {
                gracz.przeciwnik.output.println("PASS");
                gracz.pass = true;
                ustawAktualnegoGracza(gracz.przeciwnik);
            }else if (polecenie.startsWith("WYNIK")) {
                int a = Integer.parseInt(gracz.input.next());
                int b = Integer.parseInt(gracz.input.next());
                gracz.output.println("WYNIK " + a + " " + b);
            }
            if(gracz.pass && gracz.przeciwnik.pass)
            {
                gracz.output.println("KONIEC_GRY");
                gracz.przeciwnik.output.println("KONIEC_GRY");
            }
        }
    }

    private synchronized void zweryfikujRuch(int x, int y, Gracz gracz) {
        if (gracz != aktualnyGracz) {
            throw new IllegalStateException("Nie Twój ruch!");
        } else if (gracz.przeciwnik == null) {
            throw new IllegalStateException("Nie masz jeszcze przeciwnika");
        } else if (plansza_go[x][y] != null) {
            throw new IllegalStateException("Pole jest już zajęte!");
        } else if (czySamoboj(x, y)) {
            throw new IllegalStateException("Niedozwolony ruch samobojczy!");
        } else if (czyKO(x, y)) {
            throw new IllegalStateException("Niedozwolony ruch KO!");
        }
        plansza_go[x][y] = new Kamien(aktualnyGracz.kolor);
        ustawAktualnegoGracza(aktualnyGracz.przeciwnik);
    }

    private boolean czyKO(int x, int y) {
        //TODO: funkcja sprawdzająca czy podany ruch nie będzie powtórzeniem
        return false;
    }

    private boolean czySamoboj(int x, int y) {
        plansza_go[x][y] = new Kamien(aktualnyGracz.kolor);
        if(plansza_go[x][y].czyOddechLancuch(x, y, plansza_go) == null){
            plansza_go[x][y] = null;
            return false;
        } else{
            plansza_go[x][y] = null;
            return true;
        }

    }

    private void ustawAktualnegoGracza(Gracz gracz){
        this.aktualnyGracz = gracz;
        System.out.println("Aktualny gracz: "+gracz.kolor);
    }

    void wyjscieZGry(Gracz gracz){
        if (gracz.przeciwnik != null && gracz.przeciwnik.output != null) {
            gracz.przeciwnik.output.println("PRZECIWNIK_WYSZEDL");
        }
        try {gracz.socket.close();} catch (IOException ignored) {}
    }
}

