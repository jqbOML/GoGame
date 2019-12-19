package GraGo.Serwer;

import GraGo.KomunikatyKlienta;
import GraGo.KomunikatySerwera;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Serwer extends AbstractSerwer implements Runnable {
    ArrayList<Gracz> oczekujacyGracze = new ArrayList<>();

    @Override
    public void run() {
        while(true){
            for(int i = 0;i<oczekujacyGracze.size();i++){
                if (oczekujacyGracze.get(i).gotowDoGry && !oczekujacyGracze.get(i).chceGracZBotem){
                    for(int j=i+1;j<oczekujacyGracze.size();j++){
                        if(oczekujacyGracze.get(j).gotowDoGry && !oczekujacyGracze.get(j).chceGracZBotem){
                            AbstractGracz [] gracze = new AbstractGracz[2];
                            oczekujacyGracze.get(i).wGrze = true;
                            oczekujacyGracze.get(j).wGrze = true;
                            gracze[0] = oczekujacyGracze.get(i);
                            gracze[1] = oczekujacyGracze.get(j);
                            gracze[0].ustawPrzeciwnika(gracze[1]);
                            gracze[1].ustawPrzeciwnika(gracze[0]);
                            Gra gra = new Gra(gracze);
                        }
                    }
                }
            }
            for(Gracz gracz: oczekujacyGracze){
                if(gracz.gotowDoGry && gracz.chceGracZBotem && !gracz.wGrze){
                    AbstractGracz [] gracze = new AbstractGracz[2];
                    gracz.wGrze = true;
                    gracze[0] = gracz;
                    gracze[1] = new Bot();
                    gracze[0].ustawPrzeciwnika(gracze[1]);
                    gracze[1].ustawPrzeciwnika(gracze[0]);
                    Gra gra = new Gra(gracze);
                }
            }
            for(int i = oczekujacyGracze.size()-1;i>=0;i--){
                if(oczekujacyGracze.get(i).wGrze){
                    oczekujacyGracze.remove(i);
                }
            }
            synchronized (this){
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void polaczenieZGraczami(Gracz gracz) throws IOException {
        gracz.input = new Scanner(gracz.socket.getInputStream());
        gracz.output = new PrintWriter(gracz.socket.getOutputStream(), true);
        String przeciwnik = gracz.input.next();

        if (przeciwnik.startsWith(KomunikatyKlienta.BOT.toString())){
            gracz.chceGracZBotem=true;
        } else if (przeciwnik.startsWith(KomunikatyKlienta.GRACZ.toString())){
            gracz.chceGracZBotem=false;
            gracz.output.println(KomunikatySerwera.INFO + " Oczekuje na przeciwnika");
        }
        gracz.gotowDoGry = true;

    }

}