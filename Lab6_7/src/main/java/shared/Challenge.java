package shared;


import java.util.UUID;

public class Challenge {
    Player player1;
    Player player2;
    boolean player1Acceptation;
    boolean player2Acceptation;
    String challengeId;
    public Challenge(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
        this.challengeId = UUID.randomUUID().toString();
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getOpponent(Player player){
        return player==player1 ? player2:player1;
    }

    public boolean isPlayer1Acceptation() {
        return player1Acceptation;
    }

    public void setPlayer1Acceptation(boolean player1Acceptation) {
        this.player1Acceptation = player1Acceptation;
    }

    public boolean isPlayer2Acceptation() {
        return player2Acceptation;
    }

    public void setPlayer2Acceptation(boolean player2Acceptation) {
        this.player2Acceptation = player2Acceptation;
    }

    public String getChallengeId() {
        return challengeId;
    }
}
