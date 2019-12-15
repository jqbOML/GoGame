package GraGo;

public enum KomunikatySerwera {
    /**
     * Komunikat wysyłany podczas inicjalizacji połączenia Klienta z serwerem
     * WITAJ <gracz.kolor>
     * gracz.kolor: 1 - czarny, 2 - biały
     */
    WITAJ,

    /**
     * Komunikaty wyświetlane na ekranie Klienta
     * INFO <komunikat>
     */
    INFO,

    /**
     * Komunikat z serwera akceptujący ruch wysłany w komunikacie 'RUCH x y'
     */
    POPRAWNY_RUCH,

    /**
     * Komunikat o wykonanym ruchu przeciwnika, x i y to współrzędne położonego kamienia
     * RUCH_PRZECIWNIKA <x y>
     */
    RUCH_PRZECIWNIKA,

    /**
     * Komunikat o spasowaniu ruchu przez przeciwnika
     */
    PASS,

    /**
     * Komunikat o zakończeniu gry - oboje gracze spasowali
     */
    KONIEC_GRY,

    /**
     * Komunikat o propozycji wyniku gry jednego z graczy
     */
    WYNIK,

    /**
     * Komunikaty o zatwierdzonym (przez drugiego gracza) wyniku zakończonej gry
     */
    ZWYCIESTWO,
    REMIS,
    PORAZKA,

    /**
     * Komunikat o wyjściu przeciwnika z gry
     */
    PRZECIWNIK_WYSZEDL;
}
