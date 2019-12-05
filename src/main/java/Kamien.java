import java.util.ArrayList;

class Kamien {
    private int x,y; //wspolrzedne kamienia na planszy
    int kolor; //1 - czarny, 2 - bialy
    private ArrayList<Kamien> uduszone_kamienie = new ArrayList<>(); //arraylista dla metody czyOddechLancuch()

    Kamien (int wsp_x, int wsp_y, int nowy_kolor){
        this.x = wsp_x;
        this.y = wsp_y;
        this.kolor = nowy_kolor;
    }

    /**
     * metoda czyOddech zwraca true jeśli pojedynczy kamień ma w swoim zasięgu puste pole (oddech) - nie bierze pod uwagę łańucha, do którego może należeć
     */
    boolean czyOddech(int x, int y, Kamien[][] kamienie){
        return kamienie[x + 1][y] == null || kamienie[x - 1][y] == null || kamienie[x][y + 1] == null || kamienie[x][y - 1] == null;
    }

    /**
     * metoda czyOddechLancuch() zwraca tablicę kamieni z usuniętym lancuchem kamieni z planszy jesli łańcuch, do którego należał dany kamień nie posiadał oddechów,
     * a w przypadku posiadania co najmniej jednego oddechu zwraca niezmienioną tablicę
     */
    Kamien[][] czyOddechLancuch(int a, int b, Kamien[][] kamienie){
        if (kamienie[a + 1][b] != null){
            if (!uduszone_kamienie.contains(kamienie[a + 1][b]) && kamienie[a + 1][b].kolor == this.kolor){
                if (czyOddech(a + 1, b, kamienie)) return null;
                uduszone_kamienie.add(kamienie[a + 1][b]);
                czyOddechLancuch(a + 1, b, kamienie);
            }
        } else return null;

        if (kamienie[a - 1][b] != null){
            if (!uduszone_kamienie.contains(kamienie[a - 1][b]) && kamienie[a - 1][b].kolor == this.kolor){
                if (czyOddech(a - 1, b, kamienie)) return null;
                else{
                    uduszone_kamienie.add(kamienie[a-1][b]);
                    czyOddechLancuch(a - 1, b, kamienie);
                }
            }
        } else return null;

        if (kamienie[a][b + 1] != null){
            if (!uduszone_kamienie.contains(kamienie[a][b + 1]) && kamienie[a][b + 1].kolor == this.kolor){
                if (czyOddech(a,b + 1, kamienie)) return null;
                else{
                    uduszone_kamienie.add(kamienie[a][b + 1]);
                    czyOddechLancuch(a,b + 1, kamienie);
                }
            }
        } else return null;

        if (kamienie[a][b - 1] != null){
            if (!uduszone_kamienie.contains(kamienie[a][b - 1]) && kamienie[a][b - 1].kolor == this.kolor){
                if (czyOddech(a,b - 1, kamienie)) return null;
                else{
                    uduszone_kamienie.add(kamienie[a][b - 1]);
                    czyOddechLancuch(a,b - 1, kamienie);
                }
            }
        } else return null;

        for (Kamien uduszony_kamien : uduszone_kamienie){
            kamienie[uduszony_kamien.x][uduszony_kamien.y] = null;
        }
        return kamienie;
}}
