import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;


public class Server {
    public static void main(String[] args) {
        try {
            // Uruchomienie serwera RMI
            LocateRegistry.createRegistry(1099);
            GameServerImpl server = new GameServerImpl();
            Naming.rebind("rmi://localhost:1099/GameServer", server);
            System.out.println("Serwer RMI uruchomiony.");

            // Uruchomienie serwera NIO dla obserwatorów
            ObserverServerNIO observerServer = new ObserverServerNIO(5000, server.getActiveGames());
            new Thread(() -> {
                try {
                    observerServer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            System.out.println("Serwer NIO dla obserwatorów uruchomiony.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
