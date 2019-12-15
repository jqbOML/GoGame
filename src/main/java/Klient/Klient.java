package Klient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class Klient{
    private Socket socket;
    private Scanner inString;
    private PrintWriter out;
    private GUIStart startGUI;
    private GUIPlansza planszaGUI;
    private GUIWynik wynikGUI;
    private JLabel wybranePole;
    int[] wynik = new int[2];
    boolean drugigracz;



    Klient(String adresSerwera) throws Exception {

        startGUI = new GUIStart();

            startGUI.przeciwnikButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    drugigracz = true;
                }
            });

       Thread.sleep(7000); //// to nie działą jakoś super :(
        startGUI.oknoStartowe.dispose();
        if (drugigracz) {
            socket = new Socket(adresSerwera, 58901);
            inString = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            planszaGUI = new GUIPlansza();
            wysylajKomendy();
            odbierajKomendy();
        }

    }




    private void odbierajKomendy() throws Exception {
        try {
            String odpowiedz = inString.nextLine();
                System.out.println("Wiadomosc z serwera: "+ odpowiedz);
            int kolor = Character.digit(odpowiedz.charAt(6), 10);
            planszaGUI.kolorGracza = kolor;
                System.out.println("Kolor: "+kolor);
            planszaGUI.ramka.setTitle("Gra Go: Gracz " + ((kolor == 1) ? "czarny" : "biały"));
            while (inString.hasNextLine()) {
                odpowiedz = inString.next();
                System.out.println("Respons inString.next: " + odpowiedz);
                if (odpowiedz.startsWith("POPRAWNY_RUCH")) {
                    planszaGUI.belkaStatusu.setText("Runda przeciwnika, proszę czekać");
                    int locX = Integer.parseInt(inString.next());
                    int locY = Integer.parseInt(inString.next());
                    System.out.println("Moje> locX: " + locX + ", locY: " + locY);
                    if (kolor == 1) {
                        wybranePole.setIcon(planszaGUI.tekstury.Im_czarnyxx);
                        planszaGUI.planszaKamieni[locX][locY] = 1;
                    } else {
                        wybranePole.setIcon(planszaGUI.tekstury.Im_bialyxx);
                        planszaGUI.planszaKamieni[locX][locY] = 2;
                    }
                    wybranePole.repaint();
                } else if (odpowiedz.startsWith("RUCH_PRZECIWNIKA")) {
                    int locX = Integer.parseInt(inString.next());
                    int locY = Integer.parseInt(inString.next());
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
                } else if (odpowiedz.startsWith("INFO")) {
                    planszaGUI.belkaStatusu.setText(inString.nextLine());
                } else if (odpowiedz.startsWith("ZWYCIESTWO")) {
                    JOptionPane.showMessageDialog(planszaGUI.ramka, "Wygrałeś, gratulacje!");
                    break;
                } else if (odpowiedz.startsWith("PORAZKA")) {
                    JOptionPane.showMessageDialog(planszaGUI.ramka, "Przeciwnik wygrał gre :(");
                    break;
                } else if (odpowiedz.startsWith("REMIS")) {
                    JOptionPane.showMessageDialog(planszaGUI.ramka, "Remis!");
                    break;
                } else if (odpowiedz.startsWith("PASS")) {
                    planszaGUI.belkaStatusu.setText("Przeciwnik spasował, twój ruch!");
                } else if (odpowiedz.startsWith("PRZECIWNIK_WYSZEDL")) {
                    JOptionPane.showMessageDialog(planszaGUI.ramka, "Przeciwnik wyszedł z gry!");
                    break;
                }else if (odpowiedz.startsWith("WYNIK")) {
                        int a = Integer.parseInt(inString.next());
                        int b = Integer.parseInt(inString.next());
                        int z = JOptionPane.showConfirmDialog(planszaGUI, "Twój wynik to: " + b + " Wynik przeciwnika to: " + a
                                , "Czy zgadzasz się z wynikiem?", JOptionPane.YES_NO_OPTION);
                        if (z == JOptionPane.YES_OPTION)
                        {
                            if(a > b  ) out.println("PORAZKA");
                            else if(a < b ) out.println("ZWYCIESTWO");
                            else out.println("REMIS");
                        }
                } else if (odpowiedz.startsWith("KONIEC_GRY")) {
                    if (kolor == 1) {
                        wynikGUI = new GUIWynik();
                        wynikGUI.okButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                wynik[0] = Integer.parseInt(wynikGUI.podajWynikTy.getText());
                                wynik[1] = Integer.parseInt(wynikGUI.podajWynikOn.getText());
                                out.println("WYNIK " + wynik[0] + " " + wynik[1]);
                                wynikGUI.zakonczenie.dispose();
                            }
                        });
                    }
                }
            }


            out.println("WYJSCIE");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            socket.close();
            planszaGUI.ramka.dispose();
        }
    }

    private void wysylajKomendy(){
        planszaGUI.zakonczGreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                out.println("WYJSCIE");
            }
        });

        planszaGUI.passButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                out.println("PASS");
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
                        out.println("RUCH " + finalA + " " + finalB);
                        System.out.println("Moj ruch> locX: " + finalA + ", locY: " + finalB);
                    }
                });
            }
        }
    }
}