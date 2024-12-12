import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameServerInterface extends Remote {
    String registerPlayer(String playerName) throws RemoteException; // Rejestracja gracza, zwraca token
    String joinGame(String token) throws RemoteException;           // Dołączanie do gry
    boolean makeMove(String token, int row, int col) throws RemoteException; // Wykonywanie ruchu
    String getGameState(String token) throws RemoteException;       // Pobieranie stanu gry
    String getStatistics(String token) throws RemoteException;      // Pobieranie statystyk gracza
}
