package GraGo.Serwer;

import java.io.IOException;
import java.util.ArrayList;

abstract class AbstractSerwer {
    public Kamien[][] planszaGo = new Kamien[19][19];
    AbstractGracz aktualnyGracz;
    Boolean czyBot = false;
    Bot bot;
    int[] wynik = new int[2];

    abstract void polaczenieZGraczami(Gracz gracz) throws IOException;

    abstract void interpretujKomendy(Gracz gracz);

    abstract boolean czyKO(Kamien kamien);
    abstract boolean czySamoboj(Kamien kamien);
    abstract ArrayList<Kamien> czyOddech(Kamien kamien);
    abstract void czyUduszone(Kamien kamien);

    abstract void wyjscieZGry(Gracz gracz);

}
