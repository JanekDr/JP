import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class GameServerImpl extends UnicastRemoteObject implements GameServerInterface {
    private final Map<String, GameRoom> activeGames = new HashMap<>();
    private final Map<String, Player> players = new HashMap<>();
    private final Map<String, Challenge> challenges = new HashMap<>();

    public GameServerImpl() throws RemoteException {
        super();
    }

    @Override
    public String registerPlayer(Player player) throws RemoteException {
        players.put(player.getToken(), player);
        System.out.println("Zarejestrowano gracza: " + player.getName() + " (Token: " + player.getToken() + ")");
        return player.getToken();
    }

    @Override
    public String getChallengeId(String token) throws RemoteException {
        Player player = players.get(token);
        if (player == null) {
            throw new RemoteException("Nieprawidłowy token!");
        }
        return player.getChallengeId();
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
    public List<Player> getAvailablePlayers(String token) throws RemoteException {
        List<Player> availablePlayers = new ArrayList<>();
        synchronized (players) {
            for (Player player : players.values()) {
                if (!player.getToken().equals(token) && player.getGameId() == null) {
                    availablePlayers.add(player);
                }
            }
        }
        return availablePlayers;
    }

    @Override
    public String challengePlayer(String token, String opponentName) throws RemoteException {
        Player challenger = players.get(token);
        if (challenger == null) {
            System.err.println("Błąd: Gracz wyzywający nie został znaleziony. Token: " + token);
            return "Nieprawidłowy token!";
        }
        synchronized (players) {
            for (Player opponent : players.values()) {
                if (opponentName.equals(opponent.getName())) {
                    if (opponent.getGameId() != null) {
                        return "Gracz " + opponentName + " jest już w grze.";
                    }
                    Challenge challenge = new Challenge(challenger, opponent);
                    String challengeId = challenge.getChallengeId();
                    challenge.setPlayer1Acceptation(true);
                    challenger.setChallengeId(challengeId);
                    opponent.setChallengeId(challengeId);
                    challenges.put(challengeId, challenge);

                    System.out.println("Wyzwano gracza " + opponent.getName());
                    System.out.println("Token wyzwania: " + challengeId);

                    return "Wyzwano gracza: " + opponent.getName();
                }
            }
        }
        return "Nie udalo sie wyzwac gracza: " + challenger.getName() + ".\nGracz znajduje sie juz w grze lub nie istnieje.";
    }

    @Override
    public void acceptChallenge(String challengeId, boolean opponentAcceptation) {
        Challenge challenge = challenges.get(challengeId);
        challenge.setPlayer2Acceptation(opponentAcceptation);
    }

    @Override
    public boolean checkChallengeAcceptation(String challengeId) {
        Challenge challenge = challenges.get(challengeId);
        System.out.println("Otrzymano wyzwanie: " + challengeId);
        if (challenge == null) {
            System.out.println("Nie odnaleziono wyzwania o takim id");
            return false;
        }
        if (challenge.isPlayer1Acceptation() && challenge.isPlayer2Acceptation()) {
//            do zmiany to na dole, dodac osobna metode to tworzenia gier
            Player player1 = challenge.getPlayer1();
            Player player2 = challenge.getPlayer2();
            GameRoom newGame = new GameRoom(player1, player2);
            String gameId = newGame.getGameId();
            activeGames.put(gameId, newGame);
            challenges.remove(challengeId);
            player1.setChallengeId(null);
            player2.setChallengeId(null);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkForGame(String token) {
        if (token == null) {
            System.err.println("Błąd: Token jest null.");
            return false;
        }
        System.out.println("Sprawdzanie zaproszenia dla tokena: " + token);
        Player player = players.get(token);
        if (player == null) {
            System.err.println("Błąd: Gracz nie został znaleziony. Token: " + token);
            return false;
        }
        System.out.println("ChallengeId: "+player.getChallengeId());
        return player.getChallengeId() != "";
    }

    @Override
    public String getOpponentName(String token) throws RemoteException {
        Player player = players.get(token);
        String gameId = player.getGameId();
        GameRoom game = activeGames.get(gameId);
        if (game != null) {
            return game.getPlayer2().getName();
        }
        return "Nieznany";
    }
}
