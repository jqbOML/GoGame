package GraGo.Serwer;

public class Kamien implements KamienInterface {
    private int kolor; //1 - czarny, 2 - bialy
    private int x, y; //współrzędne kamienia

    public Kamien(int nowy_kolor, int x, int y){
        this.kolor = nowy_kolor;
        this.x = x;
        this.y = y;
    }

    @Override
    public int wezKolor() {
        return kolor;
    }
    @Override
    public int wezX() {
        return x;
    }
    @Override
    public int wezY() {
        return y;
    }
}

