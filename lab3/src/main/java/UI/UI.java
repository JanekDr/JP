package UI;

import java.util.Scanner;

public class UI {
//pracownikow, opinie, trendy bedzie mozna modyfikowac poprzez podanie ID lub po wyswietleniu listy tych przypisow
    private Scanner scanner = new Scanner(System.in);

    public void displayMainMenu() {
        System.out.println("=== Główne Menu ===");
        System.out.println("1. Pracownicy");
        System.out.println("2. Opinie");
        System.out.println("3. Analiza Trendów");
        System.out.println("0. Wyjdź");

        int choice = getValidInput(3);
        handleMenuOption(choice);
    }

    private void handleMenuOption(int choice) {
        switch (choice) {
            case 1 -> displayEmployeeMenu();
            case 2 -> displayCommentMenu();
            case 3 -> displayTrendAnalysis();
            case 0 -> System.out.println("Wyjście z aplikacji...");
            default -> System.out.println("Nieznana opcja, spróbuj ponownie.");
        }
    }

    private void displayEmployeeMenu() {
        System.out.println("=== Menu Pracowników ===");
        System.out.println("1. Dodaj pracownika");
        System.out.println("2. Zobacz pracowników");
        System.out.println("3. Modyfikuj pracownika");
        System.out.println("4. Usuń pracownika");
        System.out.println("0. Powrót do głównego menu");

        int choice = getValidInput(4);
        handleEmployeeOption(choice);
    }

    private void handleEmployeeOption(int choice) {
        switch (choice) {
            case 1 -> addEmployee();
            case 2 -> viewEmployees();
            case 3 -> modifyEmployee();
            case 4 -> deleteEmployee();
            case 0 -> displayMainMenu();
            default -> System.out.println("Nieznana opcja, spróbuj ponownie.");
        }
    }

    private void displayCommentMenu() {
        System.out.println("=== Menu Opinii ===");
        System.out.println("1. Dodaj opinię");
        System.out.println("2. Zobacz opinie");
        System.out.println("3. Modyfikuj opinię");
        System.out.println("4. Usuń opinię");
        System.out.println("0. Powrót do głównego menu");

        int choice = getValidInput(4);
        handleCommentOption(choice);
    }

    private void handleCommentOption(int choice) {
        switch (choice) {
            case 1 -> addComment();
            case 2 -> viewComments();
            case 3 -> modifyComment();
            case 4 -> deleteComment();
            case 0 -> displayMainMenu();
            default -> System.out.println("Nieznana opcja, spróbuj ponownie.");
        }
    }

    private void displayTrendAnalysis() {
        System.out.println("=== Analiza Trendów ===");
        // Wywołanie analizy trendów (tu dodajemy wywołanie logiki analizy)
    }

    // Poniżej metody "placeholder" dla logiki aplikacji:

    private void addEmployee() {
        System.out.println("Dodawanie nowego pracownika...");
        // Dodaj logikę do dodawania pracownika
    }

    private void viewEmployees() {
        System.out.println("Lista pracowników:");
        // Dodaj logikę do wyświetlania pracowników
    }

    private void modifyEmployee() {
        System.out.println("Modyfikowanie pracownika...");
        // Dodaj logikę do modyfikacji pracownika
    }

    private void deleteEmployee() {
        System.out.println("Usuwanie pracownika...");
        // Dodaj logikę do usuwania pracownika
    }

    private void addComment() {
        System.out.println("Dodawanie nowej opinii...");
        // Dodaj logikę do dodawania opinii
    }

    private void viewComments() {
        System.out.println("Lista opinii:");
        // Dodaj logikę do wyświetlania opinii
    }

    private void modifyComment() {
        System.out.println("Modyfikowanie opinii...");
        // Dodaj logikę do modyfikacji opinii
    }

    private void deleteComment() {
        System.out.println("Usuwanie opinii...");
        // Dodaj logikę do usuwania opinii
    }

    private int getValidInput(int optionsQuantity){
        boolean valid = false;
        int choiceToInt = 0;
        while(!valid){
            System.out.println("Podaj numer opcji z zakresu [0;" + optionsQuantity+"]: ");
            String choice = scanner.next();
            try{
                choiceToInt = Integer.parseInt(choice);
                if (choiceToInt>=0 && choiceToInt<=optionsQuantity)valid=true;
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidłowe wejście. Wprowadź liczbę.");
            }
        }
        return choiceToInt;
    }
}
