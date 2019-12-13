package Serwer;

import java.util.ArrayList;

public class Kamien {
    private int x,y; //wspolrzedne kamienia na planszy
    int kolor; //1 - czarny, 2 - bialy
    private ArrayList<Kamien> uduszone_kamienie = new ArrayList<>(); //arraylista dla metody czyOddechLancuch()

    public Kamien(int wsp_x, int wsp_y, int nowy_kolor){
        this.x = wsp_x;
        this.y = wsp_y;
        this.kolor = nowy_kolor;
    }

    /**
     * metoda czyOddech zwraca true jeśli pojedynczy kamień ma w swoim zasięgu puste pole (oddech) - nie bierze pod uwagę łańucha, do którego może należeć
     */
    public boolean czyOddech(int x, int y, Kamien[][] kamienie){
        if (x == 0 && y == 0)
            return kamienie[x + 1][y] == null || kamienie[x][y + 1] == null; // róg
        else if (x == 0 && y == 18)
            return kamienie[x + 1][y] == null || kamienie[x][y - 1] == null; // róg
        else if (x == 18 && y == 0)
            return kamienie[x - 1][y] == null || kamienie[x][y + 1] == null; // róg
        else if (x == 18 && y == 18)
            return kamienie[x - 1][y] == null || kamienie[x][y - 1] == null; // róg
        else if (x == 0 && y > 0 && y < 18)
            return kamienie[x + 1][y] == null || kamienie[x][y - 1] == null || kamienie[x][y + 1] == null; // bok
        else if (x == 18 && y > 0 && y < 18)
            return kamienie[x - 1][y] == null || kamienie[x][y - 1] == null || kamienie[x][y + 1] == null; // bok
        else if (x > 0 && x < 18 && y == 0)
            return kamienie[x + 1][y] == null || kamienie[x -1][y] == null || kamienie[x][y + 1] == null; // bok
        else if (x > 0 && x < 18 && y == 18)
            return kamienie[x + 1][y] == null || kamienie[x -1][y] == null || kamienie[x][y - 1] == null; // bok
        else
        return kamienie[x + 1][y] == null || kamienie[x - 1][y] == null || kamienie[x][y + 1] == null || kamienie[x][y - 1] == null; //środek
    }

    /**
     * metoda czyOddechLancuch() zwraca tablicę kamieni z usuniętym lancuchem kamieni z planszy jesli łańcuch, do którego należał dany kamień nie posiadał oddechów,
     * a w przypadku posiadania co najmniej jednego oddechu zwraca niezmienioną tablicę
     */
    public Kamien[][] czyOddechLancuch(int a, int b, Kamien[][] kamienie){
        if (a+1<=18) {
            if (kamienie[a + 1][b] != null) {
                if (!uduszone_kamienie.contains(kamienie[a + 1][b]) && kamienie[a + 1][b].kolor == this.kolor) {
                    if (czyOddech(a + 1, b, kamienie)){
                        System.out.println("a="+a+", b="+b+": RETURN NULL");
                        return null;
                    }
                    uduszone_kamienie.add(kamienie[a + 1][b]);
                    czyOddechLancuch(a + 1, b, kamienie);
                }
            } else {
                System.out.println("a="+a+", b="+b+": RETURN NULL");
                return null;
            }

        }
        if(a-1>=0) {
            if (kamienie[a - 1][b] != null) {
                if (!uduszone_kamienie.contains(kamienie[a - 1][b]) && kamienie[a - 1][b].kolor == this.kolor) {
                    if (czyOddech(a - 1, b, kamienie)) {
                        System.out.println("a="+a+", b="+b+": RETURN NULL");
                        return null;
                    }
                    else {
                        uduszone_kamienie.add(kamienie[a - 1][b]);
                        czyOddechLancuch(a - 1, b, kamienie);
                    }
                }
            } else {
                System.out.println("a="+a+", b="+b+": RETURN NULL");
                return null;
            }
        }
        if (b+1<=18) {
            if (kamienie[a][b + 1] != null) {
                if (!uduszone_kamienie.contains(kamienie[a][b + 1]) && kamienie[a][b + 1].kolor == this.kolor) {
                    if (czyOddech(a, b + 1, kamienie)) {
                        System.out.println("a="+a+", b="+b+": RETURN NULL");
                        return null;
                    }
                    else {
                        uduszone_kamienie.add(kamienie[a][b + 1]);
                        czyOddechLancuch(a, b + 1, kamienie);
                    }
                }
            } else {
                System.out.println("a="+a+", b="+b+": RETURN NULL");
                return null;
            }
        }
        if(b-1>=0) {
            if (kamienie[a][b - 1] != null) {
                if (!uduszone_kamienie.contains(kamienie[a][b - 1]) && kamienie[a][b - 1].kolor == this.kolor) {
                    if (czyOddech(a, b - 1, kamienie)) {
                        System.out.println("a="+a+", b="+b+": RETURN NULL");
                        return null;
                    }
                    else {
                        uduszone_kamienie.add(kamienie[a][b - 1]);
                        czyOddechLancuch(a, b - 1, kamienie);
                    }
                }
            } else {
                System.out.println("a="+a+", b="+b+": RETURN NULL");
                return null;
            }
        }



       /* for (int w = 0; w < 19; w++){
            for (int e=0; e < 19; e++){
                System.out.println("a="+a+", b="+b+":");
                System.out.print("kamienie["+w+"]["+e+"] = "+(kamienie[w][e] == null ? "null" : kamienie[w][e].kolor+", "));
            }
        }*/
        return kamienie;
}}
