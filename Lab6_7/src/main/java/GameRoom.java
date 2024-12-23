import java.util.Arrays;
import java.util.UUID;

public class GameRoom {
    private final Player player1;//challenger
    private final Player player2;//opponent
    private final char[][] board = new char[3][3];
    private Player currentPlayer;
    private String gameId;

    public GameRoom(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.gameId = UUID.randomUUID().toString();
        for (int i = 0; i < 3; i++) {
            Arrays.fill(board[i], ' ');
        }
    }

    public synchronized boolean makeMove(Player player, int row, int col) {
        if (player != currentPlayer || board[row][col] != ' ') {
            return false;
        }
        if (currentPlayer == player1) {
            board[row][col] = 'X';
        } else board[row][col] = 'O';
        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else currentPlayer = player1;
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
}