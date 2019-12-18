package GraGo.Serwer;

public class Kamien {
    private int kolor; //1 - czarny, 2 - bialy
    private int x, y; //współrzędne kamienia
    private boolean oddech;

    public Kamien(int nowy_kolor, int x, int y){
        this.kolor = nowy_kolor;
        this.x = x;
        this.y = y;
        this.oddech = true;
    }


    public int wezKolor(){
        return kolor;
    }

    public int wezX(){
        return x;
    }
    public int wezY(){
        return y;
    }
    public boolean czyOddech(){
        return oddech;
    }
    public void zmianaOddechu(boolean nowaWartosc){
        this.oddech = nowaWartosc;
    }

}

