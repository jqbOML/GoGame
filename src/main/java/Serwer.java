import java.util.ArrayList;

class Serwer {

    private Kamien kamien;
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


    ArrayList<Kamien[][]> przegladajPlansze() {
        ArrayList<Kamien[][]> kamienie_dowywalenia = new ArrayList<>();
        for (int w = 0; w < 19; w++) {
            for (int e = 0; e < 19; e++) {
                if (plansza_go[w][e] != null) {
                   // System.out.println ("usuwamy kamien:"+ plansza_go[w][e].czyOddechLancuch(w, e, plansza_go));
                   kamienie_dowywalenia.add(plansza_go[w][e].czyOddechLancuch(w, e, plansza_go));
                }
            }
        }

       /* for (Kamien[][] uduszony_kamien : kamienie_dowywalenia){
            System.out.println ("usuwamy kamien:"+uduszony_kamien[uduszony_kamien.x][uduszony_kamien.y]);
        }*/
        for (Kamien[][] i : kamienie_dowywalenia) {
            if (i != null) {
                System.out.println("usuwamy kamien: " + i);
            }
        }

        return kamienie_dowywalenia;
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

