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
    private GUI_Plansza plansza_gui;
    private int[][] plansza_kamieni;
    private JLabel[][] pole;
    private JLabel wybrane_pole;
    private int rozmiar_pola;
    private int kolor_gracza;

    public static int rozmiarBoku_planszy;

    private Socket socket;
    private Scanner inString;
    //private InputStream inObject;
    private PrintWriter out;

    Klient(String adresSerwera, int bok_planszy) throws Exception {

        socket = new Socket(adresSerwera, 58901);
        inString = new Scanner(socket.getInputStream());
        //inObject = new ObjectInputStream(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);

        rozmiarBoku_planszy = bok_planszy - 1;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); //pobranie parametrów rozdzielczości ekranu
        rozmiar_pola = (d.height / rozmiarBoku_planszy);
        plansza_gui = new GUI_Plansza();

    }

    public class GUI_Plansza extends JPanel {
        Tekstury tekstury = new Tekstury(); //wczytanie grafik do GUI z folderu ~/tekstury/

        GUI_Plansza() throws IOException {
            ramka.setVisible(true);
            ramka.setLocation(0, 0);
            ramka.setSize(rozmiarBoku_planszy * rozmiar_pola, rozmiarBoku_planszy * rozmiar_pola);
            ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            pole = new JLabel[rozmiarBoku_planszy+1][rozmiarBoku_planszy+1];
            plansza_kamieni = new int[rozmiarBoku_planszy+1][rozmiarBoku_planszy+1];
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.setSize(rozmiarBoku_planszy * rozmiar_pola, rozmiarBoku_planszy * rozmiar_pola);
            mainPanel.setLocation(0,0);
            mainPanel.setBackground(Color.ORANGE);
            belkaStatusu.setBackground(Color.lightGray);
            mainPanel.add(belkaStatusu, BorderLayout.PAGE_START);
            mainPanel.add(this, BorderLayout.CENTER);
            ramka.add(mainPanel);
            ramka.setVisible(true);
            setLayout(new GridLayout(rozmiarBoku_planszy+1, rozmiarBoku_planszy+1, 0, 0));

            /**
             * Inicjalizacja planszy o podanych (NxM) wymiarach, wgranie odpowiednich grafik
             */
            System.out.println("Plansza Width: "+rozmiarBoku_planszy+", Plansza height: "+rozmiarBoku_planszy);
            for(int b = 0; b < rozmiarBoku_planszy + 1; b++) {
                for (int a = 0; a < rozmiarBoku_planszy + 1; a++) {
                    plansza_kamieni[a][b] = 0;
                    pole[a][b] = new JLabel();
                    pole[a][b].setLayout(new GridBagLayout());
                    //WCZYTANIE ODPOWIEDNIEJ TEKSTURY
                    if (a == 0)                        pole[a][b].setIcon(tekstury.Im_puste0x);
                    else if (b == 0)                   pole[a][b].setIcon(tekstury.Im_pustex0);
                    else if (a == rozmiarBoku_planszy) pole[a][b].setIcon(tekstury.Im_puste8x);
                    else if (b == rozmiarBoku_planszy) pole[a][b].setIcon(tekstury.Im_pustex8);
                    else                               pole[a][b].setIcon(tekstury.Im_pustexx);
                    //
                    int finalA = a;
                    int finalB = b;
                    pole[a][b].addMouseListener(new MouseAdapter() {
                        public void mousePressed(MouseEvent e) {
                            wybrane_pole = pole[finalA][finalB];
                            out.println("RUCH " + finalA + " " + finalB);
                            System.out.println("Moj ruch> locX: "+finalA+", locY: "+finalB);

                        }
                    });
                    /*pole[a][b].addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            if (plansza_kamieni[finalA][finalB] == 0){
                                pole[finalA][finalB].setIcon(kolor_gracza == 1 ? plansza_gui.tekstury.Im_czarnyxx : plansza_gui.tekstury.Im_bialyxx);
                                pole[finalA][finalB].repaint();
                            }
                        }
                    });
                    pole[a][b].addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseExited(MouseEvent e) {
                            if (finalA == 0){
                                if (plansza_kamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarny0x);
                                else if (plansza_kamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialy0x);
                                else pole[finalA][finalB].setIcon(tekstury.Im_puste0x);
                            }if (finalB == 0){
                                if (plansza_kamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarnyx0);
                                else if (plansza_kamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialyx0);
                                else pole[finalA][finalB].setIcon(tekstury.Im_pustex0);
                            }if (finalA == rozmiarBoku_planszy){
                                if (plansza_kamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarny8x);
                                else if (plansza_kamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialy8x);
                                else pole[finalA][finalB].setIcon(tekstury.Im_puste8x);
                            }if (finalB == rozmiarBoku_planszy){
                                if (plansza_kamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarnyx8);
                                else if (plansza_kamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialyx8);
                                else pole[finalA][finalB].setIcon(tekstury.Im_pustex8);
                            }else{
                                if (plansza_kamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarnyxx);
                                else if (plansza_kamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialyxx);
                                else pole[finalA][finalB].setIcon(tekstury.Im_pustexx);
                            }
                            if (finalA == 0 && finalB == 0){
                                if (plansza_kamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarny00);
                                else if (plansza_kamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialy00);
                                else pole[finalA][finalB].setIcon(tekstury.Im_puste00);
                            }
                            if (finalA == 0 && finalB == rozmiarBoku_planszy){
                                if (plansza_kamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarny08);
                                else if (plansza_kamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialy08);
                                else pole[finalA][finalB].setIcon(tekstury.Im_puste08);
                            }
                            if (finalA == rozmiarBoku_planszy && finalB == 0){
                                if (plansza_kamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarny80);
                                else if (plansza_kamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialy80);
                                else pole[finalA][finalB].setIcon(tekstury.Im_puste80);
                            }
                            if (finalA == rozmiarBoku_planszy && finalB == rozmiarBoku_planszy){
                                if (plansza_kamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarny88);
                                else if (plansza_kamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialy88);
                                else pole[finalA][finalB].setIcon(tekstury.Im_puste88);
                            }

                            pole[finalA][finalB].repaint();
                        }
                    });*/
                    add(pole[a][b]);
                }
            }

            pole[0][0].setIcon(tekstury.Im_puste00);
            pole[0][rozmiarBoku_planszy].setIcon(tekstury.Im_puste08);
            pole[rozmiarBoku_planszy][rozmiarBoku_planszy].setIcon(tekstury.Im_puste88);
            pole[rozmiarBoku_planszy][0].setIcon(tekstury.Im_puste80);
            repaint();
        }

        class Tekstury{
            ArrayList<ImageIcon> tekstury = new ArrayList<>();
            ImageIcon ImI_p00 = new ImageIcon("tekstury/00polePuste.jpg");
            //dostosuj rozmiare ikony
            Image newimg1 = ImI_p00.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_puste00 = new ImageIcon(newimg1);

            ImageIcon ImI_p0x = new ImageIcon("tekstury/0xpolePuste.jpg");
            //dostosuj rozmiare ikony
            Image newimg2 = ImI_p0x.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_puste0x = new ImageIcon(newimg2);

            ImageIcon ImI_p08 = new ImageIcon("tekstury/08polePuste.jpg");
            //dostosuj rozmiare ikony
            Image newimg3 = ImI_p08.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_puste08 = new ImageIcon(newimg3);

            ImageIcon ImI_p8x = new ImageIcon("tekstury/8xpolePuste.jpg");
            //dostosuj rozmiare ikony
            Image newimg4 = ImI_p8x.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_puste8x = new ImageIcon(newimg4);

            ImageIcon ImI_p80 = new ImageIcon("tekstury/80polePuste.jpg");
            //dostosuj rozmiare ikony
            Image newimg5 = ImI_p80.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_puste80 = new ImageIcon(newimg5);

            ImageIcon ImI_p88 = new ImageIcon("tekstury/88polePuste.jpg");
            //dostosuj rozmiare ikony
            Image newimg6 = ImI_p88.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_puste88 = new ImageIcon(newimg6);

            ImageIcon ImI_pxx = new ImageIcon("tekstury/srodkowepolePuste.jpg");
            //dostosuj rozmiare ikony
            Image newimg7 = ImI_pxx.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_pustexx = new ImageIcon(newimg7);

            ImageIcon ImI_px0 = new ImageIcon("tekstury/x0polePuste.jpg");
            //dostosuj rozmiare ikony
            Image newimg8 = ImI_px0.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_pustex0 = new ImageIcon(newimg8);

            ImageIcon ImI_px8 = new ImageIcon("tekstury/x8polePuste.jpg");
            //dostosuj rozmiare ikony
            Image newimg9 = ImI_px8.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_pustex8 = new ImageIcon(newimg9);

            ImageIcon ImI_cxx = new ImageIcon("tekstury/srodkowepoleCzarny.jpg");
            //dostosuj rozmiare ikony
            Image newimg10 = ImI_cxx.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_czarnyxx = new ImageIcon(newimg10);

            ImageIcon ImI_c0x = new ImageIcon("tekstury/0xpoleCzarny.jpg");
            //dostosuj rozmiare ikony
            Image newimg11 = ImI_c0x.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_czarny0x = new ImageIcon(newimg11);

            ImageIcon ImI_c00 = new ImageIcon("tekstury/00poleCzarny.jpg");
            //dostosuj rozmiare ikony
            Image newimg12 = ImI_c00.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_czarny00 = new ImageIcon(newimg12);

            ImageIcon ImI_c8x = new ImageIcon("tekstury/8xpoleCzarny.jpg");
            //dostosuj rozmiare ikony
            Image newimg13 = ImI_c8x.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_czarny8x = new ImageIcon(newimg13);

            ImageIcon ImI_c08 = new ImageIcon("tekstury/08poleCzarny.jpg");
            //dostosuj rozmiare ikony
            Image newimg14 = ImI_c08.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_czarny08 = new ImageIcon(newimg14);

            ImageIcon ImI_c80 = new ImageIcon("tekstury/80poleCzarny.jpg");
            //dostosuj rozmiare ikony
            Image newimg15 = ImI_c80.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_czarny80 = new ImageIcon(newimg15);

            ImageIcon ImI_c88 = new ImageIcon("tekstury/88poleCzarny.jpg");
            //dostosuj rozmiare ikony
            Image newimg16 = ImI_c88.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_czarny88 = new ImageIcon(newimg16);

            ImageIcon ImI_cx0 = new ImageIcon("tekstury/x0poleCzarny.jpg");
            //dostosuj rozmiare ikony
            Image newimg17 = ImI_cx0.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_czarnyx0 = new ImageIcon(newimg17);

            ImageIcon ImI_cx8 = new ImageIcon("tekstury/x8poleCzarny.jpg");
            //dostosuj rozmiare ikony
            Image newimg18 = ImI_cx8.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_czarnyx8 = new ImageIcon(newimg18);

            ImageIcon ImI_bxx = new ImageIcon("tekstury/srodkowepoleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg19 = ImI_bxx.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialyxx = new ImageIcon(newimg19);

            ImageIcon ImI_b0x = new ImageIcon("tekstury/0xpoleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg20 = ImI_b0x.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialy0x = new ImageIcon(newimg20);

            ImageIcon ImI_b00 = new ImageIcon("tekstury/00poleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg21 = ImI_b00.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialy00 = new ImageIcon(newimg21);

            ImageIcon ImI_bx0 = new ImageIcon("tekstury/x0poleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg22 = ImI_bx0.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialyx0 = new ImageIcon(newimg22);

            ImageIcon ImI_b8x = new ImageIcon("tekstury/8xpoleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg23 = ImI_b8x.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialy8x = new ImageIcon(newimg23);

            ImageIcon ImI_bx8 = new ImageIcon("tekstury/x8poleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg24 = ImI_bx8.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialyx8 = new ImageIcon(newimg24);

            ImageIcon ImI_b88 = new ImageIcon("tekstury/88poleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg25 = ImI_b88.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialy88 = new ImageIcon(newimg25);

            ImageIcon ImI_b80 = new ImageIcon("tekstury/80poleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg26 = ImI_b80.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialy80 = new ImageIcon(newimg26);

            ImageIcon ImI_b08 = new ImageIcon("tekstury/08poleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg27 = ImI_b08.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialy08 = new ImageIcon(newimg27);
        }
    }

    /**
     * The main thread of the client will listen for messages from the server.
     * The first message will be a "WELCOME" message inString which we receive our
     * mark. Then we go into a loop listening for any of the other messages,
     * and handling each message appropriately. The "VICTORY", "DEFEAT", "TIE",
     *  and "OTHER_PLAYER_LEFT" messages the loop is exited and the server
     * is sent a "QUIT" message.
     */
    public void graj() throws Exception {
        try {
            String odpowiedz = inString.nextLine();
            System.out.println("Wiadomosc z serwera: "+ odpowiedz);
            int kolor = Character.digit(odpowiedz.charAt(6), 10);
            System.out.println("Kolor: "+kolor);
            kolor_gracza = kolor;
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
                        wybrane_pole.setIcon(plansza_gui.tekstury.Im_czarnyxx);
                        plansza_kamieni[locX][locY] = 1;
                    }
                    else {
                        wybrane_pole.setIcon(plansza_gui.tekstury.Im_bialyxx);
                        plansza_kamieni[locX][locY] = 2;
                    }
                    wybrane_pole.repaint();
                } else if (odpowiedz.startsWith("RUCH_PRZECIWNIKA")) {
                    int locX = Integer.parseInt(inString.next());
                    int locY = Integer.parseInt(inString.next());
                    System.out.println("Przeciwnik> locX: "+locX+", locY: "+locY);
                    if(kolor == 1) {
                        pole[locX][locY].setIcon(plansza_gui.tekstury.Im_bialyxx);
                        plansza_kamieni[locX][locY] = 2;
                    }
                    else {
                        pole[locX][locY].setIcon(plansza_gui.tekstury.Im_czarnyxx);
                        plansza_kamieni[locX][locY] = 1;
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
                } else if (odpowiedz.startsWith("PRZECIWNIK_WYSZEDL")) {
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