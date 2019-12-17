package GraGo.Klient;

import GraGo.KomunikatyKlienta;
import GraGo.KomunikatySerwera;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

class Klient extends AbstractKlient{

    Klient(String adresSerwera) throws Exception {
        startGUI = new GUIStart();
        startGUI.przeciwnikButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                przeciwnik = "Gracz";
            }
        });
        startGUI.botButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                przeciwnik = "Bot";
            }
        });

        Thread.sleep(4000); //// to nie działą jakoś super :(
        startGUI.oknoStartowe.dispose();
        if (przeciwnik.equals("Gracz")) {
            socket = new Socket(adresSerwera, 58901);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(KomunikatyKlienta.GRACZ);

            planszaGUI = new GUIPlansza();
            wysylajKomendy();
            odbierajKomendy();
        }
        if (przeciwnik.equals("Bot")){
            socket = new Socket(adresSerwera, 58901);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(KomunikatyKlienta.BOT);

            planszaGUI = new GUIPlansza();
            wysylajKomendy();
            odbierajKomendy();
        }
    }

    @Override
    public void odbierajKomendy() throws Exception {
        try {
            String odpowiedz = in.nextLine();
                System.out.println("Wiadomosc z serwera: "+ odpowiedz);
            int kolor = Character.digit(odpowiedz.charAt(6), 10);
            planszaGUI.ustawKolorGracza(kolor);
            planszaGUI.ramka.setTitle("Gra Go: Gracz " + ((kolor == 1) ? "czarny" : "biały"));
            while (in.hasNextLine()) {
                odpowiedz = in.next();
                System.out.println("Respons in.next: " + odpowiedz);
                if (odpowiedz.startsWith(KomunikatySerwera.POPRAWNY_RUCH.toString())) {
                    planszaGUI.belkaStatusu.setText("Runda przeciwnika, proszę czekać");
                    int locX = Integer.parseInt(in.next());
                    int locY = Integer.parseInt(in.next());
                    System.out.println("Moje> locX: " + locX + ", locY: " + locY);
                    if (kolor == 1) {
                        wybranePole.setIcon(planszaGUI.tekstury.Im_czarnyxx);
                        planszaGUI.planszaKamieni[locX][locY] = 1;
                    } else {
                        wybranePole.setIcon(planszaGUI.tekstury.Im_bialyxx);
                        planszaGUI.planszaKamieni[locX][locY] = 2;
                    }
                    wybranePole.repaint();
                } else if (odpowiedz.startsWith(KomunikatySerwera.RUCH_PRZECIWNIKA.toString())) {
                    int locX = Integer.parseInt(in.next());
                    int locY = Integer.parseInt(in.next());
                    System.out.println("Przeciwnik> locX: " + locX + ", locY: " + locY);
                    if (kolor == 1) {
                        planszaGUI.pole[locX][locY].setIcon(planszaGUI.tekstury.Im_bialyxx);
                        planszaGUI.planszaKamieni[locX][locY] = 2;
                    } else {
                        planszaGUI.pole[locX][locY].setIcon(planszaGUI.tekstury.Im_czarnyxx);
                        planszaGUI.planszaKamieni[locX][locY] = 1;
                    }
                    planszaGUI.pole[locX][locY].repaint();
                    planszaGUI.belkaStatusu.setText("Przeciwnik wykonał ruch, Twoja kolej");
                } else if (odpowiedz.startsWith(KomunikatySerwera.USUN.toString())){
                    int locX = Integer.parseInt(in.next());
                    System.out.println("Usun x: " + locX);
                    int locY = Integer.parseInt(in.next());
                    System.out.println("Usun y: "+ locY);
                    planszaGUI.planszaKamieni[locX][locY] = 0;
                    planszaGUI.pole[locX][locY].setIcon(planszaGUI.tekstury.Im_pustexx); //wczytanie tekstury bez uwzględnienia granic
                    planszaGUI.pole[locX][locY].repaint();
                } else if (odpowiedz.startsWith(KomunikatySerwera.INFO.toString())) {
                    planszaGUI.belkaStatusu.setText(in.nextLine());
                } else if (odpowiedz.startsWith(KomunikatySerwera.ZWYCIESTWO.toString())) {
                    JOptionPane.showMessageDialog(planszaGUI.ramka, "Wygrałeś, gratulacje!");
                    break;
                } else if (odpowiedz.startsWith(KomunikatySerwera.PORAZKA.toString())) {
                    JOptionPane.showMessageDialog(planszaGUI.ramka, "Przeciwnik wygrał gre :(");
                    break;
                } else if (odpowiedz.startsWith(KomunikatySerwera.REMIS.toString())) {
                    JOptionPane.showMessageDialog(planszaGUI.ramka, "Remis!");
                    break;
                } else if (odpowiedz.startsWith(KomunikatySerwera.PASS.toString())) {
                    planszaGUI.belkaStatusu.setText("Przeciwnik spasował, twój ruch!");
                }  else if (odpowiedz.startsWith(KomunikatySerwera.PRZECIWNIK_WYSZEDL.toString())) {
                    JOptionPane.showMessageDialog(planszaGUI.ramka, "Przeciwnik wyszedł z gry!");
                    break;
                }else if (odpowiedz.startsWith(KomunikatySerwera.WYNIK.toString())) {
                        int a = Integer.parseInt(in.next());
                        int b = Integer.parseInt(in.next());
                        int zgoda = JOptionPane.showConfirmDialog(planszaGUI, "Twój wynik to: " + b + " Wynik przeciwnika to: " + a
                                , "Czy zgadzasz się z wynikiem?", JOptionPane.YES_NO_OPTION);
                        if (zgoda == JOptionPane.YES_OPTION){
                            out.print(KomunikatyKlienta.ZAAKCEPTUJ_WYNIK);
                        } else if(zgoda == JOptionPane.NO_OPTION){
                            out.print(KomunikatyKlienta.ODRZUC_WYNIK);
                        }
                } else if (odpowiedz.startsWith(KomunikatySerwera.KONIEC_GRY.toString())) {
                    if (kolor == 1) {
                        wynikGUI = new GUIWynik();
                        wynikGUI.okButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                wynik[0] = Integer.parseInt(wynikGUI.WynikGracza.getText());
                                wynik[1] = Integer.parseInt(wynikGUI.WynikPrzeciwnika.getText());
                                out.println(KomunikatyKlienta.WYNIK.toString() + " " + wynik[0] + " " + wynik[1]);
                            }
                        });
                    }
                }
            }

            out.println(KomunikatyKlienta.WYJSCIE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            socket.close();
            planszaGUI.ramka.dispose();
        }
    }

    @Override
    public void wysylajKomendy(){
        planszaGUI.zakonczGreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                out.println(KomunikatyKlienta.WYJSCIE);
            }
        });

        planszaGUI.passButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                out.println(KomunikatyKlienta.PASS);
                planszaGUI.belkaStatusu.setText("Spasowałeś, ruch przeciwnika!");
                }
        });

        for(int b = 0; b < GUIPlansza.ROZMIAR_PLANSZY; b++) {
            for (int a = 0; a < GUIPlansza.ROZMIAR_PLANSZY; a++) {
                int finalA = a;
                int finalB = b;
                planszaGUI.pole[a][b].addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        wybranePole = planszaGUI.pole[finalA][finalB];
                        out.println(KomunikatyKlienta.RUCH + " " + finalA + " " + finalB);
                        System.out.println("Moj ruch> locX: " + finalA + ", locY: " + finalB);
                    }
                });
            }
        }
    }
}