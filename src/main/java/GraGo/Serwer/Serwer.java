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
            ustawAktualnegoGracza(gracz);
        } else {
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
                    zweryfikujRuch(x, y, gracz);
                    gracz.output.println(KomunikatySerwera.POPRAWNY_RUCH + " " + x + " " + y);
                    //czyUduszone(planszaGo[x][y]);
                    if (czyBot){
                        Thread.sleep(200);
                        String[] parametry = bot.wykonajRuch(planszaGo).split(" ");
                        int botX = Integer.parseInt(parametry[0]);
                        int botY = Integer.parseInt(parametry[1]);
                        gracz.output.println(KomunikatySerwera.RUCH_PRZECIWNIKA + " " + botX + " " + botY);
                        planszaGo[botX][botY] = new Kamien(aktualnyGracz.wezKolor(), botX, botY);
                        //czyUduszone(planszaGo[botX][botY]);
                    } else{
                        ustawAktualnegoGracza(aktualnyGracz.przeciwnik);
                        gracz.przeciwnik.output.println(KomunikatySerwera.RUCH_PRZECIWNIKA + " " + x + " " + y);
                    }
                    gracz.pass = false;
                } catch (IllegalStateException | InterruptedException e) {
                    gracz.output.println(KomunikatySerwera.INFO + " " + e.getMessage());
                }
            }else if (polecenie.startsWith(KomunikatySerwera.ZWYCIESTWO.toString())) {
                gracz.output.println(KomunikatySerwera.ZWYCIESTWO.toString());
                gracz.przeciwnik.output.println(KomunikatySerwera.PORAZKA);
            }else if (polecenie.startsWith(KomunikatySerwera.PORAZKA.toString())) {
                gracz.output.println(KomunikatySerwera.PORAZKA);
                gracz.przeciwnik.output.println(KomunikatySerwera.ZWYCIESTWO);
            }else if (polecenie.startsWith(KomunikatySerwera.REMIS.toString())) {
                gracz.output.println(KomunikatySerwera.REMIS);
                gracz.przeciwnik.output.println(KomunikatySerwera.REMIS);
            }else if (polecenie.startsWith(KomunikatySerwera.PASS.toString())) {
                gracz.pass = true;
                if (!czyBot){
                    gracz.przeciwnik.output.println(KomunikatySerwera.PASS);
                    ustawAktualnegoGracza(gracz.przeciwnik);
                }
            }else if (polecenie.startsWith(KomunikatySerwera.WYNIK.toString())) {
                int a = Integer.parseInt(gracz.input.next());
                int b = Integer.parseInt(gracz.input.next());
                gracz.output.println(KomunikatySerwera.WYNIK + " " + a + " " + b);
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
        if(!czySasiedniePoleWolne(kamien))
        {
            if(!uduszoneKamienie.contains(planszaGo[kamien.wezX()][kamien.wezY()])) {
                uduszoneKamienie.add(planszaGo[kamien.wezX()][kamien.wezY()]);
            }
        }
        if (kamien.wezX() < 18) {
            if (planszaGo[kamien.wezX() + 1][kamien.wezY()] != null) {
                if (!uduszoneKamienie.contains(planszaGo[kamien.wezX() + 1][kamien.wezY()]) && planszaGo[kamien.wezX() + 1][kamien.wezY()].wezKolor() == kamien.wezKolor()) {
                    if (czySasiedniePoleWolne(kamien)){
                        uduszoneKamienie.clear();
                        return (ArrayList<Kamien>) uduszoneKamienie;
                    }else {
                        uduszoneKamienie.add(planszaGo[kamien.wezX() + 1][kamien.wezY()]);
                        czyOddech(kamien);
                    }
                }
            }
        }

        if (kamien.wezX() > 0) {
            if (planszaGo[kamien.wezX() - 1][kamien.wezY()] != null) {
                if (!uduszoneKamienie.contains(planszaGo[kamien.wezX() - 1][kamien.wezY()]) && planszaGo[kamien.wezX() - 1][kamien.wezY()].wezKolor() == kamien.wezKolor()) {
                    if (czySasiedniePoleWolne(kamien)) {
                        uduszoneKamienie.clear();
                        return (ArrayList<Kamien>) uduszoneKamienie;
                    } else{
                        uduszoneKamienie.add(planszaGo[kamien.wezX() - 1][kamien.wezY()]);
                        czyOddech(kamien);
                    }
                }
            }
        }

        if (kamien.wezY() < 18) {
            if (planszaGo[kamien.wezX()][kamien.wezY() + 1] != null) {
                if (!uduszoneKamienie.contains(planszaGo[kamien.wezX()][kamien.wezY() + 1]) && planszaGo[kamien.wezX()][kamien.wezY() + 1].wezKolor() == kamien.wezKolor()) {
                    if (czySasiedniePoleWolne(kamien)) {
                        uduszoneKamienie.clear();
                        return (ArrayList<Kamien>) uduszoneKamienie;
                    } else {
                        uduszoneKamienie.add(planszaGo[kamien.wezX()][kamien.wezY() + 1]);
                        czyOddech(kamien);
                    }
                }
            }
        }

        if (kamien.wezY() > 0) {
            if (planszaGo[kamien.wezX()][kamien.wezY() - 1] != null) {
                if (!uduszoneKamienie.contains(planszaGo[kamien.wezX()][kamien.wezY() - 1]) && planszaGo[kamien.wezX()][kamien.wezY() - 1].wezKolor() == kamien.wezKolor()) {
                    if (czySasiedniePoleWolne(kamien)) {
                        uduszoneKamienie.clear();
                        return (ArrayList<Kamien>) uduszoneKamienie;
                    } else {
                        uduszoneKamienie.add(planszaGo[kamien.wezX()][kamien.wezY() - 1]);
                        czyOddech(kamien);
                    }
                }
            }
        }

        return uduszoneKamienie;
    }

    /**
     * metoda czySasiedniePoleWolne zwraca true jeśli pojedynczy kamień ma w swoim zasięgu puste pole (oddech) - nie bierze pod uwagę łańucha, do którego może należeć
     */
    public boolean czySasiedniePoleWolne(Kamien kamien){
        if (kamien.wezX() == 0 && kamien.wezY() == 0)
            return planszaGo[kamien.wezX() + 1][kamien.wezY()] == null || planszaGo[kamien.wezX()][kamien.wezY() + 1] == null; // róg
        else if (kamien.wezX() == 0 && kamien.wezY() == 18)
            return planszaGo[kamien.wezX() + 1][kamien.wezY()] == null || planszaGo[kamien.wezX()][kamien.wezY() - 1] == null; // róg
        else if (kamien.wezX() == 18 && kamien.wezY() == 0)
            return planszaGo[kamien.wezX() - 1][kamien.wezY()] == null || planszaGo[kamien.wezX()][kamien.wezY() + 1] == null; // róg
        else if (kamien.wezX() == 18 && kamien.wezY() == 18)
            return planszaGo[kamien.wezX() - 1][kamien.wezY()] == null || planszaGo[kamien.wezX()][kamien.wezY() - 1] == null; // róg
        else if (kamien.wezX() == 0 && kamien.wezY() > 0 && kamien.wezY() < 18)
            return planszaGo[kamien.wezX() + 1][kamien.wezY()] == null || planszaGo[kamien.wezX()][kamien.wezY() - 1] == null || planszaGo[kamien.wezX()][kamien.wezY() + 1] == null; // bok
        else if (kamien.wezX() == 18 && kamien.wezY() > 0 && kamien.wezY() < 18)
            return planszaGo[kamien.wezX() - 1][kamien.wezY()] == null || planszaGo[kamien.wezX()][kamien.wezY() - 1] == null || planszaGo[kamien.wezX()][kamien.wezY() + 1] == null; // bok
        else if (kamien.wezX() > 0 && kamien.wezX() < 18 && kamien.wezY() == 0)
            return planszaGo[kamien.wezX() + 1][kamien.wezY()] == null || planszaGo[kamien.wezX() -1][kamien.wezY()] == null || planszaGo[kamien.wezX()][kamien.wezY() + 1] == null; // bok
        else if (kamien.wezX() > 0 && kamien.wezX() < 18 && kamien.wezY() == 18)
            return planszaGo[kamien.wezX() + 1][kamien.wezY()] == null || planszaGo[kamien.wezX() -1][kamien.wezY()] == null || planszaGo[kamien.wezX()][kamien.wezY() - 1] == null; // bok
        else
            return planszaGo[kamien.wezX() + 1][kamien.wezY()] == null || planszaGo[kamien.wezX() - 1][kamien.wezY()] == null || planszaGo[kamien.wezX()][kamien.wezY() + 1] == null || planszaGo[kamien.wezX()][kamien.wezY() - 1] == null; //środek
    }

    @Override
    void czyUduszone(Kamien kamien) {
        //TODO: sprawdzUduszone
    }

    /*private void sprawdzUduszone(int x, int y, Gracz gracz){
        if (planszaGo[kamien.wezX()+1][kamien.wezY()] != null && planszaGo[kamien.wezX()+1][kamien.wezY()].wezKolor() == gracz.przeciwnik.wezKolor()){
            if (planszaGo[kamien.wezX()+1][kamien.wezY()].czyOddech(kamien.wezX()+1, kamien.wezY(), planszaGo) != null){
                ArrayList<Kamien> uduszoneKamienie = planszaGo[kamien.wezX()+1][kamien.wezY()].czyOddech(kamien.wezX()+1, kamien.wezY(), planszaGo);
                wyslijUduszoneKamienie(uduszoneKamienie, gracz);
            }
        }
        if (planszaGo[kamien.wezX()-1][kamien.wezY()] != null && planszaGo[kamien.wezX()-1][kamien.wezY()].wezKolor() == gracz.przeciwnik.wezKolor()){
            if (planszaGo[kamien.wezX()-1][kamien.wezY()].czyOddech(kamien.wezX()-1, kamien.wezY(), planszaGo) != null){
                ArrayList<Kamien> uduszoneKamienie = planszaGo[kamien.wezX()-1][kamien.wezY()].czyOddech(kamien.wezX()-1, kamien.wezY(), planszaGo);
                wyslijUduszoneKamienie(uduszoneKamienie, gracz);
            }
        }
        if (planszaGo[kamien.wezX()][kamien.wezY()+1] != null && planszaGo[kamien.wezX()][kamien.wezY()+1].wezKolor() == gracz.przeciwnik.wezKolor()){
            if (planszaGo[kamien.wezX()][kamien.wezY()+1].czyOddech(kamien.wezX(), kamien.wezY()+1, planszaGo) != null){
                ArrayList<Kamien> uduszoneKamienie = planszaGo[kamien.wezX()][kamien.wezY()+1].czyOddech(kamien.wezX(), kamien.wezY()+1, planszaGo);
                wyslijUduszoneKamienie(uduszoneKamienie, gracz);
            }
        }
        if (planszaGo[kamien.wezX()][kamien.wezY()-1] != null && planszaGo[kamien.wezX()][kamien.wezY()-1].wezKolor() == gracz.przeciwnik.wezKolor()){
            if (planszaGo[kamien.wezX()][kamien.wezY()-1].czyOddech(kamien.wezX(), kamien.wezY()-1, planszaGo) != null){
                ArrayList<Kamien> uduszoneKamienie = planszaGo[kamien.wezX()][kamien.wezY()-1].czyOddech(kamien.wezX(), kamien.wezY()-1, planszaGo);
                wyslijUduszoneKamienie(uduszoneKamienie, gracz);
            }
        }
    }

    private void wyslijUduszoneKamienie(ArrayList<Kamien> uduszoneKamienie, Gracz gracz){
        for (Kamien uduszonyKamien : uduszoneKamienie){
            gracz.output.println(KomunikatySerwera.USUN + " " + uduszonyKamien.wezX() + " " + uduszonyKamien.wezY());
            gracz.przeciwnik.output.println(KomunikatySerwera.USUN + " " + uduszonyKamien.wezX() + " " + uduszonyKamien.wezY());
        }
    }*/

    private void ustawAktualnegoGracza(Gracz gracz){
        this.aktualnyGracz = gracz;
        System.out.println("Aktualny gracz: "+gracz.wezKolor());
    }

    void wyjscieZGry(Gracz gracz){
        if (gracz.przeciwnik != null && gracz.przeciwnik.output != null) {
            gracz.przeciwnik.output.println(KomunikatySerwera.PRZECIWNIK_WYSZEDL);
        }
        try {gracz.socket.close();} catch (IOException ignored) {}
    }
}

