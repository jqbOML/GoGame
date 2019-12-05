import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
class Serwer {

    private Kamien[][] plansza_go = new Kamien[19][19];
    private Gracz aktualny_gracz;


    class Gracz implements Runnable {
        int kolor; //1 - czarny, 2 - bialy
        Gracz przeciwnik;
        Socket socket;
        Scanner input;
        PrintWriter outputString;
        //OutputStream outputObject;

        public Gracz(Socket socket, int kolor) {
            this.socket = socket;
            this.kolor = kolor;
            System.out.println("Nowy gracz: "+kolor);
        }

        @Override
        public void run() {
            try {
                polaczenie_graczy();
                interpretujKomendy();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (przeciwnik != null && przeciwnik.outputString != null) {
                    przeciwnik.outputString.println("PRZECIWNIK_WYSZEDL");
                }
                try {socket.close();} catch (IOException ignored) {}
            }
        }

        private void polaczenie_graczy() throws IOException {
            input = new Scanner(socket.getInputStream());
            outputString = new PrintWriter(socket.getOutputStream(), true);
            //outputObject = new ObjectOutputStream(socket.getOutputStream());
            outputString.println("WITAJ " + kolor);
            if (kolor == 1) {
                aktualny_gracz = this;
                outputString.println("INFO Oczekuje na przeciwnika");
            } else {
                przeciwnik = aktualny_gracz;
                aktualny_gracz.przeciwnik = this;
                przeciwnik.outputString.println("INFO Twoja kolej");
                this.outputString.println("INFO Runda przeciwnika, proszę czekać");
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
                        zweryfikujRuch(x, y, this);
                        //czy_uduszone(x, y);
                        outputString.println("POPRAWNY_RUCH " + x + " " + y);
                        //outputObject.flush(plansza_go);
                        przeciwnik.outputString.println("RUCH_PRZECIWNIKA " + x + " " + y);


                /*if (false) {
                    outputString.println("ZWYCIESTWO");
                    przeciwnik.outputString.println("PORAZKA");
                }*/
                    } catch (IllegalStateException e) {
                        outputString.println("INFO " + e.getMessage());
                    }
                }
            }
        }

        synchronized void zweryfikujRuch(int x, int y, Gracz gracz) {
            if (gracz != aktualny_gracz) {
                throw new IllegalStateException("Nie Twój ruch!");
            } else if (gracz.przeciwnik == null) {
                throw new IllegalStateException("Nie masz jeszcze przeciwnika");
            } else if (plansza_go[x][y] != null) {
                throw new IllegalStateException("Pole jest już zajęte!");
            } else if (czy_samoboj(x, y)) {
                throw new IllegalStateException("Niedozwolony ruch samobojczy!");
            } else if (czy_ko(x, y)) {
                throw new IllegalStateException("Niedozwolony ruch KO!");
            }
            plansza_go[x][y] = new Kamien(x, y, aktualny_gracz.kolor);
            aktualny_gracz = aktualny_gracz.przeciwnik;
        }
    }

    /*private void czy_uduszone(int x, int y) {
        System.out.println("Czy_uduszone dla x: "+x+", y: "+y);
        if (plansza_go[x + 1][y] != null) {
            if (plansza_go[x + 1][y].kolor == aktualny_gracz.przeciwnik.kolor)
            plansza_go = plansza_go[x + 1][y].czyOddechLancuch(x + 1, y, plansza_go);
        }
        if (plansza_go[x - 1][y] != null) {
            if (plansza_go[x - 1][y].kolor == aktualny_gracz.przeciwnik.kolor)
            plansza_go = plansza_go[x - 1][y].czyOddechLancuch(x - 1, y, plansza_go);
        }
        System.out.println("Sprawdz x = "+x+", y-1 = "+(y-1));
        if (plansza_go[x][y - 1] != null) {
            if (plansza_go[x][y - 1].kolor == aktualny_gracz.przeciwnik.kolor)
            plansza_go = plansza_go[x][y - 1].czyOddechLancuch(x, y - 1, plansza_go);
        }
        if (plansza_go[x][y + 1] != null) {
            if (plansza_go[x][y + 1].kolor == aktualny_gracz.przeciwnik.kolor)
            plansza_go = plansza_go[x][y + 1].czyOddechLancuch(x, y + 1, plansza_go);
        }
    }*/

    private boolean czy_ko(int x, int y) {
        //TODO: funkcja sprawdzająca czy podany ruch nie będzie powtórzeniem
        return false;
    }

    private boolean czy_samoboj(int x, int y) {
        plansza_go[x][y] = new Kamien(x, y, aktualny_gracz.kolor);
        if(plansza_go[x][y].czyOddechLancuch(x, y, plansza_go) == null){
            plansza_go[x][y] = null;
            return false;
        } else{
            plansza_go[x][y] = null;
            return true;
        }

    }
}

