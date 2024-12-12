import java.rmi.Naming;

public class Client {
    public static void main(String[] args) {
        try {
            GameServerInterface server = (GameServerInterface) Naming.lookup("rmi://localhost:1099/GameServer");
            String token = server.registerPlayer("Gracz1");
            System.out.println("zarejestrowano gracza z tokenem " + token);
//            System.out.println(server.joinGame(token));
//            System.out.println("Stan gry:\n" + server.getGameState(token));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
