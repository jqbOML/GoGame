package GraGo.Serwer;

import GraGo.KomunikatyKlienta;
import GraGo.KomunikatySerwera;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Serwer extends AbstractSerwer {
    private ArrayList<Kamien> uduszoneKamienie = new ArrayList<>();

    public Serwer(){
        planszaGo = new Kamien[19][19];
        czyBot = false;
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


    @Override
    void polaczenieZGraczami(Gracz gracz) throws IOException {
        gracz.input = new Scanner(gracz.socket.getInputStream());
        gracz.output = new PrintWriter(gracz.socket.getOutputStream(), true);
        gracz.output.println(KomunikatySerwera.WITAJ+ " " + gracz.wezKolor());
        String przeciwnik = gracz.input.next();

        if (przeciwnik.startsWith(KomunikatyKlienta.BOT.toString())){
            czyBot = true;
            bot = new Bot(this, gracz.wezKolor() == 1 ? 2 : 1);
            gracz.output.println(KomunikatySerwera.INFO + " Grasz z botem");
            gracz.ustawPrzeciwnika(bot);
            bot.ustawPrzeciwnika(gracz);
            ustawAktualnegoGracza(gracz);
        } else if (przeciwnik.startsWith(KomunikatyKlienta.GRACZ.toString())){
            if (gracz.wezKolor() == 1) {
                gracz.output.println(KomunikatySerwera.INFO + " Oczekuje na przeciwnika");
                ustawAktualnegoGracza(gracz);
            } else {
                gracz.ustawPrzeciwnika(aktualnyGracz);
                aktualnyGracz.ustawPrzeciwnika(gracz);
                aktualnyGracz.output.println(KomunikatySerwera.INFO + " Twoja kolej");
                gracz.output.println(KomunikatySerwera.INFO + " Runda przeciwnika, proszę czekać");
            }
        }
    }

    @Override
    void interpretujKomendy(Gracz gracz) {
        while (gracz.input.hasNext()) {
            String polecenie = gracz.input.next();
            if (polecenie.startsWith(KomunikatyKlienta.WYJSCIE.toString())) {
                return;
            } else if (polecenie.startsWith(KomunikatyKlienta.RUCH.toString())) {
                try {
                    int x = Integer.parseInt(gracz.input.next());
                    int y = Integer.parseInt(gracz.input.next());
                    System.out.println("<!> Otrzymano ruch od gracza "+aktualnyGracz.kolor+": x: "+x+", y: "+y);
                    zweryfikujRuch(x, y, gracz);
                    gracz.output.println(KomunikatySerwera.POPRAWNY_RUCH + " " + x + " " + y);
                    System.out.println("Zatwierdzono ruch od gracza "+aktualnyGracz.kolor+": x: "+x+", y: "+y);
                    sprawdzUduszone(planszaGo[x][y], aktualnyGracz);
                    System.out.println("Sprawdzono uduszenie dla x:"+x+", y: "+y);
                    ustawAktualnegoGracza(aktualnyGracz.przeciwnik);
                    if (czyBot){
                        Thread.sleep(207);
                        String[] parametry = bot.wykonajRuch(planszaGo).split(" ");
                        int botX = Integer.parseInt(parametry[0]);
                        int botY = Integer.parseInt(parametry[1]);
                        gracz.output.println(KomunikatySerwera.RUCH_PRZECIWNIKA + " " + botX + " " + botY);
                        planszaGo[botX][botY] = new Kamien(aktualnyGracz.wezKolor(), botX, botY);
                        sprawdzUduszone(planszaGo[botX][botY], aktualnyGracz);
                        ustawAktualnegoGracza(aktualnyGracz.przeciwnik);
                    } else{
                        gracz.przeciwnik.output.println(KomunikatySerwera.RUCH_PRZECIWNIKA + " " + x + " " + y);
                    }
                    gracz.pass = false;
                } catch (IllegalStateException | InterruptedException e) {
                    gracz.output.println(KomunikatySerwera.INFO + " " + e.getMessage());
                }
            }else if (polecenie.startsWith(KomunikatyKlienta.PASS.toString())) {
                gracz.pass = true;
                if (!czyBot){
                    gracz.przeciwnik.output.println(KomunikatySerwera.PASS);
                    ustawAktualnegoGracza(gracz.przeciwnik);
                }
            }else if (polecenie.startsWith(KomunikatyKlienta.WYNIK.toString())) {
                wynik[0] = Integer.parseInt(gracz.input.next());
                wynik[1] = Integer.parseInt(gracz.input.next());

                if(czyBot){
                    if(wynik[0] > wynik[1]) {
                        gracz.output.println(KomunikatySerwera.PORAZKA);
                    }
                    else if(wynik[0] < wynik[1]) {
                        gracz.output.println(KomunikatySerwera.ZWYCIESTWO);
                    }
                    else {
                        gracz.output.println(KomunikatySerwera.REMIS);
                    }
                } else{
                    gracz.output.println(KomunikatySerwera.WYNIK + " " + wynik[0] + " " + wynik[1]);
                }
            }else if(polecenie.startsWith(KomunikatyKlienta.ZAAKCEPTUJ_WYNIK.toString())){
                {
                    if(wynik[0] > wynik[1]) {
                        gracz.output.println(KomunikatySerwera.ZWYCIESTWO);
                        gracz.przeciwnik.output.print(KomunikatySerwera.PORAZKA);
                    }
                    else if(wynik[0] < wynik[1]) {
                        gracz.output.println(KomunikatySerwera.PORAZKA);
                        gracz.przeciwnik.output.print(KomunikatySerwera.ZWYCIESTWO);
                    }
                    else {
                        gracz.output.println(KomunikatySerwera.REMIS);
                        gracz.przeciwnik.output.print(KomunikatySerwera.REMIS);
                    }
                }
            }else if(polecenie.startsWith(KomunikatyKlienta.ODRZUC_WYNIK.toString())){
                gracz.output.print(KomunikatySerwera.KONIEC_GRY);
            }

            if(czyBot && gracz.pass){
                gracz.output.println(KomunikatySerwera.KONIEC_GRY);
            } else if (gracz.pass && gracz.przeciwnik.pass) {
                gracz.output.println(KomunikatySerwera.KONIEC_GRY);
                gracz.przeciwnik.output.println(KomunikatySerwera.KONIEC_GRY);
            }
        }
    }

    private synchronized void zweryfikujRuch(int x, int y, Gracz gracz) {
        if (!czyBot && gracz != aktualnyGracz) {
            throw new IllegalStateException("Nie Twój ruch!");
        } else if (!czyBot && gracz.przeciwnik == null) {
            throw new IllegalStateException("Nie masz jeszcze przeciwnika");
        } else if (planszaGo[x][y] != null) {
            throw new IllegalStateException("Pole jest już zajęte!");
        } else {
            planszaGo[x][y] = new Kamien(aktualnyGracz.wezKolor(), x, y);
            if (czySamoboj(planszaGo[x][y])) {
                planszaGo[x][y] = null;
                throw new IllegalStateException("Niedozwolony ruch samobojczy!");
            }
            if (czyKO(planszaGo[x][y])) {
                planszaGo[x][y] = null;
                throw new IllegalStateException("Niedozwolony ruch KO!");
            }
        }

    }

    @Override
    public boolean czyKO(Kamien kamien) {
        //TODO: funkcja sprawdzająca czy podany ruch nie będzie powtórzeniem
        return false;
    }
    @Override
    public boolean czySamoboj(Kamien kamien) {
        uduszoneKamienie.clear();
        return czyOddech(kamien).size() != 0;
    }

    /**
     * metoda czyOddech() zwraca ...
     */
    @Override
    public ArrayList<Kamien> czyOddech(Kamien kamien){
        System.out.println("czyOddech dla: x="+kamien.wezX()+", y="+kamien.wezY());
        int x = kamien.wezX();
        int y = kamien.wezY();

        if(czySasiedniePoleWolne(kamien))
        {
            uduszoneKamienie.clear();
            return uduszoneKamienie;
        } else if (!uduszoneKamienie.contains(kamien)) {
            uduszoneKamienie.add(kamien);
            System.out.println("Dodano kamien[" + kamien.wezX() + "][" + kamien.wezY() + "]");

            if (x < 18 && !uduszoneKamienie.contains(planszaGo[x + 1][y]) && planszaGo[x + 1][y].wezKolor() == kamien.wezKolor()) {
                czyOddech(planszaGo[x + 1][y]);
            }

            if (x > 0 && !uduszoneKamienie.contains(planszaGo[x - 1][y]) && planszaGo[x - 1][y].wezKolor() == kamien.wezKolor()) {
                czyOddech(planszaGo[x - 1][y]);
            }

            if (y < 18 && !uduszoneKamienie.contains(planszaGo[x][y + 1]) && planszaGo[x][y + 1].wezKolor() == kamien.wezKolor()) {
                czyOddech(planszaGo[x][y + 1]);
            }

            if (y > 0 && !uduszoneKamienie.contains(planszaGo[x][y - 1]) && planszaGo[x][y - 1].wezKolor() == kamien.wezKolor()) {
                czyOddech(planszaGo[x][y - 1]);
            }
        }

        return uduszoneKamienie;
    }

    /**
     * metoda czySasiedniePoleWolne zwraca true jeśli pojedynczy kamień ma w swoim zasięgu puste pole (oddech) - nie bierze pod uwagę łańucha, do którego może należeć
     */
    public boolean czySasiedniePoleWolne(Kamien kamien){
        int x = kamien.wezX();
        int y = kamien.wezY();
        if (x == 0 && y == 0)
            return planszaGo[x + 1][y] == null || planszaGo[x][y + 1] == null; // róg
        else if (x == 0 && y == 18)
            return planszaGo[x + 1][y] == null || planszaGo[x][y - 1] == null; // róg
        else if (x == 18 && y == 0)
            return planszaGo[x - 1][y] == null || planszaGo[x][y + 1] == null; // róg
        else if (x == 18 && y == 18)
            return planszaGo[x - 1][y] == null || planszaGo[x][y - 1] == null; // róg
        else if (x == 0 && y > 0 && y < 18)
            return planszaGo[x + 1][y] == null || planszaGo[x][y - 1] == null || planszaGo[x][y + 1] == null; // bok
        else if (x == 18 && y > 0 && y < 18)
            return planszaGo[x - 1][y] == null || planszaGo[x][y - 1] == null || planszaGo[x][y + 1] == null; // bok
        else if (x > 0 && x < 18 && y == 0)
            return planszaGo[x + 1][y] == null || planszaGo[x -1][y] == null || planszaGo[x][y + 1] == null; // bok
        else if (x > 0 && x < 18 && y == 18)
            return planszaGo[x + 1][y] == null || planszaGo[x -1][y] == null || planszaGo[x][y - 1] == null; // bok
        else
            return planszaGo[x + 1][y] == null || planszaGo[x - 1][y] == null || planszaGo[x][y + 1] == null || planszaGo[x][y - 1] == null; //środek
    }

    @Override
    void czyUduszone(Kamien kamien) {
        //TODO: sprawdzUduszone
    }

    /**
     * Metoda sprawdzUduszone sprawdza dla położonego kamienia czy jego sąsiednie pola nie zostały pozbawione oddechów,
     *  o ile znajdują się tam kamienie przeciwnika
     * @param kamien - kamien który został położony
     * @param gracz - gracz, który położył kamień
     */
    private void sprawdzUduszone(Kamien kamien, AbstractGracz gracz){
        System.out.println("czyUduszony Kamien.x: "+kamien.wezX()+", y: "+kamien.wezY()+", kolor: "+kamien.wezKolor());
        int x = kamien.wezX();
        int y = kamien.wezY();

        if (x < 18) {
            if (planszaGo[x+1][y] != null){
                if (planszaGo[x+1][y].wezKolor() == gracz.przeciwnik.wezKolor()){
                    if (czyOddech(planszaGo[x+1][y]) != null){
                        wyslijUduszoneKamienie(czyOddech(planszaGo[x+1][y]), gracz);
                    }
                }
            }
        }

        if (x > 0) {
            if (planszaGo[x-1][y] != null){
                if (planszaGo[x-1][y].wezKolor() == gracz.przeciwnik.wezKolor()){
                    if (czyOddech(planszaGo[x-1][y]) != null){
                        wyslijUduszoneKamienie(czyOddech(planszaGo[x-1][y]), gracz);
                    }
                }
            }
        }

        if (y < 18) {
            if (planszaGo[x][y+1] != null){
                if (planszaGo[x][y+1].wezKolor() == gracz.przeciwnik.wezKolor()){
                    if (czyOddech(planszaGo[x][y+1]) != null){
                        wyslijUduszoneKamienie(czyOddech(planszaGo[x][y+1]), gracz);
                    }
                }
            }
        }

        if (y > 0) {
            if(planszaGo[x][y-1] != null){
                if (planszaGo[x][y-1].wezKolor() == gracz.przeciwnik.wezKolor()){
                    if (czyOddech(planszaGo[x][y-1]) != null){
                        wyslijUduszoneKamienie(czyOddech(planszaGo[x][y-1]), gracz);
                    }
                }
            }
        }

    }

    private void wyslijUduszoneKamienie(ArrayList<Kamien> uduszoneKamienie, AbstractGracz gracz){
        System.out.println("Wysylam uduszone kamienie");
        for (Kamien uduszonyKamien : uduszoneKamienie){
            gracz.output.println(KomunikatySerwera.USUN + " " + uduszonyKamien.wezX() + " " + uduszonyKamien.wezY());
            planszaGo[uduszonyKamien.wezX()][uduszonyKamien.wezY()] = null;
            if(!czyBot){
                gracz.przeciwnik.output.println(KomunikatySerwera.USUN + " " + uduszonyKamien.wezX() + " " + uduszonyKamien.wezY());
            }
        }
        uduszoneKamienie.clear();
    }

    private void ustawAktualnegoGracza(AbstractGracz gracz){
        this.aktualnyGracz = gracz;
        System.out.println(">>      Aktualny gracz: "+aktualnyGracz.kolor);
    }

    void wyjscieZGry(Gracz gracz){
        if (gracz.przeciwnik != null && gracz.przeciwnik.output != null) {
            gracz.przeciwnik.output.println(KomunikatySerwera.PRZECIWNIK_WYSZEDL);
        }
        try {gracz.socket.close();} catch (IOException ignored) {}
    }
}

