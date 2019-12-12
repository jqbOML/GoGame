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

public class Klient{

    public JFrame ramka = new JFrame("Gra Go");
    private JLabel belkaStatusu = new JLabel("...");
    private GUIPlansza planszaGUI;
    private int[][] planszaKamieni;
    private JLabel[][] pole;
    private JLabel wybranePole;
    private JButton passButton = new JButton("<html>P<br>A<br>S<br>S");
    private JButton zakonczGreButton = new JButton("<html>K<br>O<br>N<br>I<br>E<br>C");
    private int rozmiarPola;
    private int kolorGracza;

    public static int rozmiarBokuPlanszy;

    private Socket socket;
    private Scanner inString;
    //private InputStream inObject;
    private PrintWriter out;

    Klient(String adresSerwera, int bokPlanszy) throws Exception {

        socket = new Socket(adresSerwera, 58901);
        inString = new Scanner(socket.getInputStream());
        //inObject = new ObjectInputStream(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);

        rozmiarBokuPlanszy = bokPlanszy - 1;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); //pobranie parametrów rozdzielczości ekranu
        rozmiarPola = (d.height / rozmiarBokuPlanszy);
        planszaGUI = new GUIPlansza();

    }

    public class GUIPlansza extends JPanel {
        Tekstury tekstury = new Tekstury(); //wczytanie grafik do GUI z folderu ~/tekstury/

        GUIPlansza() throws IOException {
            ramka.setVisible(true);
            ramka.setLocation(0, 0);
            ramka.setSize(rozmiarBokuPlanszy * rozmiarPola, rozmiarBokuPlanszy * rozmiarPola);
            ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            pole = new JLabel[rozmiarBokuPlanszy+1][rozmiarBokuPlanszy+1];
            planszaKamieni = new int[rozmiarBokuPlanszy+1][rozmiarBokuPlanszy+1];
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.setSize(rozmiarBokuPlanszy * rozmiarPola, rozmiarBokuPlanszy * rozmiarPola);
            mainPanel.setLocation(0,0);
            mainPanel.setBackground(Color.ORANGE);
            belkaStatusu.setBackground(Color.lightGray);
            mainPanel.add(belkaStatusu, BorderLayout.PAGE_START);
            mainPanel.add(this, BorderLayout.CENTER);
            //buttony
            passButton.setBackground(Color.DARK_GRAY);
            zakonczGreButton.setBackground(Color.DARK_GRAY);
            mainPanel.add(passButton, BorderLayout.PAGE_END);
            mainPanel.add(zakonczGreButton, BorderLayout.PAGE_END);
            ramka.add(mainPanel);
            ramka.setVisible(true);

            setLayout(new GridLayout(rozmiarBokuPlanszy+1, rozmiarBokuPlanszy+1, 0, 0));

            zakonczGreButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    out.println("WYJSCIE");
                }
            });

            passButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    out.println("passButton");
                }
            });

            /**
             * Inicjalizacja planszy o podanych (NxM) wymiarach, wgranie odpowiednich grafik
             */
            System.out.println("Plansza Width: "+rozmiarBokuPlanszy+", Plansza height: "+rozmiarBokuPlanszy);
            for(int b = 0; b < rozmiarBokuPlanszy + 1; b++) {
                for (int a = 0; a < rozmiarBokuPlanszy + 1; a++) {
                    planszaKamieni[a][b] = 0;
                    pole[a][b] = new JLabel();
                    pole[a][b].setLayout(new GridBagLayout());
                    //WCZYTANIE ODPOWIEDNIEJ TEKSTURY
                    if (a == 0)                        pole[a][b].setIcon(tekstury.Im_puste0x);
                    else if (b == 0)                   pole[a][b].setIcon(tekstury.Im_pustex0);
                    else if (a == rozmiarBokuPlanszy) pole[a][b].setIcon(tekstury.Im_puste8x);
                    else if (b == rozmiarBokuPlanszy) pole[a][b].setIcon(tekstury.Im_pustex8);
                    else                               pole[a][b].setIcon(tekstury.Im_pustexx);
                    //
                    int finalA = a;
                    int finalB = b;
                    pole[a][b].addMouseListener(new MouseAdapter() {
                        public void mousePressed(MouseEvent e) {
                            wybranePole = pole[finalA][finalB];
                            out.println("RUCH " + finalA + " " + finalB);
                            System.out.println("Moj ruch> locX: "+finalA+", locY: "+finalB);

                        }
                    });
                    /*pole[a][b].addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            if (planszaKamieni[finalA][finalB] == 0){
                                pole[finalA][finalB].setIcon(kolorGracza == 1 ? planszaGUI.tekstury.Im_czarnyxx : planszaGUI.tekstury.Im_bialyxx);
                                pole[finalA][finalB].repaint();
                            }
                        }
                    });
                    pole[a][b].addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseExited(MouseEvent e) {
                            if (finalA == 0){
                                if (planszaKamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarny0x);
                                else if (planszaKamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialy0x);
                                else pole[finalA][finalB].setIcon(tekstury.Im_puste0x);
                            }if (finalB == 0){
                                if (planszaKamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarnyx0);
                                else if (planszaKamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialyx0);
                                else pole[finalA][finalB].setIcon(tekstury.Im_pustex0);
                            }if (finalA == rozmiarBokuPlanszy){
                                if (planszaKamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarny8x);
                                else if (planszaKamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialy8x);
                                else pole[finalA][finalB].setIcon(tekstury.Im_puste8x);
                            }if (finalB == rozmiarBokuPlanszy){
                                if (planszaKamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarnyx8);
                                else if (planszaKamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialyx8);
                                else pole[finalA][finalB].setIcon(tekstury.Im_pustex8);
                            }else{
                                if (planszaKamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarnyxx);
                                else if (planszaKamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialyxx);
                                else pole[finalA][finalB].setIcon(tekstury.Im_pustexx);
                            }
                            if (finalA == 0 && finalB == 0){
                                if (planszaKamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarny00);
                                else if (planszaKamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialy00);
                                else pole[finalA][finalB].setIcon(tekstury.Im_puste00);
                            }
                            if (finalA == 0 && finalB == rozmiarBokuPlanszy){
                                if (planszaKamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarny08);
                                else if (planszaKamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialy08);
                                else pole[finalA][finalB].setIcon(tekstury.Im_puste08);
                            }
                            if (finalA == rozmiarBokuPlanszy && finalB == 0){
                                if (planszaKamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarny80);
                                else if (planszaKamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialy80);
                                else pole[finalA][finalB].setIcon(tekstury.Im_puste80);
                            }
                            if (finalA == rozmiarBokuPlanszy && finalB == rozmiarBokuPlanszy){
                                if (planszaKamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarny88);
                                else if (planszaKamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialy88);
                                else pole[finalA][finalB].setIcon(tekstury.Im_puste88);
                            }

                            pole[finalA][finalB].repaint();
                        }
                    });*/
                    add(pole[a][b]);
                }
            }

            pole[0][0].setIcon(tekstury.Im_puste00);
            pole[0][rozmiarBokuPlanszy].setIcon(tekstury.Im_puste08);
            pole[rozmiarBokuPlanszy][rozmiarBokuPlanszy].setIcon(tekstury.Im_puste88);
            pole[rozmiarBokuPlanszy][0].setIcon(tekstury.Im_puste80);
            repaint();
        }

        class Tekstury{
            ArrayList<ImageIcon> tekstury = new ArrayList<>();
            ImageIcon ImI_p00 = new ImageIcon("tekstury/00polePuste.jpg");
            //dostosuj rozmiare ikony
            Image newimg1 = ImI_p00.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_puste00 = new ImageIcon(newimg1);

            ImageIcon ImI_p0x = new ImageIcon("tekstury/0xpolePuste.jpg");
            //dostosuj rozmiare ikony
            Image newimg2 = ImI_p0x.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_puste0x = new ImageIcon(newimg2);

            ImageIcon ImI_p08 = new ImageIcon("tekstury/08polePuste.jpg");
            //dostosuj rozmiare ikony
            Image newimg3 = ImI_p08.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_puste08 = new ImageIcon(newimg3);

            ImageIcon ImI_p8x = new ImageIcon("tekstury/8xpolePuste.jpg");
            //dostosuj rozmiare ikony
            Image newimg4 = ImI_p8x.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_puste8x = new ImageIcon(newimg4);

            ImageIcon ImI_p80 = new ImageIcon("tekstury/80polePuste.jpg");
            //dostosuj rozmiare ikony
            Image newimg5 = ImI_p80.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_puste80 = new ImageIcon(newimg5);

            ImageIcon ImI_p88 = new ImageIcon("tekstury/88polePuste.jpg");
            //dostosuj rozmiare ikony
            Image newimg6 = ImI_p88.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_puste88 = new ImageIcon(newimg6);

            ImageIcon ImI_pxx = new ImageIcon("tekstury/srodkowepolePuste.jpg");
            //dostosuj rozmiare ikony
            Image newimg7 = ImI_pxx.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_pustexx = new ImageIcon(newimg7);

            ImageIcon ImI_px0 = new ImageIcon("tekstury/x0polePuste.jpg");
            //dostosuj rozmiare ikony
            Image newimg8 = ImI_px0.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_pustex0 = new ImageIcon(newimg8);

            ImageIcon ImI_px8 = new ImageIcon("tekstury/x8polePuste.jpg");
            //dostosuj rozmiare ikony
            Image newimg9 = ImI_px8.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_pustex8 = new ImageIcon(newimg9);

            ImageIcon ImI_cxx = new ImageIcon("tekstury/srodkowepoleCzarny.jpg");
            //dostosuj rozmiare ikony
            Image newimg10 = ImI_cxx.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_czarnyxx = new ImageIcon(newimg10);

            ImageIcon ImI_c0x = new ImageIcon("tekstury/0xpoleCzarny.jpg");
            //dostosuj rozmiare ikony
            Image newimg11 = ImI_c0x.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_czarny0x = new ImageIcon(newimg11);

            ImageIcon ImI_c00 = new ImageIcon("tekstury/00poleCzarny.jpg");
            //dostosuj rozmiare ikony
            Image newimg12 = ImI_c00.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_czarny00 = new ImageIcon(newimg12);

            ImageIcon ImI_c8x = new ImageIcon("tekstury/8xpoleCzarny.jpg");
            //dostosuj rozmiare ikony
            Image newimg13 = ImI_c8x.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_czarny8x = new ImageIcon(newimg13);

            ImageIcon ImI_c08 = new ImageIcon("tekstury/08poleCzarny.jpg");
            //dostosuj rozmiare ikony
            Image newimg14 = ImI_c08.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_czarny08 = new ImageIcon(newimg14);

            ImageIcon ImI_c80 = new ImageIcon("tekstury/80poleCzarny.jpg");
            //dostosuj rozmiare ikony
            Image newimg15 = ImI_c80.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_czarny80 = new ImageIcon(newimg15);

            ImageIcon ImI_c88 = new ImageIcon("tekstury/88poleCzarny.jpg");
            //dostosuj rozmiare ikony
            Image newimg16 = ImI_c88.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_czarny88 = new ImageIcon(newimg16);

            ImageIcon ImI_cx0 = new ImageIcon("tekstury/x0poleCzarny.jpg");
            //dostosuj rozmiare ikony
            Image newimg17 = ImI_cx0.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_czarnyx0 = new ImageIcon(newimg17);

            ImageIcon ImI_cx8 = new ImageIcon("tekstury/x8poleCzarny.jpg");
            //dostosuj rozmiare ikony
            Image newimg18 = ImI_cx8.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_czarnyx8 = new ImageIcon(newimg18);

            ImageIcon ImI_bxx = new ImageIcon("tekstury/srodkowepoleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg19 = ImI_bxx.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialyxx = new ImageIcon(newimg19);

            ImageIcon ImI_b0x = new ImageIcon("tekstury/0xpoleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg20 = ImI_b0x.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialy0x = new ImageIcon(newimg20);

            ImageIcon ImI_b00 = new ImageIcon("tekstury/00poleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg21 = ImI_b00.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialy00 = new ImageIcon(newimg21);

            ImageIcon ImI_bx0 = new ImageIcon("tekstury/x0poleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg22 = ImI_bx0.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialyx0 = new ImageIcon(newimg22);

            ImageIcon ImI_b8x = new ImageIcon("tekstury/8xpoleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg23 = ImI_b8x.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialy8x = new ImageIcon(newimg23);

            ImageIcon ImI_bx8 = new ImageIcon("tekstury/x8poleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg24 = ImI_bx8.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialyx8 = new ImageIcon(newimg24);

            ImageIcon ImI_b88 = new ImageIcon("tekstury/88poleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg25 = ImI_b88.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialy88 = new ImageIcon(newimg25);

            ImageIcon ImI_b80 = new ImageIcon("tekstury/80poleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg26 = ImI_b80.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialy80 = new ImageIcon(newimg26);

            ImageIcon ImI_b08 = new ImageIcon("tekstury/08poleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg27 = ImI_b08.getImage().getScaledInstance(rozmiarPola, rozmiarPola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialy08 = new ImageIcon(newimg27);
        }
    }

    public void odbierajKomendy() throws Exception {
        try {
            String odpowiedz = inString.nextLine();
            System.out.println("Wiadomosc z serwera: "+ odpowiedz);
            int kolor = Character.digit(odpowiedz.charAt(6), 10);
            System.out.println("Kolor: "+kolor);
            kolorGracza = kolor;
            ramka.setTitle("Gra Go: Gracz " + ((kolor == 1) ? "czarny" : "biały"));
            while (inString.hasNextLine()) {
                odpowiedz = inString.next();
                System.out.println("Respons inString.next: "+odpowiedz);
                if (odpowiedz.startsWith("POPRAWNY_RUCH")) {
                    belkaStatusu.setText("Runda przeciwnika, proszę czekać");
                    int locX = Integer.parseInt(inString.next());
                    int locY = Integer.parseInt(inString.next());
                    System.out.println("Moje> locX: "+locX+", locY: "+locY);
                    if(kolor == 1) {
                        wybranePole.setIcon(planszaGUI.tekstury.Im_czarnyxx);
                        planszaKamieni[locX][locY] = 1;
                    }
                    else {
                        wybranePole.setIcon(planszaGUI.tekstury.Im_bialyxx);
                        planszaKamieni[locX][locY] = 2;
                    }
                    wybranePole.repaint();
                } else if (odpowiedz.startsWith("RUCH_PRZECIWNIKA")) {
                    int locX = Integer.parseInt(inString.next());
                    int locY = Integer.parseInt(inString.next());
                    System.out.println("Przeciwnik> locX: "+locX+", locY: "+locY);
                    if(kolor == 1) {
                        pole[locX][locY].setIcon(planszaGUI.tekstury.Im_bialyxx);
                        planszaKamieni[locX][locY] = 2;
                    }
                    else {
                        pole[locX][locY].setIcon(planszaGUI.tekstury.Im_czarnyxx);
                        planszaKamieni[locX][locY] = 1;
                    }
                    pole[locX][locY].repaint();
                    belkaStatusu.setText("Przeciwnik wykonał ruch, Twoja kolej");
                } else if (odpowiedz.startsWith("INFO")) {
                    belkaStatusu.setText(inString.nextLine());
                /*} else if (odpowiedz.startsWith("ZWYCIESTWO")) {
                    JOptionPane.showMessageDialog(ramka, "Wygrałeś, gratulacje!");
                    break;
                } else if (odpowiedz.startsWith("PORAZKA")) {
                    JOptionPane.showMessageDialog(ramka, "Przeciwnik wygrał gre :(");
                    break;
                } else if (odpowiedz.startsWith("REMIS")) {
                    JOptionPane.showMessageDialog(ramka, "Remis!");
                    break;*/
                } else if (odpowiedz.startsWith("PRZECIWNIK_ZPASOWAL")){
                    belkaStatusu.setText("Przeciwnik zpasował, twój ruch!");
                } else if (odpowiedz.startsWith("ZPASOWALES")){
                belkaStatusu.setText("Zpsaowałeś, ruch preciwnika!");
                }

                else if (odpowiedz.startsWith("PRZECIWNIK_WYSZEDL")) {
                    JOptionPane.showMessageDialog(ramka, "Przeciwnik wyszedł z gry!");
                    break;
                }
            }
            out.println("WYJSCIE");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            socket.close();
            ramka.dispose();
        }
    }
}