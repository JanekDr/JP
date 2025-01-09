package client;

import server.GameServerInterface;
import shared.Player;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class Client {
    static GameServerInterface server;
    static String token;
    static String challengeId;

    public static void main(String[] args) {
        try {
            server = (GameServerInterface) Naming.lookup("rmi://localhost:1099/GameServer");

            Scanner scanner = new Scanner(System.in);

            System.out.println("Wprowadz nazwe gracza: ");
            String playerName = scanner.nextLine();
            token = server.registerPlayer(new Player(playerName));

            System.out.println("zarejestrowano gracza z tokenem: " + token);
            Menu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void Menu() throws RemoteException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("\n--- Menu ---");
            System.out.println("1. Wyświetl twoje statystyki");
            System.out.println("2. Wejdz do lobby");
            System.out.println("3. Wejdz w tryb obserwatora");
            System.out.println("4. Wyjdź");

            System.out.print("Wybierz opcje: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Twoje statystyki:");
                    System.out.println(server.getStatistics(token));
                    break;
                case 2:
                    Lobby();
                    break;
                case 3:
                    observerMode();
                    break;
                case 4:
                    System.out.println("Do widzenia!");
                    return;
                default:
                    System.out.println("Nieprawidłowa opcja.");
                    break;
            }
        }
    }
    public static void Lobby() throws RemoteException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("\n--- Lobby ---");
            System.out.println("1. Wyświetl dostępnych graczy");
            System.out.println("2. Wyzwij gracza");
            System.out.println("3. Pokoj oczekujacych na gre");
            System.out.println("4. Wroc do menu");

            System.out.print("Wybierz opcje: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    List<Player> availablePlayers = server.getAvailablePlayers(token);
                    System.out.println("Dostepni gracze:");
                    for (Player p : availablePlayers) {
                        System.out.println(p.getName()+" - " + p);
                    }
                    break;

                case 2:
                    System.out.print("Podaj nazwe przeciwnika: ");
                    String opponentName = scanner.nextLine();
                    String challengePlayerOutput = server.challengePlayer(token, opponentName);
                    System.out.println(challengePlayerOutput);

                    if (challengePlayerOutput.startsWith("Nie udalo sie"))break;

                    challengeId = server.getChallengeId(token);

                    while (true){
                        if(server.checkChallengeAcceptation(challengeId)){
                            System.out.println("Nowa gra rozpoczęta! Twój przeciwnik to: " + server.getOpponentName(token));
                            processGame();
                            break;
                        } else {
                            System.out.println("Oczekiwanie na przeciwnika...");
                        }
                        Thread.sleep(3000);
                    }
                    break;

                case 3:
                    while (true){
                        if(server.checkForGame(token)){
                            challengeId = server.getChallengeId(token);
                            System.out.println("Zostales wyzwany na pojedynek przez : " + server.getOpponentName(token));
                            System.out.println("token wyzwania: "+challengeId);

                            boolean valid = false;
                            boolean acceptation;

                            while(!valid){
                                System.out.print("Czy akceptujesz to wyzwanie? (y/n): ");
                                String input = scanner.nextLine();
                                if (input.equals("y")){
                                    acceptation = true;
                                    valid = true;
                                    server.acceptChallenge(challengeId, acceptation);

                                    String gameId = server.getGameId(token);
                                    System.out.println("Przypisane gameId: " + gameId);

                                    processGame();
                                }
                                else if (input.equals("n")) {
                                    acceptation = false;
                                    valid=true;
                                    server.acceptChallenge(challengeId, acceptation);
                                } else{
                                    System.out.println("Niepoprawne dane.");
                                }
                            }
                        } else {
                            System.out.println("Oczekiwanie na przeciwnika...");
                        }
                        Thread.sleep(3000);
                    }
                case 4:
                    Menu();
                    return;
                default:
                    System.out.println("Nieprawidłowa opcja.");
                    break;
            }
        }
    }

    public static void processGame() throws RemoteException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        while (true){
            String state = server.getGameState(token);
            System.out.println("Stan gry:");
            System.out.println(state);

            String isGameEnd = server.checkEndGame(token);
            if (isGameEnd!=null) {
                if (isGameEnd.equals(token)){
                    System.out.println("Wygrales!");
                } else if (isGameEnd.equals("draw")){
                    System.out.println("Remis!");
                } else {
                    System.out.println("Przegrales!");
                }
                server.reset(token);
                Lobby();
            }

            if(server.isYourTurn(token)){
                System.out.print("Podaj wiersz (0-2): ");
                int row = scanner.nextInt();
                System.out.print("Podaj kolumnę (0-2): ");
                int col = scanner.nextInt();
                boolean moveResult = server.makeMove(token, row, col);
                if (moveResult) {
                    System.out.println("Ruch wykonany.");
                } else {
                    System.out.println("Nieprawidłowy ruch. Spróbuj ponownie.");
                }
            } else {
                System.out.println("Czekasz na ruch przeciwnika.");
            }
            Thread.sleep(3000);
        }
    }


    public static void observerMode() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Podaj nazwę gracza do obserwacji: ");
            String playerName = scanner.nextLine();

            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 5000));
            System.out.println("Połączono z graczem: "+playerName+" ("+socketChannel.getLocalAddress().toString().substring(1)+").");
            ByteBuffer buffer = ByteBuffer.allocate(256);

            socketChannel.write(ByteBuffer.wrap((playerName + "\n").getBytes()));

            while (true) {
                buffer.clear();
                int bytesRead = socketChannel.read(buffer);
                if (bytesRead == -1) break;

                buffer.flip();
                String message = new String(buffer.array(), 0, buffer.limit()).trim();
                if (message.length()!=29)System.out.println(message);

                if (message.contains("exit")) {
                    System.out.println("Gra zakończona. Powrót do menu.");
                    Menu();
                    socketChannel.close();
                    return;
                }
                Thread.sleep(2000);
            }

            socketChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}