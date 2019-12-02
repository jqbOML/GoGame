import javax.swing.*;

public class MainKlient {
    public static void main(String[] args) throws Exception {

        Klient klient = new Klient("ip", 19);
        klient.ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        klient.ramka.setVisible(true);
        klient.play();
    }
}
