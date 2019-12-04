import java.util.ArrayList;
import java.util.List;

public class Kamien {
    private int kolor; //1 - czarny, 2 - bialy


    Kamien (int nowy_kolor){
        this.kolor = nowy_kolor;
    };

//czyOddech sprawdza czy kamień ma w swoim zasięgu puste pole (nie bierze pod uwagę kolegi)
    public  boolean czyOddech(int x, int y, Kamien[][] kamien){
        return kamien[x + 1][y].kolor == 0 || kamien[x - 1][y].kolor == 0 || kamien[x][y + 1].kolor == 0 || kamien[x][y - 1].kolor == 0;
                //|| kamien[x + 1][y].kolor == kolor || kamien[x - 1][y].kolor == kolor || kamien[x][y + 1].kolor == kolor || kamien[x][y - 1].kolor == kolor;
    }

    List<Kamien> lista = new ArrayList<>();

    public ArrayList<Kamien> czyKolega (int a, int b, Kamien[][] kamien){

        if (lista.contains(kamien[a + 1][b])==false && kamien[a + 1][b].kolor==kolor){

          if (czyOddech(a+1, b, kamien)){
              lista.clear();
              return (ArrayList<Kamien>) lista;
          }
          lista.add(kamien[a+1][b]);
          czyKolega(a+1, b, kamien);
    }
        if (lista.contains(kamien[a - 1][b]) == false && kamien[a - 1][b].kolor == kolor){

            if (czyOddech(a-1,b,kamien)){
                lista.clear();
                return (ArrayList<Kamien>) lista;
            }
            lista.add(kamien[a-1][b]);
            czyKolega(a-1, b,kamien);
        }
        if (lista.contains(kamien[a][b+1]) == false && kamien[a][b+1].kolor==kolor){

            if (czyOddech(a,b+1,kamien)){
                lista.clear();
                return (ArrayList<Kamien>) lista;
            }
            lista.add(kamien[a][b+1]);
            czyKolega(a, b+1,kamien);
        }
        if (lista.contains(kamien[a][b-1]) == false && kamien[a][b-1].kolor==kolor){

            if (czyOddech(a,b-1,kamien)){
                lista.clear();
                return (ArrayList<Kamien>) lista;
            }
            lista.add(kamien[a][b-1]);
            czyKolega(a, b-1,kamien);
        }
        return (ArrayList<Kamien>) lista;
}}
