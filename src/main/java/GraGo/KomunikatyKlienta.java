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
     * Komunikat o wyjściu z gry
     */
    WYJSCIE;
}
