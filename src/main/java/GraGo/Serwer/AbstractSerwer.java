package GraGo.Serwer;

import java.io.IOException;

abstract class AbstractSerwer {
    Interpreter interpreter;
    AbstractGracz aktualnyGracz;
    Boolean czyBot = false;
    int[] wynik = new int[2];

    abstract void polaczenieZGraczami(Gracz gracz) throws IOException;

    abstract void interpretujKomendy(Gracz gracz);

    abstract void wyjscieZGry(Gracz gracz);

}
