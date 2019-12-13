import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
class Serwer {

    private Kamien[][] plansza_go = new Kamien[19][19];
    Gracz aktualnyGracz;

    synchronized void zweryfikujRuch(int x, int y, Gracz gracz) {
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
        plansza_go[x][y] = new Kamien(x, y, aktualnyGracz.kolor);
        ustawAktualnegoGracza(aktualnyGracz.przeciwnik);
    }

    void czyUduszone(int x, int y) {
        System.out.println("czyUduszone dla x: "+x+", y: "+y);
        if (plansza_go[x + 1][y] != null) {
            if (plansza_go[x + 1][y].kolor == aktualnyGracz.przeciwnik.kolor)
            plansza_go = plansza_go[x + 1][y].czyOddechLancuch(x + 1, y, plansza_go);
        }
        if (plansza_go[x - 1][y] != null) {
            if (plansza_go[x - 1][y].kolor == aktualnyGracz.przeciwnik.kolor)
            plansza_go = plansza_go[x - 1][y].czyOddechLancuch(x - 1, y, plansza_go);
        }

        if (plansza_go[x][y - 1] != null) {
            if (plansza_go[x][y - 1].kolor == aktualnyGracz.przeciwnik.kolor)
            plansza_go = plansza_go[x][y - 1].czyOddechLancuch(x, y - 1, plansza_go);
        }

        if (plansza_go[x][y + 1] != null) {
            if (plansza_go[x][y + 1].kolor == aktualnyGracz.przeciwnik.kolor)
            plansza_go = plansza_go[x][y + 1].czyOddechLancuch(x, y + 1, plansza_go);
        }
    }

    private boolean czyKO(int x, int y) {
        //TODO: funkcja sprawdzająca czy podany ruch nie będzie powtórzeniem
        return false;
    }

    private boolean czySamoboj(int x, int y) {
        plansza_go[x][y] = new Kamien(x, y, aktualnyGracz.kolor);
        if(plansza_go[x][y].czyOddechLancuch(x, y, plansza_go) == null){
            plansza_go[x][y] = null;
            return false;
        } else{
            plansza_go[x][y] = null;
            return true;
        }

    }

    public void ustawAktualnegoGracza(Gracz gracz){
        this.aktualnyGracz = gracz;
    }
}

