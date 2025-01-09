package server;

import shared.Player;

import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.UUID;

public class GameRoom {
    private final Player player1; // challenger
    private final Player player2; // opponent
    private final char[][] board = new char[3][3];
    private Player playerTurn;
    private String gameId;
    private final ObserverManager observerManager = new ObserverManager();

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

        board[row][col] = (playerTurn == player1) ? 'X' : 'O';
        playerTurn = (playerTurn == player1) ? player2 : player1;

        notifyObservers("Stan gry:\n" + getState());

        Player winner = checkWinner();
        if (winner != null) {
            notifyGameEnd("Gra zakonczona! Zwyciezca: " + winner.getName());
            return true;
        } else if (isDraw()) {
            notifyGameEnd("Gra zakonczona remisem!");
            return true;
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
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return getWinner(board[i][0]);
            }
            if (board[0][i] != ' ' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return getWinner(board[0][i]);
            }
        }
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return getWinner(board[0][0]);
        }
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return getWinner(board[0][2]);
        }
        return null;
    }

    public boolean isDraw() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private Player getWinner(char symbol) {
        return (symbol == 'X') ? player1 : player2;
    }

    public synchronized void addObserver(SocketChannel observer) {
        observerManager.addObserver(observer);
    }

    public void notifyObservers(String message) {
        observerManager.notifyObservers(message);
    }

    private void notifyGameEnd(String message) {
        observerManager.notifyObservers(message);
        observerManager.notifyObservers("exit");
        observerManager.clearObservers();
    }
}
