package GraGo.Klient;

import javax.swing.*;

abstract class AbstractStart extends JFrame {
    JFrame oknoStartowe;

    /**
     * Graj z botem, wyślij do serwera KomunikatKlienta.BOT
     */
    JButton botButton;

    /**
     * Graj z innym graczem, wyślij do serwera KomunikatKlienta.GRACZ
     */
    JButton przeciwnikButton;
}
