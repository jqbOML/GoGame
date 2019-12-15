package GraGo.Serwer;

class Bot {
    private Kamien[][] planszaGo;
    private int kolor;

    Bot(int kolor){
        this.kolor = kolor;
    }

    String wykonajRuch(Kamien[][] planszaGo){
        String parametryRuchu = "1 1";
        this.planszaGo = planszaGo;

        /**
         * TODO: połóż kamień i wyślij jego parametry w Stringu parametryRuchu np. "0 3"
         */

        return parametryRuchu;
    }

    private boolean czyKO(int x, int y) {
        //TODO: funkcja sprawdzająca czy podany ruch nie będzie powtórzeniem
        return false;
    }

    private boolean czySamoboj(int x, int y) {
        planszaGo[x][y] = new Kamien(kolor, x, y);
        if(planszaGo[x][y].czyOddechLancuch(x, y, planszaGo) == null){
            planszaGo[x][y] = null;
            return false;
        } else{
            planszaGo[x][y] = null;
            return true;
        }

    }
}
