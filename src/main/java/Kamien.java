import java.util.ArrayList;
import java.util.List;

public class Kamien {
public int mojkolor; //1-biały 2-czarny


Kamien (int mojkolor){
    this.mojkolor=mojkolor;
};



    public  boolean czyOddech( int x, int y, Kamien[][] kamien){
        if (kamien[x+1][y].mojkolor ==0 || kamien[x-1][y].mojkolor == 0 || kamien[x][y+1].mojkolor == 0 || kamien[x][y-1].mojkolor == 0
        || kamien[x+1][y].mojkolor == mojkolor || kamien[x-1][y].mojkolor == mojkolor || kamien[x][y+1].mojkolor == mojkolor || kamien[x][y-1].mojkolor == mojkolor)
            return true;
        else return false;
    }

    public ArrayList<Kamien> czyKolega (int a, int b, Kamien[][] kamien){
        List<Kamien> lista = new ArrayList<>();
        if (!lista.contains(kamien[a + 1][b]) && kamien[a + 1][b].mojkolor==mojkolor){

          if (czyOddech(a+1,b,kamien)){
              lista.clear();
              return (ArrayList<Kamien>) lista;
          }
          lista.add(kamien[a+1][b]);
          czyKolega(a+1, b,kamien);
    }
        if (!lista.contains(kamien[a - 1][b]) && kamien[a - 1][b].mojkolor==mojkolor){

            if (czyOddech(a-1,b,kamien)){
                lista.clear();
                return (ArrayList<Kamien>) lista;
            }
            lista.add(kamien[a-1][b]);
            czyKolega(a-1, b,kamien);
        }
        if (!lista.contains(kamien[a][b+1]) && kamien[a][b+1].mojkolor==mojkolor){

            if (czyOddech(a,b+1,kamien)){
                lista.clear();
                return (ArrayList<Kamien>) lista;
            }
            lista.add(kamien[a][b+1]);
            czyKolega(a, b+1,kamien);
        }
        if (!lista.contains(kamien[a][b-1]) && kamien[a][b-1].mojkolor==mojkolor){

            if (czyOddech(a,b-1,kamien)){
                lista.clear();
                return (ArrayList<Kamien>) lista;
            }
            lista.add(kamien[a][b-1]);
            czyKolega(a, b-1,kamien);
        }
        return (ArrayList<Kamien>) lista;
}}
