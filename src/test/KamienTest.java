import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KamienTest {

    private Kamien[][] kamienie = new Kamien[5][5];
    @Test
    public void brakOddechowTest() {

        kamienie[3][2] = new Kamien(1); //brak oddechow

        kamienie[3][1] = new Kamien(2);
        kamienie[3][3] = new Kamien(2);
        kamienie[2][2] = new Kamien(2);
        kamienie[4][2] = new Kamien(2);


        assertFalse(kamienie[3][2].czyOddech(3,2, kamienie));
    }

    @Test
    void czyKolega() {
    }
}