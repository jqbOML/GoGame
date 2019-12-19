package GraGo.Serwer;

import java.util.ArrayList;

public class Interpreter extends AbstractInterpreter {

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

        ostatniUsunietyKamien = null;
    }

    @Override
    boolean czyKO(Kamien kamien) {
        if (ostatniUsunietyKamien == null){
            return false;
        }
        return (kamien.wezX() == ostatniUsunietyKamien.wezX() && kamien.wezY() == ostatniUsunietyKamien.wezY() && kamien.wezKolor() == ostatniUsunietyKamien.wezKolor());
    }

    @Override
    public boolean czySamoboj(Kamien kamien) {
        uduszoneKamienie.clear();

        if (czyOddech(kamien).size() == 0){
            System.out.println("znalazlem oddech");
            return false;
        } else if(kamien.wezX()<18 && planszaGo[kamien.wezX()+1][kamien.wezY()].wezKolor() != kamien.wezKolor() && czyOddech(planszaGo[kamien.wezX()+1][kamien.wezY()]).size() != 0){
            System.out.println("Prawy uduszony");
            return false;
        } else if(kamien.wezX()>0 && planszaGo[kamien.wezX()-1][kamien.wezY()].wezKolor() != kamien.wezKolor() && czyOddech(planszaGo[kamien.wezX()-1][kamien.wezY()]).size() != 0){
            System.out.println("Lewy uduszony");
            return false;
        } else if(kamien.wezY()<18 && planszaGo[kamien.wezX()][kamien.wezY()+1].wezKolor() != kamien.wezKolor() && czyOddech(planszaGo[kamien.wezX()][kamien.wezY()+1]).size() != 0){
            System.out.println("Gorny uduszony");
            return false;
        } else if (kamien.wezY()>0 && planszaGo[kamien.wezX()][kamien.wezY()-1].wezKolor() != kamien.wezKolor() && czyOddech(planszaGo[kamien.wezX()][kamien.wezY()-1]).size() != 0){
            System.out.println("Dolny uduszony");
            return false;
        }else{
            return true;
        }

    }

    @Override
    public ArrayList<Kamien> czyOddech(Kamien kamien) {
        uduszoneKamienie.clear();
        kamien.zmianaOddechu(false);

        return czyOddech(kamien, kamien, new ArrayList<Kamien>());

    }

    /**
     * metoda czyOddech() zwraca ArrayList<Kamien>
     */
    private ArrayList<Kamien> czyOddech(Kamien kamienSprawdzany, Kamien kamienZGrupy, ArrayList<Kamien> sprawdzoneKamienie){

        int x = kamienZGrupy.wezX();
        int y = kamienZGrupy.wezY();

        if(czySasiedniePoleWolne(kamienZGrupy))
        {
            uduszoneKamienie.clear();
            kamienSprawdzany.zmianaOddechu(true);
            return uduszoneKamienie;
        } else if (!sprawdzoneKamienie.contains(kamienZGrupy)) {
            uduszoneKamienie.add(kamienZGrupy);
            sprawdzoneKamienie.add(kamienZGrupy);

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
    public void sprawdzUduszone(Kamien kamien, AbstractGracz gracz, Gra serwer){
        System.out.println("czyUduszony dla sąsiadów Kamienia.x: "+kamien.wezX()+", y: "+kamien.wezY()+", kolor: "+kamien.wezKolor());
        int x = kamien.wezX();
        int y = kamien.wezY();
        ArrayList<Kamien> kamienieDoUduszenia;

        if (x < 18) {
            if (planszaGo[x+1][y] != null){
                if (planszaGo[x+1][y].wezKolor() == gracz.przeciwnik.wezKolor()){
                    kamienieDoUduszenia = czyOddech(planszaGo[x+1][y]);
                    if (!planszaGo[x+1][y].czyOddech()){
                        if(kamienieDoUduszenia.size() == 1){
                            ostatniUsunietyKamien = kamienieDoUduszenia.get(0);
                        }
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
                        if(kamienieDoUduszenia.size() == 1){
                            ostatniUsunietyKamien = kamienieDoUduszenia.get(0);
                        }
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
                        if(kamienieDoUduszenia.size() == 1){
                            ostatniUsunietyKamien = kamienieDoUduszenia.get(0);
                        }
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
                        if(kamienieDoUduszenia.size() == 1){
                            ostatniUsunietyKamien = kamienieDoUduszenia.get(0);
                        }
                        serwer.wyslijUduszoneKamienie(kamienieDoUduszenia, gracz);
                    }
                }
            }
        }

    }
}
