package GraGo.Serwer;

import java.io.IOException;
import java.util.ArrayList;

abstract class AbstractSerwer {
    Interpreter interpreter;
    AbstractGracz aktualnyGracz;
    Boolean czyBot = false;
    Bot bot;
    int[] wynik = new int[2];

    abstract void polaczenieZGraczami(Gracz gracz) throws IOException;

    abstract void interpretujKomendy(Gracz gracz);

    abstract void wyjscieZGry(Gracz gracz);

    //public abstract Object wyslijUduszoneKamienie();
    abstract void wyslijUduszoneKamienie(ArrayList<Kamien> uduszoneKamienie, AbstractGracz gracz);

    //public abstract Object wyslijUduszoneKamienie(ArrayList<Kamien> czyOddech);
}
