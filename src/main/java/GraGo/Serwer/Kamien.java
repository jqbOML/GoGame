package GraGo.Serwer;

import java.util.ArrayList;

public class Kamien {
    private int kolor; //1 - czarny, 2 - bialy
    private int x, y; //współrzędne kamienia

    public Kamien(int nowy_kolor, int x, int y){
        this.kolor = nowy_kolor;
        this.x = x;
        this.y = y;
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

}

