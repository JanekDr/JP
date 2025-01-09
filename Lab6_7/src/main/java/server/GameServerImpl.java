package server;

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
            throw new RemoteException("Nieprawidlowy token!");
        }
        return player.getChallengeId();
    }

    @Override
    public String getGameId(String token) throws RemoteException {
        Player player = players.get(token);
        if (player == null) {
            throw new RemoteException("Nieprawidlowy token!");
        }
        return player.getGameId();
    }

    @Override
    public boolean makeMove(String token, int row, int col) throws RemoteException {
        Player player = players.get(token);
        if (player == null) {
            throw new RemoteException("Nieprawidlowy token!");
        }
        String gameId = player.getGameId();
        GameRoom gameRoom = activeGames.get(gameId);
        if (gameRoom == null) {
            throw new RemoteException("Nie znaleziono gry!");
        }
        boolean moveResult = gameRoom.makeMove(player, row, col);
        if (moveResult) {
            checkWin(gameId);
            gameRoom.notifyObservers(gameRoom.getState());
        }
        return moveResult;
    }

    @Override
    public String getGameState(String token) throws RemoteException {
        Player player = players.get(token);
        if (player == null) {
            throw new RemoteException("Nieprawidlowy token!");
        }
        String gameId = player.getGameId();
        GameRoom gameRoom = activeGames.get(gameId);
        if (gameRoom == null) {
            return "Gra nie rozpoczeta.";
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
            System.err.println("Blad: Gracz wyzywajacy nie zostal znaleziony. Token: " + token);
            return "Nieprawidlowy token!";
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

                    return "Wyzwano gracza: " + opponent.getName();
                }
            }
        }
        return "Nie udalo sie wyzwac gracza: " + opponentName + ".\nGracz znajduje sie juz w grze lub nie istnieje.";
    }

    @Override
    public void acceptChallenge(String challengeId, boolean opponentAcceptation) throws RemoteException {
        Challenge challenge = challenges.get(challengeId);
        if (challenge == null) {
            throw new RemoteException("Nie odnaleziono wyzwania o takim ID!");
        }
        challenge.setPlayer2Acceptation(opponentAcceptation);
        if (!opponentAcceptation) {
            System.out.println("Wyzwanie zostalo odrzucone przez przeciwnika.");
            challenges.remove(challengeId);
        }

        if (challenge.isPlayer1Acceptation() && challenge.isPlayer2Acceptation()) {
            Player player1 = challenge.getPlayer1();
            Player player2 = challenge.getPlayer2();

            if (player1.getGameId() == null && player2.getGameId() == null) {
                GameRoom newGame = new GameRoom(player1, player2);
                String gameId = newGame.getGameId();
                activeGames.put(gameId, newGame);

                player1.setGameId(gameId);
                player2.setGameId(gameId);

                System.out.println("Gra zostala rozpoczeta miedzy: " + player1.getName() + " i " + player2.getName());

            } else {
                System.out.println("Gra juz istnieje dla tych graczy.");
            }
        }
    }


    @Override
    public boolean checkChallengeAcceptation(String challengeId) throws RemoteException {
        Challenge challenge = challenges.get(challengeId);
        System.out.println("Otrzymano wyzwanie: " + challengeId);

        if (challenge == null) {
            System.out.println("Nie odnaleziono wyzwania o takim ID.");
            return false;
        }

        if (challenge.isPlayer1Acceptation() && challenge.isPlayer2Acceptation()) {
            Player player1 = challenge.getPlayer1();
            Player player2 = challenge.getPlayer2();

            if (player1.getGameId() == null && player2.getGameId() == null) {
                GameRoom newGame = new GameRoom(player1, player2);
                String gameId = newGame.getGameId();
                activeGames.put(gameId, newGame);

                player1.setGameId(gameId);
                player2.setGameId(gameId);
            }
            challenges.remove(challengeId);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean checkForGame(String token) {
        if (token == null) {
            System.err.println("Blad: Token jest null.");
            return false;
        }
        System.out.println("Sprawdzanie zaproszenia dla tokena: " + token);
        Player player = players.get(token);
        if (player == null) {
            System.err.println("Blad: Gracz nie zostal znaleziony. Token: " + token);
            return false;
        }
        System.out.println("ChallengeId: "+player.getChallengeId());
        return player.getChallengeId() != "";
    }

    @Override
    public String getOpponentName(String token) throws RemoteException {
        Player player = players.get(token);
        String challengeId = player.getChallengeId();
        Challenge challenge = challenges.get(challengeId);
        if (challenge != null) {
            return challenge.getOpponent(player).getName();
        }
        String gameId = player.getGameId();
        if (gameId != null){
            GameRoom gameRoom = activeGames.get(gameId);
            return gameRoom.getOpponent(player).getName();
        }
        return "Nieznany";
    }

    private void declareWinner(String gameId, Player winner) {
        GameRoom gameRoom = activeGames.get(gameId);
        if (gameRoom != null) {
            Player loser = gameRoom.getOpponent(winner);
            winner.setWins(winner.getWins() + 1);
            loser.setLosses(loser.getLosses() + 1);
            System.out.println("Zwyciezca: " + winner.getName());
        }
    }

    private void declareDraw(String gameId) {
        GameRoom gameRoom = activeGames.get(gameId);
        if (gameRoom != null) {
            Player player1 = gameRoom.getPlayer2();
            Player player2 = gameRoom.getOpponent(player1);
            player1.setDraws(player1.getDraws() + 1);
            player2.setDraws(player2.getDraws() + 1);
            System.out.println("Gra zakonczona remisem.");
        }
    }
    private void checkWin(String gameId) throws RemoteException {
        GameRoom gameRoom = activeGames.get(gameId);
        if (gameRoom == null) {
            throw new RemoteException("Nie znaleziono gry!");
        }
        Player winner = gameRoom.checkWinner();
        if (winner != null) {
            declareWinner(gameId, winner);
        } else if (gameRoom.isDraw()) {
            declareDraw(gameId);
        }
    }

    @Override
    public boolean isYourTurn(String token) throws RemoteException{
        Player player = players.get(token);
        if (player == null) {
            throw new RemoteException("Nieprawidlowy token!");
        }
        String gameId = player.getGameId();
        GameRoom gameRoom = activeGames.get(gameId);
        Player currentPlayer = gameRoom.getPlayerTurn();
        return player==currentPlayer;
    }

    @Override
    public String checkEndGame(String token) throws RemoteException {
        Player player = players.get(token);
        if (player == null) {
            throw new RemoteException("Nieprawidlowy token!");
        }

        String gameId = player.getGameId();
        if (gameId == null) {
            throw new RemoteException("Gracz nie jest przypisany do zadnej gry!");
        }

        GameRoom gameRoom = activeGames.get(gameId);
        if (gameRoom == null) {
            throw new RemoteException("Nie znaleziono aktywnej gry o ID: " + gameId);
        }

        Player winner = gameRoom.checkWinner();
        if (winner != null) {
            return winner.getToken();
        } else if (gameRoom.isDraw()) {
            return "draw";
        } else {
            return null;
        }
    }

    @Override
    public void reset(String token)throws RemoteException{
        Player player = players.get(token);
        if (player == null) {
            throw new RemoteException("Nieprawidlowy token!");
        }
        player.reset();
    }

    public Map<String, GameRoom> getActiveGames() {
        return activeGames;
    }

}