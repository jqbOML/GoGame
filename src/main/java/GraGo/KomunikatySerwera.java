package GraGo;

public enum KomunikatySerwera {
    /**
     * Komunikat wysyłany podczas inicjalizacji połączenia Klienta z serwerem, serwer informuje gracza o kolorze
     * WITAJ <" "gracz.kolor>
     * <" "gracz.kolor>: (1 (czarny), 2 (biały));
     */
    WITAJ,

    /**
     * Komunikaty wyświetlane na ekranie Klienta, np. o niedozwolonym ruchu
     * INFO <" "komunikat>
     * <" "komunikat>: (" Grasz z botem", " Oczekuje na przeciwnika", " Twoja kolej", " Runda przeciwnika, proszę czekać"
     *                 " Nie Twój ruch!", " Nie masz jeszcze przeciwnika", " Pole jest już zajęte!", "Niedozwolony ruch samobojczy!",
     *                 " Niedozwolony ruch KO!");
     */
    INFO,

    /**
     * Komunikat z serwera akceptujący ruch wysłany w komunikacie 'RUCH x y'
     * Po zatwierdzeniu ruchu wczytywana jest grafika z kamieniem u klienta i tworzony obiekt Kamień w serwerze
     */
    POPRAWNY_RUCH,

    /**
     * Komunikat o wykonanym ruchu przeciwnika, x i y to współrzędne położonego kamienia
     * RUCH_PRZECIWNIKA <x y>
     * Po otrzymaniu komunikatu o ruchu przeciwnika wczytywana jest grafika z kamieniem u klienta i tworzony obiekt Kamień w serwerze
     */
    RUCH_PRZECIWNIKA,

    /**
     * Komunikat przekazujący współrzędne uduszonego kamienia (kamienia do uduszenia)
     * USUN <x y>
     * Po otrzymaniu komunikatu usuń, usuwana jest grafika z danym kamieniem  u klienta i usuwany obiekt Kamień w serwerze
     */
    USUN,

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
