import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            GameServerInterface server = new GameServerImpl();
            Naming.rebind("rmi://localhost:1099/GameServer", server);
            System.out.println("Serwer RMI uruchomiony.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
