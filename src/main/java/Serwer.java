import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
class Serwer {

    private Kamien[][] plansza_go = new Kamien[19][19];
    private Gracz aktualny_gracz;

    public synchronized void ruch(int x, int y, Gracz gracz) {
        if (gracz != aktualny_gracz) {
            throw new IllegalStateException("Nie Twój ruch");
        } else if (gracz.przeciwnik == null) {
            throw new IllegalStateException("Nie masz jeszcze przeciwnika");
        } else if (plansza_go[x][y] != null) {
            throw new IllegalStateException("Pole jest już zajęte");
        }
        plansza_go[x][y] = new Kamien(aktualny_gracz.kolor);
        aktualny_gracz = aktualny_gracz.przeciwnik;
    }

    /**
     * A Gracz is identified by a character kolor which is either 'X' - czarny or 'O' - bialy.
     * For communication with the client the gracz has a socket and associated
     * Scanner and PrintWriter.
     */
    class Gracz implements Runnable {
        int kolor; //1 - czarny, 2 - bialy
        Gracz przeciwnik;
        Socket socket;
        Scanner input;
        PrintWriter output;

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
                if (przeciwnik != null && przeciwnik.output != null) {
                    przeciwnik.output.println("PRZECIWNIK_WYSZEDL");
                }
                try {socket.close();} catch (IOException ignored) {}
            }
        }

        private void polaczenie_graczy() throws IOException {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("WITAJ " + kolor);
            if (kolor == 1) {
                aktualny_gracz = this;
                output.println("INFO Oczekuje na przeciwnika");
            } else {
                przeciwnik = aktualny_gracz;
                aktualny_gracz.przeciwnik = this;
                przeciwnik.output.println("INFO Twoja kolej");
                this.output.println("INFO Runda przeciwnika, proszę czekać");
            }
        }

        private void interpretujKomendy() {
            while (input.hasNext()) {
                String polecenie = input.next();
                if (polecenie.startsWith("WYJSCIE")) {
                    return;
                } else if (polecenie.startsWith("RUCH")) {
                    zweryfikujRuch(Integer.parseInt(input.next()), Integer.parseInt(input.next()));
                }
            }
        }

        private void zweryfikujRuch(int locX, int locY) {
            try {
                ruch(locX, locY, this);
                output.println("POPRAWNY_RUCH");
                przeciwnik.output.println("RUCH_PRZECIWNIKA " + locX + " " + locY);
                /*if (false) {
                    output.println("ZWYCIESTWO");
                    przeciwnik.output.println("PORAZKA");
                }*/
            } catch (IllegalStateException e) {
                output.println("INFO " + e.getMessage());
            }
        }
    }
}

