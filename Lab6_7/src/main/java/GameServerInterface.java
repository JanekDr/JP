import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GameServerInterface extends Remote {
    boolean registerPlayer(Player player) throws RemoteException; // Rejestracja gracza, zwraca token
    boolean makeMove(String token, int row, int col) throws RemoteException; // Wykonywanie ruchu
    String getGameState(String token) throws RemoteException;       // Pobieranie stanu gry
    String getStatistics(String token) throws RemoteException;      // Pobieranie statystyk gracza
    List<Player> getAvailablePlayers(String token) throws RemoteException;
    String challengePlayer(String token, String opponentName) throws RemoteException; // Wyzwij gracza
    boolean checkForGame(String token) throws RemoteException;
    String getOpponentName(Player player) throws RemoteException;
}
