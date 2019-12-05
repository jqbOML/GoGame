
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KamienTest {

    private Kamien[][] kamienie = new Kamien[19][19];

    @Test
    public void czyOddechTest() {
        kamienie[3][2] = new Kamien(3, 2, 1); //brak oddechow dla pojedynczego kamienia

        kamienie[3][1] = new Kamien(3, 1, 1);
        kamienie[3][3] = new Kamien(3, 3, 2);
        kamienie[2][2] = new Kamien(2, 2, 2);
        kamienie[4][2] = new Kamien(4, 2, 2);

        assertFalse(kamienie[3][2].czyOddech(3,2, kamienie)); //kamien nie ma oddechow
        assertTrue(kamienie[2][2].czyOddech(2,2, kamienie)); //kamien ma przynajmniej jeden oddech
    }

    @Test
    void czyUduszoneTest() {
        kamienie[3][2] = new Kamien(3, 2, 1);
        kamienie[3][1] = new Kamien(3, 1, 1);
        kamienie[3][3] = new Kamien(3, 3, 1);
        kamienie[2][2] = new Kamien(2, 2, 1);
        kamienie[4][2] = new Kamien(4, 2, 1); //5 bialych kamienii do uduszenia
        assertNotNull(kamienie[3][2]);

        kamienie[3][0] = new Kamien(3, 0, 2);
        kamienie[4][1] = new Kamien(4, 1, 2);
        kamienie[5][2] = new Kamien(5, 2, 2);
        kamienie[4][3] = new Kamien(4, 3, 2);
        kamienie[3][4] = new Kamien(3, 4, 2);
        kamienie[2][3] = new Kamien(2, 3, 2);
        kamienie[1][2] = new Kamien(1, 2, 2);
        kamienie[2][1] = new Kamien(2, 1, 2); //czarne kamienie otaczające
        kamienie[3][2].czyOddechLancuch(3,2, kamienie); //sprawdz oddechy dla lanucha, do ktorego nalezy kamien [3][2]

        kamienie[5][3] = new Kamien(5, 3, 2);
        kamienie[4][4] = new Kamien(4, 4, 2);
        kamienie[4][3].czyOddechLancuch(4,3, kamienie); //sprawdz oddechy dla lancucha, do ktorego nalezy kamien [4][3]

        //sprawdz czy uduszone kamienie zostaly usuniete z tablicy
        assertNull(kamienie[3][2]);
        assertNull(kamienie[3][2]);
        assertNull(kamienie[3][2]);
        assertNull(kamienie[3][2]);
        assertNull(kamienie[3][2]);
        //sprawdz czy kamien ktory posiada oddech w lancuchu nadal istnieje
        assertNotNull(kamienie[4][3]);
    }

}