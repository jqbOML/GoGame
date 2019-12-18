package GraGo.Serwer;

public class Bot extends AbstractGracz implements BotInterface
{
    private Interpreter interpreter;
    private int licznikRuchow = 0;

    public Bot(Interpreter interpreter, int kolor){
        this.interpreter = interpreter;
        this.kolor = kolor;
        System.out.println("Kolor bota: "+wezKolor());
    }

    @Override
    public String wykonajRuch(Kamien[][] planszaGo) {
        boolean zrobionyRuch = false;
        String parametryRuchu = null;

        while (!zrobionyRuch) {
            if (licznikRuchow < 4) {
                int i = (int) (Math.random() * ((18 - 1) + 1) + 1);
                int j = (int) (Math.random() * ((18 - 1) + 1) + 1);


                if (planszaGo[i][j] == null && !czySamoboj(i, j, planszaGo)) {
                    System.out.println("Ruch bota: " + (j - 1) + " " + (i + 1));
                    parametryRuchu = i + " " + j;
                    zrobionyRuch = true;
                    licznikRuchow++;
                }
            } else {
                for (int i = 1; i < 17; i++) {
                    for (int j = 1; j < 17; j++) {
                        if (planszaGo[j][i] != null) {
                            if (licznikRuchow % 4 == 0 && planszaGo[j - 1][i] == null && !czySamoboj(j - 1, i, planszaGo)) {
                                System.out.println("Ruch bota: " + (j - 1) + " " + (i) + "zaawansowany1");
                                parametryRuchu = (j - 1) + " " + (i);
                                zrobionyRuch = true;
                                licznikRuchow++;
                                break;
                            } else if (licznikRuchow % 3 == 0 && planszaGo[j + 1][i] == null && !czySamoboj(j + 1, i, planszaGo)) {
                                System.out.println("Ruch bota: " + (j + 1) + " " + (i) + "zaawansowany2");
                                parametryRuchu = (j + 1) + " " + (i);
                                zrobionyRuch = true;
                                licznikRuchow++;
                                break;
                            } else if (licznikRuchow % 2 == 0 && planszaGo[j][i + 1] == null && !czySamoboj(j, i + 1, planszaGo)) {
                                System.out.println("Ruch bota: " + (j) + " " + (i + 1) + "zaawansowany3");
                                parametryRuchu = (j) + " " + (i + 1);
                                zrobionyRuch = true;
                                licznikRuchow++;
                                break;
                            } else if (licznikRuchow % 1 == 0 && planszaGo[j][i - 1] == null && !czySamoboj(j, i - 1, planszaGo)) {
                                System.out.println("Ruch bota: " + (j) + " " + (i - 1) + "zaawansowany4");
                                parametryRuchu = (j) + " " + (i - 1);
                                zrobionyRuch = true;
                                licznikRuchow=3;
                                break;
                            } else {
                                licznikRuchow=3;
                            }
                        }
                    }
                }
            }
        }
        return parametryRuchu;
    }

    private boolean czyKO(int x, int y) {
        //TODO: funkcja sprawdzająca czy podany ruch nie będzie powtórzeniem
        return false;
    }


    private boolean czySamoboj(int x, int y, Kamien[][] planszaGo) {
        planszaGo[x][y] = new Kamien(kolor, x, y);
        interpreter.czyOddech(planszaGo[x][y]);
        if(interpreter.planszaGo[x][y].czyOddech()){
            planszaGo[x][y] = null;
            return false;
        } else{
            planszaGo[x][y] = null;
            return true;
        }
    }


}
