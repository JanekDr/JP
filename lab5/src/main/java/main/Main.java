package main;


import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println(args.length);
        if (args.length < 2){
            System.out.println("Nieprawidlowe dane wejsciowe");
        } else {
            try {
                String path = args[0];
                int numberOfThreads = Integer.parseInt(args[1]);
                GameConfig config = FileHandler.readFile(path);

                if (config != null){
                    GameOfLife game = new GameOfLife(config.rows, config.cols, config.initialGrid);
                    game.runGame(config.iterations, numberOfThreads);
                }
            } catch (IOException e) {
                System.out.println("Blad podczas wczytywania pliku: " + e.getMessage());
            }
        }

    }
}

