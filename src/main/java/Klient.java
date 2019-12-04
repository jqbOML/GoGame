import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
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
    private static GUI_Plansza plansza_go;
    private JLabel[][] pole;
    private JLabel wybrane_pole;
    private int rozmiar_pola;

    public static int rozmiarBoku_planszy;

    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    Klient(String adresSerwera, int bok_planszy) throws Exception {

        socket = new Socket(adresSerwera, 58901);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);

        rozmiarBoku_planszy = bok_planszy - 1;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); //pobranie parametrów rozdzielczości ekranu
        rozmiar_pola = (d.height / rozmiarBoku_planszy);

        ramka.setVisible(true);
        ramka.setLocation(0, 0);
        ramka.setSize(rozmiarBoku_planszy * rozmiar_pola, rozmiarBoku_planszy * rozmiar_pola);
        ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pole = new JLabel[rozmiarBoku_planszy+1][rozmiarBoku_planszy+1];
        plansza_go = new GUI_Plansza();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setSize(rozmiarBoku_planszy * rozmiar_pola, rozmiarBoku_planszy * rozmiar_pola);
        mainPanel.setLocation(0,0);
        mainPanel.setBackground(Color.ORANGE);
        belkaStatusu.setBackground(Color.lightGray);
        mainPanel.add(belkaStatusu,BorderLayout.PAGE_START);
        mainPanel.add(plansza_go,BorderLayout.CENTER);
        ramka.add(mainPanel);
        ramka.setVisible(true);

    }

    public class GUI_Plansza extends JPanel {
        Tekstury tekstury = new Tekstury();

        GUI_Plansza() throws IOException {
            setLayout(new GridLayout(rozmiarBoku_planszy+1, rozmiarBoku_planszy+1, 0, 0));

            /**
             * Inicjalizacja planszy o podanych (NxM) wymiarach
             */
            System.out.println("Plansza Width: "+rozmiarBoku_planszy+", Plansza height: "+rozmiarBoku_planszy);
            for(int b = 0; b < rozmiarBoku_planszy + 1; b++) {
                for (int a = 0; a < rozmiarBoku_planszy + 1; a++) {
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

            ImageIcon ImI_bxx = new ImageIcon("tekstury/srodkowepoleBialy.jpg");
            //dostosuj rozmiare ikony
            Image newimg11 = ImI_bxx.getImage().getScaledInstance(rozmiar_pola, rozmiar_pola,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon Im_bialyxx = new ImageIcon(newimg11);
        }
    }

    /**
     * The main thread of the client will listen for messages from the server.
     * The first message will be a "WELCOME" message in which we receive our
     * mark. Then we go into a loop listening for any of the other messages,
     * and handling each message appropriately. The "VICTORY", "DEFEAT", "TIE",
     *  and "OTHER_PLAYER_LEFT" messages the loop is exited and the server
     * is sent a "QUIT" message.
     */
    public void graj() throws Exception {
        try {
            String odpowiedz = in.nextLine();
            System.out.println("Wiadomosc z serwera: "+ odpowiedz);
            int kolor = Character.digit(odpowiedz.charAt(6), 10);
            System.out.println("Kolor: "+kolor);
            ramka.setTitle("Gra Go: Gracz " + ((kolor == 1) ? "czarny" : "biały"));
            while (in.hasNextLine()) {
                odpowiedz = in.next();
                System.out.println("Respons in.next: "+odpowiedz);
                if (odpowiedz.startsWith("POPRAWNY_RUCH")) {
                    belkaStatusu.setText("Runda przeciwnika, proszę czekać");
                    if(kolor == 1) wybrane_pole.setIcon(plansza_go.tekstury.Im_czarnyxx);
                    else wybrane_pole.setIcon(plansza_go.tekstury.Im_bialyxx);
                    wybrane_pole.repaint();
                } else if (odpowiedz.startsWith("RUCH_PRZECIWNIKA")) {
                    int locX = Integer.parseInt(in.next());
                    int locY = Integer.parseInt(in.next());
                    System.out.println("Przeciwnik> locX: "+locX+", locY: "+locY);
                    if(kolor == 1) pole[locX][locY].setIcon(plansza_go.tekstury.Im_bialyxx);
                    else pole[locX][locY].setIcon(plansza_go.tekstury.Im_czarnyxx);
                    pole[locX][locY].repaint();
                    belkaStatusu.setText("Przeciwnik wykonał ruch, Twoja kolej");
                } else if (odpowiedz.startsWith("INFO")) {
                    belkaStatusu.setText(in.nextLine());
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