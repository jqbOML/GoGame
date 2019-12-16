import GraGo.Serwer.Kamien;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class KamienTest {

    Kamien[][] kamienie = new Kamien[19][19];

    @Test
    public void czyOddechTest() {
        kamienie[3][2] = new Kamien(1, 3, 2); //brak oddechow dla pojedynczego kamienia
        kamienie[3][1] = new Kamien(1,3,1);
        kamienie[3][3] = new Kamien(2,3,3);
        kamienie[2][2] = new Kamien(2,2,2);
        kamienie[4][2] = new Kamien(2,4,2);
        assertFalse(kamienie[3][2].czyOddech(3,2, kamienie)); //kamien nie ma oddechow
        assertTrue(kamienie[2][2].czyOddech(2,2, kamienie)); //kamien ma przynajmniej jeden oddech
    }

    @Test
    void czyUduszoneTest() {
        kamienie[3][2] = new Kamien(1,3,2);
        kamienie[3][1] = new Kamien(1,3,1);
        kamienie[3][3] = new Kamien(1,3,3);
        kamienie[2][2] = new Kamien(1,2,2);
        kamienie[4][2] = new Kamien(1,4,2); //5 bialych kamienii do uduszenia

        kamienie[3][0] = new Kamien(2,3,0);
        kamienie[4][1] = new Kamien(2,4,1);
        kamienie[5][2] = new Kamien(2,5,2);
        kamienie[4][3] = new Kamien(2,4,3);
        kamienie[3][4] = new Kamien(2,3,4);
        kamienie[2][3] = new Kamien(2,2,3);
        kamienie[1][2] = new Kamien(2,1,2);
        kamienie[2][1] = new Kamien(2,2,1); //czarne kamienie otaczające
        assertEquals(5, kamienie[3][2].czyOddechLancuch(3, 2, kamienie).size()); // sprawdza, czy testowana metoda zwróci listę 5 kamieni do usunięcia


        kamienie[4][10] = new Kamien(2,4,10);
        kamienie[5][1] = new Kamien(2,5,3);
        kamienie[4][12] = new Kamien(2,4,4);
        assertEquals(0, kamienie[4][10].czyOddechLancuch(4, 10, kamienie).size()); // sprawdza czy testowana metoda zwróci pustą listę (testowany kamień ma oddechy)

    }

}