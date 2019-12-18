package GraGo.Serwer;

import GraGo.KomunikatyKlienta;
import GraGo.KomunikatySerwera;

import java.io.IOException;
import java.util.ArrayList;

public class Gra {
    Interpreter interpreter;
    AbstractGracz aktualnyGracz;
    Boolean czyBot = false;
    int[] wynik = new int[2];
    AbstractGracz[] gracze = new AbstractGracz[2];

    public Gra(AbstractGracz[] gracze) {
        System.out.println("Tworze gre");
        interpreter = new Interpreter();
        this.gracze = gracze;
        wystartujGraczy();
        aktualnyGracz = gracze[0];
        if(aktualnyGracz instanceof Gracz)
            aktualnyGracz.output.println(KomunikatySerwera.INFO + " Twoja kolej");
        if(aktualnyGracz.przeciwnik instanceof Gracz)
            aktualnyGracz.przeciwnik.output.println(KomunikatySerwera.INFO + " Runda przeciwnika, proszę czekać");
    }

    public void wystartujGraczy() {
        gracze[0].kolor = 1;
        gracze[0].gra = this;
        gracze[1].gra = this;
        gracze[1].kolor = 2;
        gracze[0].output.println(KomunikatySerwera.KOLOR + " " + gracze[0].kolor);
        if ((gracze[1] instanceof Bot)) {
            ((Bot) gracze[1]).interpreter = this.interpreter;
            czyBot = true;
        }else{
            gracze[1].output.println(KomunikatySerwera.KOLOR + " " + gracze[1].kolor);
        }

    }

    private void ustawAktualnegoGracza(AbstractGracz gracz) {
        aktualnyGracz = gracz;
        System.out.println(">>      Aktualny gracz: " + aktualnyGracz.kolor);
    }

    void wyslijUduszoneKamienie(ArrayList<Kamien> uduszoneKamienie, AbstractGracz gracz) {
        System.out.println("Wysylam uduszone kamienie: ");
        for (Kamien uduszonyKamien : uduszoneKamienie) {
            System.out.print("(" + uduszonyKamien.wezX() + ", " + uduszonyKamien.wezY() + "), ");
            if (gracz instanceof Gracz) {
                gracz.output.println(KomunikatySerwera.USUN + " " + uduszonyKamien.wezX() + " " + uduszonyKamien.wezY());
            }
            if (gracz.przeciwnik instanceof Gracz) {
                gracz.przeciwnik.output.println(KomunikatySerwera.USUN + " " + uduszonyKamien.wezX() + " " + uduszonyKamien.wezY());
            }
            interpreter.planszaGo[uduszonyKamien.wezX()][uduszonyKamien.wezY()] = null;
        }
        System.out.println("");
        uduszoneKamienie.clear();
    }

    public void wyjscieZGry(Gracz gracz) {
        if (gracz.przeciwnik != null && gracz.przeciwnik.output != null) {
            gracz.przeciwnik.output.println(KomunikatySerwera.PRZECIWNIK_WYSZEDL);
        }
        try {
            gracz.socket.close();
        } catch (IOException ignored) {
        }
    }

