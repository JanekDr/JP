import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GameServerInterface extends Remote {
    String registerPlayer(Player player) throws RemoteException; // Rejestracja gracza, zwraca token

    String getChallengeId(String token) throws RemoteException;
    String getGameId(String token) throws RemoteException;

    boolean makeMove(String token, int row, int col) throws RemoteException; // Wykonywanie ruchu
    String getGameState(String token) throws RemoteException;       // Pobieranie stanu gry
    String getStatistics(String token) throws RemoteException;      // Pobieranie statystyk gracza
    List<Player> getAvailablePlayers(String token) throws RemoteException;
    String challengePlayer(String token, String opponentName) throws RemoteException; // Wyzwij gracza
    boolean checkForGame(String token) throws RemoteException;
    boolean checkChallengeAcceptation(String challengeId)throws RemoteException;
    String getOpponentName(String token) throws RemoteException;
    void acceptChallenge(String challengeId, boolean opponentAcceptation)throws RemoteException;
    boolean isYourTurn(String token)throws RemoteException;
    String checkEndGame(String token)throws RemoteException;
    void reset(String token)throws RemoteException;
}
