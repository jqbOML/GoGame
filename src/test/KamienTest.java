import GraGo.Serwer.Kamien;
import GraGo.Serwer.Serwer;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class KamienTest {

    private Serwer serwer = new Serwer();

    @Test
    public void czyOddechTest() {
        serwer.planszaGo[3][2] = new Kamien(1, 3, 2); //brak oddechow dla pojedynczego kamienia
        serwer.planszaGo[3][1] = new Kamien(1,3,1);
        serwer.planszaGo[3][3] = new Kamien(2,3,3);
        serwer.planszaGo[2][2] = new Kamien(2,2,2);
        serwer.planszaGo[4][2] = new Kamien(2,4,2);
        assertFalse(serwer.czySasiedniePoleWolne(serwer.planszaGo[3][2])); //kamien nie ma oddechow
        assertTrue(serwer.czySasiedniePoleWolne(serwer.planszaGo[2][2])); //kamien ma przynajmniej jeden oddech
    }

    @Test
    void czyUduszoneTest() {
        serwer.planszaGo[3][2] = new Kamien(1,3,2);
        serwer.planszaGo[3][1] = new Kamien(1,3,1);
        serwer.planszaGo[3][3] = new Kamien(1,3,3);
        serwer.planszaGo[2][2] = new Kamien(1,2,2);
        serwer.planszaGo[4][2] = new Kamien(1,4,2); //5 bialych kamienii do uduszenia

        serwer.planszaGo[3][0] = new Kamien(2,3,0);
        serwer.planszaGo[4][1] = new Kamien(2,4,1);
        serwer.planszaGo[5][2] = new Kamien(2,5,2);
        serwer.planszaGo[4][3] = new Kamien(2,4,3);
        serwer.planszaGo[3][4] = new Kamien(2,3,4);
        serwer.planszaGo[2][3] = new Kamien(2,2,3);
        serwer.planszaGo[1][2] = new Kamien(2,1,2);
        serwer.planszaGo[2][1] = new Kamien(2,2,1); //czarne serwer.planszaGo otaczające
        assertEquals(5, serwer.czyOddech(serwer.planszaGo[3][2]).size()); // sprawdza, czy testowana metoda zwróci listę 5 kamieni do usunięcia


        serwer.planszaGo[4][10] = new Kamien(2,4,10);
        serwer.planszaGo[4][11] = new Kamien(1,5,3);
        serwer.planszaGo[5][10] = new Kamien(1,4,4);
        assertEquals(0, serwer.czyOddech(serwer.planszaGo[4][10]).size()); // sprawdza czy testowana metoda zwróci pustą listę (testowany kamień ma oddechy)

        serwer.planszaGo[10][10] = new Kamien(1,10,10);
        serwer.planszaGo[9][10] = new Kamien(2,9,10);
        serwer.planszaGo[11][10] = new Kamien(2,11,10);
        serwer.planszaGo[10][9] = new Kamien(2,10,9);
        serwer.planszaGo[10][11] = new Kamien(2,10,11);
        assertEquals(1, serwer.czyOddech(serwer.planszaGo[10][10]).size()); //koszyczek

        serwer.planszaGo[0][0] = new Kamien(1,0,0);
        serwer.planszaGo[0][1] = new Kamien(2,0,1);
        serwer.planszaGo[1][0] = new Kamien(2,1,0);
        assertEquals(1, serwer.czyOddech(serwer.planszaGo[0][0]).size()); // sprawdza serwer.planszaGo w rogu (jeden jest uduszony)

        serwer.planszaGo[0][0] = new Kamien(1,0,0);
        serwer.planszaGo[0][1] = new Kamien(1,0,1);
        serwer.planszaGo[1][0] = new Kamien(1,1,0);
        assertEquals(0, serwer.czyOddech(serwer.planszaGo[1][1]).size()); // sprawdza serwer.planszaGo w rogu (zero uduszonych bo wszystkie tego samego koloru)

        serwer.planszaGo[10][10] = new Kamien(1,10,10);
        serwer.planszaGo[9][10] = new Kamien(1,9,10);
        serwer.planszaGo[11][10] = new Kamien(1,11,10);
        serwer.planszaGo[10][9] = new Kamien(2,10,9);
        serwer.planszaGo[10][11] = new Kamien(2,10,11);
        assertEquals(0, serwer.czyOddech(serwer.planszaGo[10][10]).size());

    }


}