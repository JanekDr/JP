package lab2;

import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java -jar <skrypt.jar> <sciezka do pliku wejsciowego>");
            System.exit(1);
        }
        String filePath = args[0];  // Pobranie ścieżki do pliku z argumentu

        try {
            FileHandler file = new FileHandler(filePath);
            List<Project> projects = new ArrayList<>();
            List<Staff> staff = new ArrayList<>();
            file.parseFile(projects, staff);
            GreedyAlgorithm algorithm = new GreedyAlgorithm();
            algorithm.getBestSolution(projects,staff);

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
            e.printStackTrace();
        }

    }
}
