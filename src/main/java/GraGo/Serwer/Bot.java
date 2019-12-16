package GraGo.Serwer;

public class Bot {
    private Kamien[][] planszaGo;
    private int kolor;
    private int n =0;



    public Bot(int kolor){
        this.kolor = kolor;
    }

    public String wykonajRuch(Kamien[][] planszaGo) {
        boolean zrobionyRuch = false;
        String parametryRuchu = null;
        this.planszaGo = planszaGo;
        while (!zrobionyRuch) {
            if (n < 4) {
                int i = (int) (Math.random() * ((18 - 1) + 1) + 1);
                int j = (int) (Math.random() * ((18 - 1) + 1) + 1);


                if (planszaGo[i][j] == null && !czySamoboj(i, j)) {
                    System.out.println((j - 1) + " " + (i + 1));
                    parametryRuchu = i + " " + j;
                    zrobionyRuch = true;
                    n++;
                }
            } else {
                for (int i = 1; i < 17; i++) {
                    for (int j = 1; j < 17; j++) {

                        if (planszaGo[j][i] != null) {
                            if (n % 4 == 0 && planszaGo[j - 1][i] == null && !czySamoboj(j - 1, i)) {
                                System.out.println((j - 1) + " " + (i) + "zaawansowany1");
                                parametryRuchu = (j - 1) + " " + (i);
                                zrobionyRuch = true;
                                n++;
                                break;
                            } else if (n % 3 == 0 && planszaGo[j + 1][i] == null && !czySamoboj(j + 1, i)) {
                                System.out.println((j + 1) + " " + (i) + "zaawansowany2");
                                parametryRuchu = (j + 1) + " " + (i);
                                zrobionyRuch = true;
                                n++;
                                break;
                            } else if (n % 2 == 0 && planszaGo[j][i + 1] == null && !czySamoboj(j, i + 1)) {
                                System.out.println((j) + " " + (i + 1) + "zaawansowany3");
                                parametryRuchu = (j) + " " + (i + 1);
                                zrobionyRuch = true;
                                n++;
                                break;
                            } else if (n % 1 == 0 && planszaGo[j][i - 1] == null && !czySamoboj(j, i - 1)) {
                                System.out.println((j) + " " + (i - 1) + "zaawansowany4");
                                parametryRuchu = (j) + " " + (i - 1);
                                zrobionyRuch = true;
                                n=3;
                                break;
                            } else {
                                n=3;
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


    private boolean czySamoboj(int x, int y) {
        planszaGo[x][y] = new Kamien(kolor, x, y);
        if(planszaGo[x][y].czyOddechLancuch(x, y, planszaGo).size() == 0){
            planszaGo[x][y] = null;
            return false;
        } else{
            planszaGo[x][y] = null;
            return true;
        }

    }
}
