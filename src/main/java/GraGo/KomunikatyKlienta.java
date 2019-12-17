package GraGo;

public enum KomunikatyKlienta {
    /**
     * Komunikat dla serwera, gracz chce grać z botem
     */
    BOT,

    /**
     * Komunikat dla serwera, gracz chce grać z drugim użytkownikiem
     */
    GRACZ,

    /**
     * Komunikat o wykonanym ruchu, x i y to współrzędne położonego kamienia
     * RUCH <x y>
     * Serwer po otrzymaniu komunikatu o ruchu weryfikuje jego poprawność i wysyła komunikat zwrotny POPRAWNY_RUCH lub INFO <komunikat>
     */
    RUCH,

    /**
     * Komunikat o spasowaniu ruchu przez gracza
     * Serwer po otrzymaniu komunikatu, przekazuje go przeciwnikowi i czeka na jego ruch
     */
    PASS,

    /**
     * Komunikat o propozycji wyniku gry gracza, który spasował jako drugi
     * WYNIK <wynik[0] wynik[1]>, gdzie wynik[0] to wynik gracza, który wysłał komunikat
     * Serwer po otrzymaniu komunikatu proponuje ten wynik przeciwnikowi lub automatycznie akceptuje jeśli gra z botem
     */
    WYNIK,

    /**
     * Komunikat wysyłany, gdy gracz przyjmuję propozycję wyniku przeciwnika
     * Serwer po otrzymaniu tego komunikatu interpretuje wyniki i wysyła odpowiedni komunikat o zwycięstwie/remisie/porażce do graczy
     */
    ZAAKCEPTUJ_WYNIK,

    /**
     * Komunikat wysyłany, gdy gracz nie zgadza się z propozycją wyniku przeciwnika
     * Serwer po otrzymaniu tego komunikatu prosi ponownie przeciwnika o propozycję wyniku i wysyła go drugiemu graczowi
     */
    ODRZUC_WYNIK,

    /**
     * Komunikat o wyjściu z gry
     */
    WYJSCIE;
}
