package Klient;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class Klient{
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private GUIPlansza planszaGUI;
    private JLabel wybranePole;
    private int[] wynik = new int[2];


    Klient(String adresSerwera) throws Exception {
        socket = new Socket(adresSerwera, 58901);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);

        planszaGUI = new GUIPlansza();
        wysylajKomendy();
        odbierajKomendy();
    }

    private void odbierajKomendy() throws Exception {
        try {
            String odpowiedz = in.nextLine();
                System.out.println("Wiadomosc z serwera: "+ odpowiedz);
            int kolor = Character.digit(odpowiedz.charAt(6), 10);
            planszaGUI.kolorGracza = kolor;
            planszaGUI.ramka.setTitle("Gra Go: Serwer.Gracz " + ((kolor == 1) ? "czarny" : "biały"));
            while (in.hasNextLine()) {
                odpowiedz = in.next();
                    System.out.println("Respons in.next: "+odpowiedz);
                if (odpowiedz.startsWith("POPRAWNY_RUCH")) {
                    planszaGUI.belkaStatusu.setText("Runda przeciwnika, proszę czekać");
                    int locX = Integer.parseInt(in.next());
                    int locY = Integer.parseInt(in.next());
                    System.out.println("Moje> locX: "+locX+", locY: "+locY);
                    if(kolor == 1) {
                        wybranePole.setIcon(planszaGUI.tekstury.Im_czarnyxx);
                        planszaGUI.planszaKamieni[locX][locY] = 1;
                    }
                    else {
                        wybranePole.setIcon(planszaGUI.tekstury.Im_bialyxx);
                        planszaGUI.planszaKamieni[locX][locY] = 2;
                    }
                    wybranePole.repaint();
                } else if (odpowiedz.startsWith("RUCH_PRZECIWNIKA")) {
                    int locX = Integer.parseInt(in.next());
                    int locY = Integer.parseInt(in.next());
                    System.out.println("Przeciwnik> locX: "+locX+", locY: "+locY);
                    if(kolor == 1) {
                        planszaGUI.pole[locX][locY].setIcon(planszaGUI.tekstury.Im_bialyxx);
                        planszaGUI.planszaKamieni[locX][locY] = 2;
                    }
                    else {
                        planszaGUI.pole[locX][locY].setIcon(planszaGUI.tekstury.Im_czarnyxx);
                        planszaGUI.planszaKamieni[locX][locY] = 1;
                    }
                    planszaGUI.pole[locX][locY].repaint();
                    planszaGUI.belkaStatusu.setText("Przeciwnik wykonał ruch, Twoja kolej");
                } else if (odpowiedz.startsWith("INFO")) {
                    planszaGUI.belkaStatusu.setText(in.nextLine());
                } else if (odpowiedz.startsWith("ZWYCIESTWO")) {
                    JOptionPane.showMessageDialog(planszaGUI.ramka, "Wygrałeś, gratulacje!");
                } else if (odpowiedz.startsWith("PORAZKA")) {
                    JOptionPane.showMessageDialog(planszaGUI.ramka, "Przeciwnik wygrał gre :(");
                } else if (odpowiedz.startsWith("REMIS")) {
                    JOptionPane.showMessageDialog(planszaGUI.ramka, "Remis!");
                } else if (odpowiedz.startsWith("PRZECIWNIK_SPASOWAL")){
                    planszaGUI.belkaStatusu.setText("Przeciwnik spasował, twój ruch!");
                } else if (odpowiedz.startsWith("SPASOWALES")){
                    planszaGUI.belkaStatusu.setText("Spasowałeś, ruch przeciwnika!");
                }

                else if (odpowiedz.startsWith("PRZECIWNIK_WYSZEDL")) {
                    JOptionPane.showMessageDialog(planszaGUI.ramka, "Przeciwnik wyszedł z gry!");
                    break;
                }
                else if (odpowiedz.startsWith("KONIEC_GRY")) {
                    /*
                        TODO: stworzyć nową osobną klasę dla okna końcowego, która tutaj będzie wywoływana
                     */
                    JFrame zakonczenie = new JFrame("Second");
                    JTextArea podajWynikTy = new JTextArea(1, 10);
                    JTextArea podajWynikOn = new JTextArea(1, 10);
                    JButton okButton = new JButton("OK");
                    if (kolor == 1) {
                        zakonczenie.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        zakonczenie.setSize(400, 100);
                        zakonczenie.setLocationRelativeTo(null);
                        zakonczenie.setVisible(true);
                        zakonczenie.setTitle("Podsumowanie: Gracz biały");
                        zakonczenie.add(podajWynikTy, BorderLayout.WEST);
                        zakonczenie.add(okButton, BorderLayout.CENTER);
                        podajWynikTy.setBorder(new TitledBorder("TWÓJ WYNIK"));
                        zakonczenie.add(podajWynikOn, BorderLayout.EAST);
                        podajWynikOn.setBorder(new TitledBorder("WYNIK PZECIWNIKA"));
                        okButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                    wynik[0] = Integer.parseInt(podajWynikTy.getText());
                                    wynik[1] = Integer.parseInt(podajWynikOn.getText());
                                if(wynik[0] > wynik[1] && wynik[0] != 0 && wynik[1] != 0 ) out.println("PORAZKA");
                                else if(wynik[0] < wynik[1] && wynik[0] != 0 && wynik[1] != 0) out.println("ZWYCIESTWO");
                                else if(wynik[0] == wynik[1] && wynik[0] != 0 && wynik[1] != 0) out.println("REMIS");
                                zakonczenie.dispose();
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