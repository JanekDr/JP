package server;

import java.io.Serializable;
import java.util.UUID;

public class Player implements Serializable {
    private String name;
    private String token;
    private int losses, wins, draws;
    private String gameId;
    private String challengeId = "";

    public Player(String name) {
        this.name = name;
        this.token = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getToken(){
        return token;
    }

    public void reset(){
        this.gameId=null;
        this.challengeId="";
    }

    @Override
    public String toString() {
        return "Wygrane: " + wins + ", Porażki: " + losses + ", Remisy: " + draws;
    }
}