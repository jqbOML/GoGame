import javax.swing.*;

public class MainKlient {
    public static void main(String[] args) throws Exception {

        Klient klient = new Klient("192.168.56.1", 19);
        klient.ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        klient.ramka.setVisible(true);
        klient.play();
    }
}
