import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;

public class GameRoom {
    private final Player player1; // challenger
    private final Player player2; // opponent
    private final char[][] board = new char[3][3];
    private Player playerTurn;
    private String gameId;
    private final List<SocketChannel> observers = new ArrayList<>();

    public GameRoom(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.playerTurn = player1;
        this.gameId = UUID.randomUUID().toString();
        for (int i = 0; i < 3; i++) {
            Arrays.fill(board[i], ' ');
        }
    }

    public synchronized boolean makeMove(Player player, int row, int col) {
        if (player != playerTurn || board[row][col] != ' ') {
            return false;
        }
        if (playerTurn == player1) {
            board[row][col] = 'X';
        } else {
            board[row][col] = 'O';
        }

        // Zmiana tury
        playerTurn = (playerTurn == player1) ? player2 : player1;

        // Powiadomienie obserwatorów o stanie gry
        notifyObservers("Stan gry:\n" + getState());

        // Sprawdzenie, czy gra się zakończyła
        Player winner = checkWinner();
        if (winner != null) {
            notifyGameEnd("Gra zakończona! Zwycięzca: " + winner.getName());
            return true; // Kończymy
        } else if (isDraw()) {
            notifyGameEnd("Gra zakończona remisem!");
            return true; // Kończymy
        }
        return true;
    }


    public synchronized String getState() {
        StringBuilder sb = new StringBuilder();
        for (char[] row : board) {
            sb.append(Arrays.toString(row)).append("\n");
        }
        return sb.toString();
    }

    public String getGameId() {
        return gameId;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getOpponent(Player player) {
        return player == player1 ? player2 : player1;
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }

    public Player checkWinner() {
        // Sprawdzenie wierszy i kolumn
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return getWinner(board[i][0]);
            }
            if (board[0][i] != ' ' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return getWinner(board[0][i]);
            }
        }
        // Przekątne
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return getWinner(board[0][0]);
        }
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return getWinner(board[0][2]);
        }
        return null; // Brak zwycięzcy
    }

    public boolean isDraw() {
        for (char[] chars : board) {
            for (char aChar : chars) {
                if (aChar == ' ') {
                    return false; // Gra trwa
                }
            }
        }
        return true; // Remis
    }

    private Player getWinner(char symbol) {
        return symbol == 'X' ? player1 : player2;
    }

    // Dodanie obserwatora
    public synchronized void addObserver(SocketChannel observer) {
        observers.add(observer);
    }

    // Powiadomienie obserwatorów
    public synchronized void notifyObservers(String message) {
        Iterator<SocketChannel> iterator = observers.iterator();
        while (iterator.hasNext()) {
            SocketChannel observer = iterator.next();
            try {
                observer.write(ByteBuffer.wrap((message + "\n").getBytes()));
            } catch (IOException e) {
                iterator.remove(); // Usuń obserwatora, jeśli połączenie jest zerwane
            }
        }
    }

    // Powiadomienie obserwatorów o zakończeniu gry
    public synchronized void notifyGameEnd(String message) {
        notifyObservers(message);
        notifyObservers("exit");
        observers.clear();
    }
}
