import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
class Game {

    // Board cells numbered 0-361, top to bottom, left to right; null if empty
    private Player[][] board = new Player[19][19];
    Player currentPlayer;
    /*public static int[][] pozycje_kamienii;
    pozycje_kamienii =
    for (int a = 0; a < (rozmiarBoku_planszy+1)*(rozmiarBoku_planszy+1); a++) {
        //for (int b = 0; b < rozmiarBoku_planszy; b++) {
        pozycje_kamienii[a] = 0;
    }*/

    public synchronized void move(int x, int y, Player player) {
        if (player != currentPlayer) {
            throw new IllegalStateException("Nie Twój ruch");
        } else if (player.opponent == null) {
            throw new IllegalStateException("Nie masz jeszcze przeciwnika");
        } else if (board[x][y] != null) {
            throw new IllegalStateException("Pole jest już zajęte");
        }
        board[x][y] = currentPlayer;
        currentPlayer = currentPlayer.opponent;
    }

    /**
     * A Player is identified by a character mark which is either 'X' or 'O'.
     * For communication with the client the player has a socket and associated
     * Scanner and PrintWriter.
     */
    class Player implements Runnable {
        char mark;
        Player opponent;
        Socket socket;
        Scanner input;
        PrintWriter output;

        public Player(Socket socket, char mark) {
            this.socket = socket;
            this.mark = mark;
        }

        @Override
        public void run() {
            try {
                setup();
                processCommands();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (opponent != null && opponent.output != null) {
                    opponent.output.println("OTHER_PLAYER_LEFT");
                }
                try {socket.close();} catch (IOException e) {}
            }
        }

        private void setup() throws IOException {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("WELCOME " + mark);
            if (mark == 'X') {
                currentPlayer = this;
                output.println("MESSAGE Waiting for opponent to connect");
            } else {
                opponent = currentPlayer;
                opponent.opponent = this;
                opponent.output.println("MESSAGE Your move");
                this.output.println("MESSAGE Runda przeciwnika, proszę czekać");
            }
        }

        private void processCommands() {
            while (input.hasNext()) {
                String command = input.next();
                if (command.startsWith("QUIT")) {
                    return;
                } else if (command.startsWith("MOVE")) {
                    processMoveCommand(Integer.parseInt(input.next()), Integer.parseInt(input.next()));
                }
            }
        }

        private void processMoveCommand(int locX, int locY) {
            try {
                move(locX, locY, this);
                output.println("POPRAWNY_RUCH");
                opponent.output.println("OPPONENT_MOVED " + locX + " " + locY);
                if (false) {
                    output.println("VICTORY");
                    opponent.output.println("DEFEAT");
                }
            } catch (IllegalStateException e) {
                output.println("MESSAGE " + e.getMessage());
            }
        }
    }
}

