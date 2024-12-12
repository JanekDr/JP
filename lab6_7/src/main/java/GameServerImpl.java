import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class GameServerImpl extends UnicastRemoteObject implements GameServerInterface {
    private final Map<String, GameRoom> activeGames = new HashMap<>();
    private final Map<String, Player> players = new HashMap<>();
    private final List<String> waitingPlayers = new LinkedList<>();

    public GameServerImpl() throws RemoteException {
        super();
    }

    @Override
    public String registerPlayer(String playerName) throws RemoteException {
        String token = UUID.randomUUID().toString();
        players.put(token, new Player(playerName));
        System.out.println("Zarejestrowano gracza: " + playerName + " (Token: " + token + ")");
        return token;
    }

    @Override
    public String joinGame(String token) throws RemoteException {
        synchronized (waitingPlayers) {
            waitingPlayers.add(token);
            if (waitingPlayers.size() >= 2) {
                String player1 = waitingPlayers.remove(0);
                String player2 = waitingPlayers.remove(0);
                String gameId = UUID.randomUUID().toString();
                activeGames.put(gameId, new GameRoom(players.get(player1), players.get(player2)));
                players.get(player1).setGameId(gameId);
                players.get(player2).setGameId(gameId);
                System.out.println("Stworzono nową grę: " + gameId);
                return "Gra rozpoczęta! ID gry: " + gameId;
            }
        }
        return "Czekam na drugiego gracza...";
    }

    @Override
    public boolean makeMove(String token, int row, int col) throws RemoteException {
        Player player = players.get(token);
        if (player == null) {
            throw new RemoteException("Nieprawidłowy token!");
        }
        String gameId = player.getGameId();
        GameRoom gameRoom = activeGames.get(gameId);
        if (gameRoom == null) {
            throw new RemoteException("Nie znaleziono gry!");
        }
        return gameRoom.makeMove(player, row, col);
    }

    @Override
    public String getGameState(String token) throws RemoteException {
        Player player = players.get(token);
        if (player == null) {
            throw new RemoteException("Nieprawidłowy token!");
        }
        String gameId = player.getGameId();
        GameRoom gameRoom = activeGames.get(gameId);
        if (gameRoom == null) {
            return "Gra nie rozpoczęta.";
        }
        return gameRoom.getState();
    }

    @Override
    public String getStatistics(String token) throws RemoteException {
        Player player = players.get(token);
        if (player == null) {
            throw new RemoteException("Nieprawidłowy token!");
        }
        return player.toString();
    }
}
