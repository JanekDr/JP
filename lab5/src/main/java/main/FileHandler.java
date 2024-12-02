package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHandler {
    public static GameConfig readFile(String fileName) throws IOException {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            int rows = scanner.nextInt();
            int cols = scanner.nextInt();
            int iterations = scanner.nextInt();
            List<String> errors = new ArrayList<>();

            if (rows <= 0 || cols <= 0 || iterations <= 0) {
                errors.add("Wymiary planszy oraz liczba iteracji musza byc dodatnie.");
            }

            int liveCellsCount = scanner.nextInt();
            if (liveCellsCount < 0) {
                errors.add("Liczba zywych komorek nie moze byc ujemna.");
            }
            if(!errors.isEmpty()){
                System.out.println("Bledy w pliku konfiguracyjnym:\n" + String.join("\n", errors));
                return null;
            }

            boolean[][] initialGrid = new boolean[rows][cols];

            for (int i = 0; i < liveCellsCount; i++) {
                if (!scanner.hasNextInt()) {
                    errors.add("Brakuje wspolrzednych dla komorki numer " + (i + 1));
                    break;
                }
                int x = scanner.nextInt();

                if (!scanner.hasNextInt()) {
                    errors.add("Brakuje wspolrzednych Y dla komorki numer " + (i + 1));
                    break;
                }
                int y = scanner.nextInt();

                if (x < 0 || x >= rows || y < 0 || y >= cols) {
                    errors.add("Nieprawidlowe wspolrzedne komorki: (" + x + ", " + y + ").");
                } else {
                    initialGrid[x][y] = true;
                }
            }

            if (!errors.isEmpty()) {
                System.out.println("Bledy w pliku konfiguracyjnym:\n" + String.join("\n", errors));
                return null;
            }

            return new GameConfig(rows, cols, iterations, initialGrid);
        }
    }
}

