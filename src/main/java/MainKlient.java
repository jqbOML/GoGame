import javax.swing.*;

public class MainKlient {
    public static void main(String[] args) throws Exception {

        Klient klient = new Klient("127.0.0.1", 19);
        klient.ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        klient.ramka.setVisible(true);
        klient.graj();
    }
}
