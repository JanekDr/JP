package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObserverManager {
    private final List<SocketChannel> observers = new ArrayList<>();

    public synchronized void addObserver(SocketChannel observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public synchronized void notifyObservers(String message) {
        Iterator<SocketChannel> iterator = observers.iterator();
        while (iterator.hasNext()) {
            SocketChannel observer = iterator.next();
            try {
                observer.write(ByteBuffer.wrap((message + "\n").getBytes()));
            } catch (IOException e) {
                iterator.remove();
            }
        }
    }

    public synchronized void clearObservers() {
        observers.clear();
    }
}
