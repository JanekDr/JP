import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class ObserverServerNIO {
    private final int port;
    private final Map<String, GameRoom> activeGames;

    public ObserverServerNIO(int port, Map<String, GameRoom> activeGames) {
        this.port = port;
        this.activeGames = activeGames;
    }

    public void start() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Serwer NIO dla obserwatorów działa na porcie: " + port);

        ByteBuffer buffer = ByteBuffer.allocate(256);

        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                if (key.isAcceptable()) {
                    handleAccept(serverChannel, selector);
                } else if (key.isReadable()) {
                    handleRead(key, buffer);
                }
                iter.remove();
            }
        }
    }

    private void handleAccept(ServerSocketChannel serverChannel, Selector selector) throws IOException {
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        clientChannel.write(ByteBuffer.wrap("Połączono z serwerem obserwatora.\n".getBytes()));
    }

    private void handleRead(SelectionKey key, ByteBuffer buffer) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        buffer.clear();
        int bytesRead = clientChannel.read(buffer);

        if (bytesRead == -1) {
            clientChannel.close();
            return;
        }

        buffer.flip();
        String playerName = new String(buffer.array(), 0, buffer.limit()).trim();
        GameRoom gameRoom = findGameByPlayerName(playerName);

        if (gameRoom != null) {
            gameRoom.addObserver(clientChannel); // Dodaj obserwatora do gry
            clientChannel.write(ByteBuffer.wrap(("Połączono z serwerem obserwatora.\n").getBytes()));
            clientChannel.write(ByteBuffer.wrap(("Stan gry:\n" + gameRoom.getState()).getBytes()));
        } else {
            clientChannel.write(ByteBuffer.wrap("Nie znaleziono gry dla podanego gracza.\n".getBytes()));
            clientChannel.close();
        }
    }

    private GameRoom findGameByPlayerName(String playerName) {
        for (GameRoom room : activeGames.values()) {
            if (room.getPlayer1().getName().equals(playerName) || room.getPlayer2().getName().equals(playerName)) {
                return room;
            }
        }
        return null;
    }
}
