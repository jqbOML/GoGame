
import GraGo.Serwer.Bot;
import GraGo.Serwer.Kamien;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BotTest {
    private Kamien[][] kamienie = new Kamien[19][19];
    Bot testowyBot = new Bot(2);

    @Test
    void czyNieSamobojTest(){
        kamienie[1][3] = new Kamien(1,1,3);
        kamienie[2][2] = new Kamien(1,2,2);
        kamienie[2][4] = new Kamien(1,2,4);
        kamienie[3][3] = new Kamien(1,3,3);

        String[] parametr = testowyBot.wykonajRuch(kamienie).split(" ");
        int botX = Integer.parseInt(parametr[0]);
        int botY = Integer.parseInt(parametr[1]);
        kamienie[botX][botY] = new Kamien(2,botX,botY);

        assertNull(kamienie[2][3]);

    }

    @Test
    void wykonajRuchTest(){
        kamienie[1][3] = new Kamien(1,1,3);
        kamienie[2][2] = new Kamien(1,2,2);
        kamienie[2][4] = new Kamien(1,2,4);
        kamienie[3][3] = new Kamien(1,3,3);

        String[] parametr = testowyBot.wykonajRuch(kamienie).split(" ");
        int botX = Integer.parseInt(parametr[0]);
        int botY = Integer.parseInt(parametr[1]);
        kamienie[botX][botY] = new Kamien(2,botX,botY);

        
        int ilosc = 0;
        for(Kamien[] kamien1: kamienie)
            for(Kamien kamien: kamien1)
            {
                if (kamien != null)
                    ++ilosc;
            }


        assertEquals(5, ilosc);

    }
}