    public void interpretujKomendy(Gracz gracz) {
        while (gracz.input.hasNext()) {
            String polecenie = gracz.input.next();
            if (polecenie.startsWith(KomunikatyKlienta.WYJSCIE.toString())) {
                return;
            } else if (polecenie.startsWith(KomunikatyKlienta.RUCH.toString())) {
                try {
                    int x = Integer.parseInt(gracz.input.next());
                    int y = Integer.parseInt(gracz.input.next());
                    System.out.println("<!> Otrzymano ruch od gracza " + aktualnyGracz.kolor + ": x: " + x + ", y: " + y);
                    interpreter.zweryfikujRuch(x, y, gracz, aktualnyGracz, czyBot);
                    gracz.output.println(KomunikatySerwera.POPRAWNY_RUCH + " " + x + " " + y);
                    System.out.println("Zatwierdzono ruch od gracza " + aktualnyGracz.kolor + ": x: " + x + ", y: " + y);
                    interpreter.sprawdzUduszone(interpreter.planszaGo[x][y], aktualnyGracz, this);
                    System.out.println("Sprawdzono uduszenie dla x:" + x + ", y: " + y);
                    ustawAktualnegoGracza(aktualnyGracz.przeciwnik);
                    if (czyBot) {
                        Thread.sleep(207);
                        String[] parametry = ((Bot) gracze[1]).wykonajRuch(interpreter.planszaGo).split(" ");
                        int botX = Integer.parseInt(parametry[0]);
                        int botY = Integer.parseInt(parametry[1]);
                        System.out.println("Serwer: otrzymałem ruch bota x: " + botX + ", " + botY);
                        gracz.output.println(KomunikatySerwera.RUCH_PRZECIWNIKA + " " + botX + " " + botY);
                        interpreter.planszaGo[botX][botY] = new Kamien(aktualnyGracz.wezKolor(), botX, botY);
                        interpreter.sprawdzUduszone(interpreter.planszaGo[botX][botY], aktualnyGracz, this);
                        System.out.println("Sprawdzono uduszenie dla x:" + botX + ", y: " + botY);
                        ustawAktualnegoGracza(aktualnyGracz.przeciwnik);
                    } else {
                        gracz.przeciwnik.output.println(KomunikatySerwera.RUCH_PRZECIWNIKA + " " + x + " " + y);
                    }
                    gracz.pass = false;
                } catch (IllegalStateException | InterruptedException e) {
                    gracz.output.println(KomunikatySerwera.INFO + " " + e.getMessage());
                }
            } else if (polecenie.startsWith(KomunikatyKlienta.PASS.toString())) {
                gracz.pass = true;
                if (!czyBot) {
                    gracz.przeciwnik.output.println(KomunikatySerwera.PASS);
                    ustawAktualnegoGracza(gracz.przeciwnik);
                }
            } else if (polecenie.startsWith(KomunikatyKlienta.WYNIK.toString())) {
                wynik[0] = Integer.parseInt(gracz.input.next());
                wynik[1] = Integer.parseInt(gracz.input.next());

                if (czyBot) {
                    if (wynik[0] > wynik[1]) {
                        gracz.output.println(KomunikatySerwera.PORAZKA);
                    } else if (wynik[0] < wynik[1]) {
                        gracz.output.println(KomunikatySerwera.ZWYCIESTWO);
                    } else {
                        gracz.output.println(KomunikatySerwera.REMIS);
                    }
                } else {
                    gracz.output.println(KomunikatySerwera.WYNIK + " " + wynik[0] + " " + wynik[1]);
                }
            } else if (polecenie.startsWith(KomunikatyKlienta.ZAAKCEPTUJ_WYNIK.toString())) {
                {
                    if (wynik[0] > wynik[1]) {
                        gracz.output.println(KomunikatySerwera.ZWYCIESTWO);
                        gracz.przeciwnik.output.print(KomunikatySerwera.PORAZKA);
                    } else if (wynik[0] < wynik[1]) {
                        gracz.output.println(KomunikatySerwera.PORAZKA);
                        gracz.przeciwnik.output.print(KomunikatySerwera.ZWYCIESTWO);
                    } else {
                        gracz.output.println(KomunikatySerwera.REMIS);
                        gracz.przeciwnik.output.print(KomunikatySerwera.REMIS);
                    }
                }
            } else if (polecenie.startsWith(KomunikatyKlienta.ODRZUC_WYNIK.toString())) {
                gracz.output.print(KomunikatySerwera.KONIEC_GRY);
            }

            if (czyBot && gracz.pass) {
                gracz.output.println(KomunikatySerwera.KONIEC_GRY);
            } else if (gracz.pass && gracz.przeciwnik.pass) {
                gracz.output.println(KomunikatySerwera.KONIEC_GRY);
                gracz.przeciwnik.output.println(KomunikatySerwera.KONIEC_GRY);
            }
        }
    }

}
