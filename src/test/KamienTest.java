import GraGo.Serwer.Interpreter;
import GraGo.Serwer.Kamien;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KamienTest {
    private Interpreter interpreter = new Interpreter();

    @Test
    public void czyOddechTest() {
        interpreter.planszaGo[3][2] = new Kamien(1, 3, 2); //brak oddechow dla pojedynczego kamienia
        interpreter.planszaGo[3][1] = new Kamien(1,3,1);
        interpreter.planszaGo[3][3] = new Kamien(2,3,3);
        interpreter.planszaGo[2][2] = new Kamien(2,2,2);
        interpreter.planszaGo[4][2] = new Kamien(2,4,2);
        assertFalse(interpreter.czySasiedniePoleWolne(interpreter.planszaGo[3][2])); //kamien nie ma oddechow
        assertTrue(interpreter.czySasiedniePoleWolne(interpreter.planszaGo[2][2])); //kamien ma przynajmniej jeden oddech
    }

    @Test
    void czyUduszoneTest() {
        interpreter.planszaGo[3][2] = new Kamien(1,3,2);
        interpreter.planszaGo[3][1] = new Kamien(1,3,1);
        interpreter.planszaGo[3][3] = new Kamien(1,3,3);
        interpreter.planszaGo[2][2] = new Kamien(1,2,2);
        interpreter.planszaGo[4][2] = new Kamien(1,4,2); //5 bialych kamienii do uduszenia

        interpreter.planszaGo[3][0] = new Kamien(2,3,0);
        interpreter.planszaGo[4][1] = new Kamien(2,4,1);
        interpreter.planszaGo[5][2] = new Kamien(2,5,2);
        interpreter.planszaGo[4][3] = new Kamien(2,4,3);
        interpreter.planszaGo[3][4] = new Kamien(2,3,4);
        interpreter.planszaGo[2][3] = new Kamien(2,2,3);
        interpreter.planszaGo[1][2] = new Kamien(2,1,2);
        interpreter.planszaGo[2][1] = new Kamien(2,2,1); //czarne interpreter.planszaGo otaczające
        assertEquals(5, interpreter.czyOddech(interpreter.planszaGo[3][2]).size()); // sprawdza, czy testowana metoda zwróci listę 5 kamieni do usunięcia


        interpreter.planszaGo[4][10] = new Kamien(2,4,10);
        interpreter.planszaGo[4][11] = new Kamien(1,5,3);
        interpreter.planszaGo[5][10] = new Kamien(1,4,4);
        assertEquals(0, interpreter.czyOddech(interpreter.planszaGo[4][10]).size()); // sprawdza czy testowana metoda zwróci pustą listę (testowany kamień ma oddechy)

        interpreter.planszaGo[9][10] = new Kamien(2,9,10);
        interpreter.planszaGo[10][9] = new Kamien(2,10,9);
        interpreter.planszaGo[10][10] = new Kamien(1,10,10);
        interpreter.planszaGo[10][11] = new Kamien(2,10,11);
        interpreter.planszaGo[11][10] = new Kamien(2,11,10);
        assertEquals(1, interpreter.czyOddech(interpreter.planszaGo[10][10]).size()); //koszyczek

        interpreter.planszaGo[0][0] = new Kamien(1,0,0);
        interpreter.planszaGo[0][1] = new Kamien(2,0,1);
        interpreter.planszaGo[1][0] = new Kamien(2,1,0);
        assertEquals(1, interpreter.czyOddech(interpreter.planszaGo[0][0]).size()); // sprawdza interpreter.planszaGo w rogu (jeden jest uduszony)

        interpreter.planszaGo[0][0] = new Kamien(1,0,0);
        interpreter.planszaGo[0][1] = new Kamien(1,0,1);
        interpreter.planszaGo[1][0] = new Kamien(1,1,0);
        assertEquals(0, interpreter.czyOddech(interpreter.planszaGo[0][0]).size()); // sprawdza interpreter.planszaGo w rogu (zero uduszonych bo wszystkie tego samego koloru)

        interpreter.planszaGo[10][10] = new Kamien(1,10,10);
        interpreter.planszaGo[9][10] = new Kamien(1,9,10);
        interpreter.planszaGo[11][10] = new Kamien(1,11,10);
        interpreter.planszaGo[10][9] = new Kamien(2,10,9);
        interpreter.planszaGo[10][11] = new Kamien(2,10,11);
        assertEquals(0, interpreter.czyOddech(interpreter.planszaGo[10][10]).size());

    }


}