package GraGo.Serwer;

import java.util.ArrayList;

public class Interpreter extends AbstractInterpreter {
    private ArrayList<Kamien> uduszoneKamienie = new ArrayList<>();

    @Override
    public synchronized void zweryfikujRuch(int x, int y, Gracz gracz, AbstractGracz aktualnyGracz, boolean czyBot) {
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

    //@Override
    public boolean czyKO(Kamien kamien) {
        //TODO: funkcja sprawdzająca czy podany ruch nie będzie powtórzeniem
        return false;
    }
    @Override
    public boolean czySamoboj(Kamien kamien) {
        uduszoneKamienie.clear();
        czyOddech(kamien);
        return (!kamien.czyOddech());
    }

    @Override
    public ArrayList<Kamien> czyOddech(Kamien kamien) {
        uduszoneKamienie.clear();
        System.out.println("czyOddech["+kamien.wezX()+"]["+kamien.wezY()+"] = false");
        kamien.zmianaOddechu(false);

        return czyOddech(kamien, kamien, new ArrayList<Kamien>());

    }

    /**
     * metoda czyOddech() zwraca ...
     */
    private ArrayList<Kamien> czyOddech(Kamien kamienSprawdzany, Kamien kamienZGrupy, ArrayList<Kamien> sprawdzoneKamienie){
        System.out.println("czyOddech dla: x="+kamienZGrupy.wezX()+", y="+kamienZGrupy.wezY());
        int x = kamienZGrupy.wezX();
        int y = kamienZGrupy.wezY();

        if(czySasiedniePoleWolne(kamienZGrupy))
        {
            uduszoneKamienie.clear();
            System.out.println("czyOddech["+kamienSprawdzany.wezX()+"]["+kamienSprawdzany.wezY()+"] = true");
            kamienSprawdzany.zmianaOddechu(true);
            return uduszoneKamienie;
        } else if (!sprawdzoneKamienie.contains(kamienZGrupy)) {
            uduszoneKamienie.add(kamienZGrupy);
            sprawdzoneKamienie.add(kamienZGrupy);
            System.out.println("Dodano kamien[" + kamienZGrupy.wezX() + "][" + kamienZGrupy.wezY() + "]");

            if (x < 18 && !uduszoneKamienie.contains(planszaGo[x + 1][y]) && planszaGo[x + 1][y] != null && planszaGo[x + 1][y].wezKolor() == kamienZGrupy.wezKolor()) {
                czyOddech(kamienSprawdzany, planszaGo[x + 1][y], sprawdzoneKamienie);
            }

            if (x > 0 && !uduszoneKamienie.contains(planszaGo[x - 1][y]) && planszaGo[x - 1][y] != null && planszaGo[x - 1][y].wezKolor() == kamienZGrupy.wezKolor()) {
                czyOddech(kamienSprawdzany, planszaGo[x - 1][y], sprawdzoneKamienie);
            }

            if (y < 18 && !uduszoneKamienie.contains(planszaGo[x][y + 1]) && planszaGo[x][y + 1] != null && planszaGo[x][y + 1].wezKolor() == kamienZGrupy.wezKolor()) {
                czyOddech(kamienSprawdzany, planszaGo[x][y + 1], sprawdzoneKamienie);
            }

            if (y > 0 && !uduszoneKamienie.contains(planszaGo[x][y - 1]) && planszaGo[x][y - 1] != null && planszaGo[x][y - 1].wezKolor() == kamienZGrupy.wezKolor()) {
                czyOddech(kamienSprawdzany, planszaGo[x][y - 1], sprawdzoneKamienie);
            }
        }

        return uduszoneKamienie;
    }

    /**
     * metoda czySasiedniePoleWolne zwraca true jeśli pojedynczy kamień ma w swoim zasięgu puste pole (oddech) - nie bierze pod uwagę łańucha, do którego może należeć
     */
    @Override
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

    /**
     * Metoda sprawdzUduszone sprawdza dla położonego kamienia czy jego sąsiednie pola nie zostały pozbawione oddechów,
     *  o ile znajdują się tam kamienie przeciwnika
     * @param kamien - kamien który został położony
     * @param gracz - gracz, który położył kamień
     */
    @Override
    public void sprawdzUduszone(Kamien kamien, AbstractGracz gracz, Serwer serwer){
        System.out.println("czyUduszony dla sąsiadów Kamienia.x: "+kamien.wezX()+", y: "+kamien.wezY()+", kolor: "+kamien.wezKolor());
        int x = kamien.wezX();
        int y = kamien.wezY();
        ArrayList<Kamien> kamienieDoUduszenia;

        if (x < 18) {
            if (planszaGo[x+1][y] != null){
                if (planszaGo[x+1][y].wezKolor() == gracz.przeciwnik.wezKolor()){
                    kamienieDoUduszenia = czyOddech(planszaGo[x+1][y]);
                    if (!planszaGo[x+1][y].czyOddech()){
                        System.out.println("Kamien["+(x+1)+"]["+y+"] nie posiada oddechow");
                        serwer.wyslijUduszoneKamienie(kamienieDoUduszenia, gracz);
                    }
                }
            }
        }

        if (x > 0) {
            if (planszaGo[x-1][y] != null){
                if (planszaGo[x-1][y].wezKolor() == gracz.przeciwnik.wezKolor()){
                    kamienieDoUduszenia = czyOddech(planszaGo[x-1][y]);
                    if (!planszaGo[x-1][y].czyOddech()){
                        System.out.println("Kamien["+(x-1)+"]["+y+"] nie posiada oddechow");
                        serwer.wyslijUduszoneKamienie(kamienieDoUduszenia, gracz);
                    }
                }
            }
        }

        if (y < 18) {
            if (planszaGo[x][y+1] != null){
                if (planszaGo[x][y+1].wezKolor() == gracz.przeciwnik.wezKolor()){
                    kamienieDoUduszenia = czyOddech(planszaGo[x][y+1]);
                    if (!planszaGo[x][y+1].czyOddech()){
                        System.out.println("Kamien["+x+"]["+(y+1)+"] nie posiada oddechow");
                        serwer.wyslijUduszoneKamienie(kamienieDoUduszenia, gracz);
                    }
                }
            }
        }

        if (y > 0) {
            if(planszaGo[x][y-1] != null){
                if (planszaGo[x][y-1].wezKolor() == gracz.przeciwnik.wezKolor()){
                    kamienieDoUduszenia = czyOddech(planszaGo[x][y-1]);
                    if (!planszaGo[x][y-1].czyOddech()){
                        System.out.println("Kamien["+(x)+"]["+(y-1)+"] nie posiada oddechow");
                        serwer.wyslijUduszoneKamienie(kamienieDoUduszenia, gracz);
                    }
                }
            }
        }

    }
}
