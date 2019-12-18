
import GraGo.Serwer.Bot;
import GraGo.Serwer.Interpreter;
import GraGo.Serwer.Kamien;
import GraGo.Serwer.Serwer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BotTest {
    private Interpreter interpreter = new Interpreter();
    private Bot testowyBot = new Bot(interpreter, 2);

    @Test
    void czyNieSamobojTest(){
        interpreter.planszaGo[1][3] = new Kamien(1,1,3);
        interpreter.planszaGo[2][2] = new Kamien(1,2,2);
        interpreter.planszaGo[2][4] = new Kamien(1,2,4);
        interpreter.planszaGo[3][3] = new Kamien(1,3,3);

        String[] parametr = testowyBot.wykonajRuch(interpreter.planszaGo).split(" ");
        int botX = Integer.parseInt(parametr[0]);
        int botY = Integer.parseInt(parametr[1]);
        interpreter.planszaGo[botX][botY] = new Kamien(2,botX,botY);

        assertNull(interpreter.planszaGo[2][3]);
    }

    @Test
    void wykonajRuchTest(){
        interpreter.planszaGo[1][3] = new Kamien(1,1,3);
        interpreter.planszaGo[2][2] = new Kamien(1,2,2);
        interpreter.planszaGo[2][4] = new Kamien(1,2,4);
        interpreter.planszaGo[3][3] = new Kamien(1,3,3);

        String[] parametr = testowyBot.wykonajRuch(interpreter.planszaGo).split(" ");
        int botX = Integer.parseInt(parametr[0]);
        int botY = Integer.parseInt(parametr[1]);
        interpreter.planszaGo[botX][botY] = new Kamien(2,botX,botY);

        
        int ilosc = 0;
        for(Kamien[] kamien1: interpreter.planszaGo)
            for(Kamien kamien: kamien1)
            {
                if (kamien != null)
                    ++ilosc;
            }


        assertEquals(5, ilosc);

    }
}
