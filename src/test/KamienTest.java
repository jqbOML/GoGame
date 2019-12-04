
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KamienTest {

    private Kamien[][] kamienie = new Kamien[13][13];

    @Test
    public void brakOddechowTest() {

        kamienie[3][2] = new Kamien(1); //brak oddechow

        kamienie[3][1] = new Kamien(1);
        kamienie[3][3] = new Kamien(2);
        kamienie[2][2] = new Kamien(2);
        kamienie[4][2] = new Kamien(2);


        assertFalse(kamienie[3][2].czyOddech(3,2, kamienie));
    }

    @Test
    void czyKolegatest() {

        List<Kamien> testlista = new ArrayList<>();
        kamienie[3][2] = new Kamien(1);
        testlista.add(kamienie[3][2]);
        kamienie[3][1] = new Kamien(1);
        testlista.add(kamienie[3][1]);
        kamienie[3][3] = new Kamien(1);
        testlista.add(kamienie[3][3]);
        kamienie[2][2] = new Kamien(1);
        testlista.add(kamienie[2][2]);
        kamienie[4][2] = new Kamien(1);
        testlista.add(kamienie[4][2]);//kamienie otoczone, dodane do listy testowej

        kamienie[3][0] = new Kamien(2);
        kamienie[4][1] = new Kamien(2);
        kamienie[5][2] = new Kamien(2);
        kamienie[4][3] = new Kamien(2);
        kamienie[3][4] = new Kamien(2);
        kamienie[2][3] = new Kamien(2);
        kamienie[1][2] = new Kamien(2);
        kamienie[2][1] = new Kamien(2); //kmienie otaczające*/

        assertEquals(testlista.size(), kamienie[3][2].czyKolega(3, 2, kamienie).size());

    }

}