import java.util.Arrays;
import java.util.UUID;

public class GameRoom {
    private final Player player1; // challenger
    private final Player player2; // opponent
    private final char[][] board = new char[3][3];
    private Player playerTurn;
    private String gameId;

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
        if (playerTurn == player1) {
            playerTurn = player2;
        } else {
            playerTurn = player1;
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

    public void setGameId(String gameId) {
        this.gameId = gameId;
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
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {//kolumny
                return getWinner(board[i][0]);
            }
            if (board[0][i] != ' ' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {//wiersze
                return getWinner(board[0][i]);
            }
        }
        //przekatne
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return getWinner(board[0][0]);
        }
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return getWinner(board[0][2]);
        }
        // Brak zwycięzcy
        return null;
    }

    public boolean isDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false; // Gra trwa
                }
            }
        }
        return true; // Remis
    }

    private Player getWinner(char symbol) {
        return symbol == 'X' ? player1 : player2;
    }
}
