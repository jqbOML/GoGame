package Klient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

class GUIPlansza extends JPanel {
    static int ROZMIAR_PLANSZY = 19;
    int kolorGracza; //1 - czarny, 2 - biały
    JFrame ramka = new JFrame("Gra Go");
    JLabel belkaStatusu = new JLabel("...");
    JButton passButton = new JButton("PASS");
    JButton zakonczGreButton = new JButton("KONIEC");
    JLabel[][] pole;
    private Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); //pobranie parametrów rozdzielczości ekranu
    private int ROZMIAR_POLA = (d.height / ROZMIAR_PLANSZY);
    int[][] planszaKamieni;
    Tekstury tekstury = new Tekstury(); //wczytanie grafik do GUI z folderu ~/tekstury/

    GUIPlansza() throws IOException {
        ramka.setVisible(true);
        ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ramka.setLocation(0, 0);
        ramka.setSize(ROZMIAR_PLANSZY * ROZMIAR_POLA, ROZMIAR_PLANSZY * ROZMIAR_POLA);

        pole = new JLabel[ROZMIAR_PLANSZY][ROZMIAR_PLANSZY];
        planszaKamieni = new int[ROZMIAR_PLANSZY][ROZMIAR_PLANSZY];
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setSize(ROZMIAR_PLANSZY * ROZMIAR_POLA, ROZMIAR_PLANSZY * ROZMIAR_POLA);
        mainPanel.setLocation(0,0);
        mainPanel.setBackground(Color.ORANGE);
        belkaStatusu.setBackground(Color.lightGray);
        mainPanel.add(belkaStatusu, BorderLayout.PAGE_START);
        mainPanel.add(this, BorderLayout.CENTER);
        passButton.setForeground(Color.WHITE);
        passButton.setBackground(Color.DARK_GRAY);
        zakonczGreButton.setBackground(Color.DARK_GRAY);
        zakonczGreButton.setForeground(Color.WHITE);
        JPanel basePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        basePanel.add(passButton);
        basePanel.add(zakonczGreButton);
        mainPanel.add(basePanel, BorderLayout.PAGE_END);
        ramka.add(mainPanel);
        ramka.setVisible(true);

        setLayout(new GridLayout(ROZMIAR_PLANSZY, ROZMIAR_PLANSZY, 0, 0));

        

        /**
         * Inicjalizacja planszy 19x19, wgranie odpowiednich grafik
         */
        for(int b = 0; b < ROZMIAR_PLANSZY; b++) {
            for (int a = 0; a < ROZMIAR_PLANSZY; a++) {
                planszaKamieni[a][b] = 0;
                pole[a][b] = new JLabel();
                pole[a][b].setLayout(new GridBagLayout());
                //WCZYTANIE ODPOWIEDNIEJ TEKSTURY
                if (a == 0)                        pole[a][b].setIcon(tekstury.Im_puste0x);
                else if (b == 0)                   pole[a][b].setIcon(tekstury.Im_pustex0);
                else if (a == ROZMIAR_PLANSZY - 1) pole[a][b].setIcon(tekstury.Im_puste8x);
                else if (b == ROZMIAR_PLANSZY - 1) pole[a][b].setIcon(tekstury.Im_pustex8);
                else                               pole[a][b].setIcon(tekstury.Im_pustexx);
                //

                int finalA = a;
                int finalB = b;
                pole[a][b].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (planszaKamieni[finalA][finalB] == 0){
                            pole[finalA][finalB].setIcon(kolorGracza == 1 ? tekstury.Im_czarnyxx : tekstury.Im_bialyxx);
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
                        }else if (finalB == 0){
                            if (planszaKamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarnyx0);
                            else if (planszaKamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialyx0);
                            else pole[finalA][finalB].setIcon(tekstury.Im_pustex0);
                        }else if (finalA == ROZMIAR_PLANSZY - 1){
                            if (planszaKamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarny8x);
                            else if (planszaKamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialy8x);
                            else pole[finalA][finalB].setIcon(tekstury.Im_puste8x);
                        }else if (finalB == ROZMIAR_PLANSZY - 1){
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
                        if (finalA == 0 && finalB == ROZMIAR_PLANSZY - 1){
                            if (planszaKamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarny08);
                            else if (planszaKamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialy08);
                            else pole[finalA][finalB].setIcon(tekstury.Im_puste08);
                        }
                        if (finalA == ROZMIAR_PLANSZY - 1 && finalB == 0){
                            if (planszaKamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarny80);
                            else if (planszaKamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialy80);
                            else pole[finalA][finalB].setIcon(tekstury.Im_puste80);
                        }
                        if (finalA == ROZMIAR_PLANSZY - 1 && finalB == ROZMIAR_PLANSZY - 1){
                            if (planszaKamieni[finalA][finalB] == 1) pole[finalA][finalB].setIcon(tekstury.Im_czarny88);
                            else if (planszaKamieni[finalA][finalB] == 2) pole[finalA][finalB].setIcon(tekstury.Im_bialy88);
                            else pole[finalA][finalB].setIcon(tekstury.Im_puste88);
                        }

                        pole[finalA][finalB].repaint();
                    }
                });

                add(pole[a][b]);
            }
        }

        //korekcja tekstur na rogach planszy
        pole[0][0].setIcon(tekstury.Im_puste00);
        pole[0][ROZMIAR_PLANSZY - 1].setIcon(tekstury.Im_puste08);
        pole[ROZMIAR_PLANSZY - 1][ROZMIAR_PLANSZY - 1].setIcon(tekstury.Im_puste88);
        pole[ROZMIAR_PLANSZY - 1][0].setIcon(tekstury.Im_puste80);
        repaint();
    }

    class Tekstury{
        ArrayList<ImageIcon> tekstury = new ArrayList<>();
        ImageIcon ImI_p00 = new ImageIcon("tekstury/00polePuste.jpg");
        //dostosuj rozmiare ikony
        Image newimg1 = ImI_p00.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_puste00 = new ImageIcon(newimg1);

        ImageIcon ImI_p0x = new ImageIcon("tekstury/0xpolePuste.jpg");
        //dostosuj rozmiare ikony
        Image newimg2 = ImI_p0x.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_puste0x = new ImageIcon(newimg2);

        ImageIcon ImI_p08 = new ImageIcon("tekstury/08polePuste.jpg");
        //dostosuj rozmiare ikony
        Image newimg3 = ImI_p08.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_puste08 = new ImageIcon(newimg3);

        ImageIcon ImI_p8x = new ImageIcon("tekstury/8xpolePuste.jpg");
        //dostosuj rozmiare ikony
        Image newimg4 = ImI_p8x.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_puste8x = new ImageIcon(newimg4);

        ImageIcon ImI_p80 = new ImageIcon("tekstury/80polePuste.jpg");
        //dostosuj rozmiare ikony
        Image newimg5 = ImI_p80.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_puste80 = new ImageIcon(newimg5);

        ImageIcon ImI_p88 = new ImageIcon("tekstury/88polePuste.jpg");
        //dostosuj rozmiare ikony
        Image newimg6 = ImI_p88.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_puste88 = new ImageIcon(newimg6);

        ImageIcon ImI_pxx = new ImageIcon("tekstury/srodkowepolePuste.jpg");
        //dostosuj rozmiare ikony
        Image newimg7 = ImI_pxx.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_pustexx = new ImageIcon(newimg7);

        ImageIcon ImI_px0 = new ImageIcon("tekstury/x0polePuste.jpg");
        //dostosuj rozmiare ikony
        Image newimg8 = ImI_px0.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_pustex0 = new ImageIcon(newimg8);

        ImageIcon ImI_px8 = new ImageIcon("tekstury/x8polePuste.jpg");
        //dostosuj rozmiare ikony
        Image newimg9 = ImI_px8.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_pustex8 = new ImageIcon(newimg9);

        ImageIcon ImI_cxx = new ImageIcon("tekstury/srodkowepoleCzarny.jpg");
        //dostosuj rozmiare ikony
        Image newimg10 = ImI_cxx.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_czarnyxx = new ImageIcon(newimg10);

        ImageIcon ImI_c0x = new ImageIcon("tekstury/0xpoleCzarny.jpg");
        //dostosuj rozmiare ikony
        Image newimg11 = ImI_c0x.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_czarny0x = new ImageIcon(newimg11);

        ImageIcon ImI_c00 = new ImageIcon("tekstury/00poleCzarny.jpg");
        //dostosuj rozmiare ikony
        Image newimg12 = ImI_c00.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_czarny00 = new ImageIcon(newimg12);

        ImageIcon ImI_c8x = new ImageIcon("tekstury/8xpoleCzarny.jpg");
        //dostosuj rozmiare ikony
        Image newimg13 = ImI_c8x.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_czarny8x = new ImageIcon(newimg13);

        ImageIcon ImI_c08 = new ImageIcon("tekstury/08poleCzarny.jpg");
        //dostosuj rozmiare ikony
        Image newimg14 = ImI_c08.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_czarny08 = new ImageIcon(newimg14);

        ImageIcon ImI_c80 = new ImageIcon("tekstury/80poleCzarny.jpg");
        //dostosuj rozmiare ikony
        Image newimg15 = ImI_c80.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_czarny80 = new ImageIcon(newimg15);

        ImageIcon ImI_c88 = new ImageIcon("tekstury/88poleCzarny.jpg");
        //dostosuj rozmiare ikony
        Image newimg16 = ImI_c88.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_czarny88 = new ImageIcon(newimg16);

        ImageIcon ImI_cx0 = new ImageIcon("tekstury/x0poleCzarny.jpg");
        //dostosuj rozmiare ikony
        Image newimg17 = ImI_cx0.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_czarnyx0 = new ImageIcon(newimg17);

        ImageIcon ImI_cx8 = new ImageIcon("tekstury/x8poleCzarny.jpg");
        //dostosuj rozmiare ikony
        Image newimg18 = ImI_cx8.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_czarnyx8 = new ImageIcon(newimg18);

        ImageIcon ImI_bxx = new ImageIcon("tekstury/srodkowepoleBialy.jpg");
        //dostosuj rozmiare ikony
        Image newimg19 = ImI_bxx.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_bialyxx = new ImageIcon(newimg19);

        ImageIcon ImI_b0x = new ImageIcon("tekstury/0xpoleBialy.jpg");
        //dostosuj rozmiare ikony
        Image newimg20 = ImI_b0x.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_bialy0x = new ImageIcon(newimg20);

        ImageIcon ImI_b00 = new ImageIcon("tekstury/00poleBialy.jpg");
        //dostosuj rozmiare ikony
        Image newimg21 = ImI_b00.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_bialy00 = new ImageIcon(newimg21);

        ImageIcon ImI_bx0 = new ImageIcon("tekstury/x0poleBialy.jpg");
        //dostosuj rozmiare ikony
        Image newimg22 = ImI_bx0.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_bialyx0 = new ImageIcon(newimg22);

        ImageIcon ImI_b8x = new ImageIcon("tekstury/8xpoleBialy.jpg");
        //dostosuj rozmiare ikony
        Image newimg23 = ImI_b8x.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_bialy8x = new ImageIcon(newimg23);

        ImageIcon ImI_bx8 = new ImageIcon("tekstury/x8poleBialy.jpg");
        //dostosuj rozmiare ikony
        Image newimg24 = ImI_bx8.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_bialyx8 = new ImageIcon(newimg24);

        ImageIcon ImI_b88 = new ImageIcon("tekstury/88poleBialy.jpg");
        //dostosuj rozmiare ikony
        Image newimg25 = ImI_b88.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_bialy88 = new ImageIcon(newimg25);

        ImageIcon ImI_b80 = new ImageIcon("tekstury/80poleBialy.jpg");
        //dostosuj rozmiare ikony
        Image newimg26 = ImI_b80.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_bialy80 = new ImageIcon(newimg26);

        ImageIcon ImI_b08 = new ImageIcon("tekstury/08poleBialy.jpg");
        //dostosuj rozmiare ikony
        Image newimg27 = ImI_b08.getImage().getScaledInstance(ROZMIAR_POLA, ROZMIAR_POLA,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon Im_bialy08 = new ImageIcon(newimg27);
    }


}