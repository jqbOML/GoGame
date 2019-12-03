import java.util.ArrayList;
import java.util.List;

public class Kamien {
public int mojkolor;
int [][] kamien;



    public  boolean czyOddech( int x, int y){
        if (kamien[x+1][y]==mojkolor && kamien[x-1][y]==mojkolor && kamien[x][y+1]==mojkolor && kamien[x][y-1]==mojkolor)
            return true;
        else return false;
    }

    public ArrayList<Integer> czyKolega (int a, int b){
        List<Integer> lista = new ArrayList<>();
        if (!lista.contains(kamien[a + 1][b]) || kamien[a + 1][b]==mojkolor){

          if (czyOddech(a+1,b)){
              lista.clear();
              return (ArrayList<Integer>) lista;
          }
          lista.add(kamien[a+1][b]);
          czyKolega(a+1, b);
    }
        if (!lista.contains(kamien[a - 1][b]) || kamien[a - 1][b]==mojkolor){

            if (czyOddech(a-1,b)){
                lista.clear();
                return (ArrayList<Integer>) lista;
            }
            lista.add(kamien[a-1][b]);
            czyKolega(a-1, b);
        }
        if (!lista.contains(kamien[a][b+1]) || kamien[a][b+1]==mojkolor){

            if (czyOddech(a,b+1)){
                lista.clear();
                return (ArrayList<Integer>) lista;
            }
            lista.add(kamien[a][b+1]);
            czyKolega(a, b+1);
        }
        if (!lista.contains(kamien[a][b-1]) || kamien[a][b-1]==mojkolor){

            if (czyOddech(a,b-1)){
                lista.clear();
                return (ArrayList<Integer>) lista;
            }
            lista.add(kamien[a][b-1]);
            czyKolega(a, b-1);
        }
        return (ArrayList<Integer>) lista;
}}
