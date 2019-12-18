package GraGo.Serwer;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

abstract class AbstractGracz {
    Serwer serwer;
    Socket socket;
    Scanner input;
    PrintWriter output;
    int kolor; //1 - czarny, 2 - bialy
    AbstractGracz przeciwnik;
   public boolean pass;

    void ustawPrzeciwnika(AbstractGracz przeciwnik){
        this.przeciwnik = przeciwnik;
    };
    int wezKolor(){
        return kolor;
    };
}
