import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class GameServerImpl extends UnicastRemoteObject implements GameServerInterface {
    private Map<String, GameRoom> activeGames = new HashMap<>();
    private Map<String,Player> players = new HashMap<>();

    public GameServerImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean registerPlayer(Player player) throws RemoteException {
        players.put(player.token, player);
        System.out.println("Zarejestrowano gracza: " + player.name + " (Token: " + player.token + ")");
        return true;
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
            throw new RemoteException("Nieprawidlowy token!");
        }
        return player.toString();
    }

    @Override
    public List<Player> getAvailablePlayers(String token) throws RemoteException{
        List<Player> availablePlayers = new ArrayList<>();
        synchronized (players){
            for (Player player:players.values()) {
                if (!player.getToken().equals(token) && player.getGameId() == null) {
                    availablePlayers.add(player);
                }
            }
        }
        return availablePlayers;
    }

    @Override
    public String challengePlayer(String token, String opponentName) throws RemoteException{
        Player challenger = players.get(token);
        if (challenger == null) {
            System.err.println("Błąd: Gracz wyzywający nie został znaleziony. Token: " + token);
            return "Nieprawidłowy token!";
        }
        synchronized (getAvailablePlayers(token)) {
            List<Player> availalblePlayers = getAvailablePlayers(token);
            for (Player p : availalblePlayers) {
                if (opponentName.equals(p.getName())) {
                    System.out.println("nazwy sie zgadzaja");
                    if (p.getGameId() != null) {
                        return "Gracz " + opponentName + " jest już w grze.";
                    }
                    Player opponent = p;
                    GameRoom newGame = new GameRoom(challenger, opponent);
                    String gameId = newGame.getGameId();
                    activeGames.put(gameId, newGame);
                    challenger.setGameId(gameId);
                    opponent.setGameId(gameId);
                    opponent.setGamePending(true);
                    System.out.println("zarejestrowano gre "+gameId);
                    return "Wyzwano gracza: " + challenger.getName();
                }
            }
        }
        return "Nie udalo sie wyzwac gracza: "+challenger.getName()+".\nGracz znajduje sie juz w grze lub nie istnieje.";
    }

    @Override
    public boolean checkForGame(String token){ //sprawdza czy ktos inny wyzwal gracza
        if (token == null) {
            System.err.println("Błąd: Token jest null.");
            return false;
        }
        System.out.println("Sprawdzanie gry dla tokena: " + token);
        Player player = players.get(token);
        if (player == null) {
            System.err.println("Błąd: Gracz nie został znaleziony. Token: " + token);
            return false;
        }
        if (Boolean.TRUE.equals(player.getGamePending())) {
            player.setGamePending(false);
            return true;
        } else {
            return false;
        }
    }

    public String getOpponentName(Player player){
        String gameId = player.getGameId();
        GameRoom game = activeGames.get(gameId);
        if (game != null){
            return game.getPlayer2().getName();
        }
        return "Nieznany";
    }
}