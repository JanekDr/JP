package UI;

import Logic.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UI{
//pracownikow, opinie, trendy bedzie mozna modyfikowac poprzez podanie ID lub po wyswietleniu listy tych przypisow
    private Scanner scanner = new Scanner(System.in);
    private EmployeesManager employeesManager;
    private CommentManager commentManager;
    FileHandler fileHandler = new FileHandler();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private List<Employee> employees = new ArrayList<>();

    public UI(){
        this.employeesManager = new EmployeesManager();
        this.commentManager = new CommentManager(employeesManager);
    }

    public void displayMainMenu() throws IOException {
        System.out.println("=== Główne Menu ===");
        System.out.println("1. Pracownicy");
        System.out.println("2. Opinie");
        System.out.println("3. Analiza Trendów");
        System.out.println("4. Zapisz");
        System.out.println("5. Wczytaj dane");
        System.out.println("0. Wyjdź");

        int choice = getValidOptionInput(5);
        handleMenuOption(choice);
    }

    private void handleMenuOption(int choice) throws IOException {
        switch (choice) {
            case 1 -> displayEmployeeMenu();
            case 2 -> displayCommentMenu();
            case 3 -> displayTrendAnalysis();
            case 4 -> saveData();
            case 5 -> loadData();
            case 0 -> System.out.println("Wyjście z aplikacji...");
            default -> System.out.println("Nieznana opcja, spróbuj ponownie.");
        }
    }

    private void displayEmployeeMenu() throws IOException {
        System.out.println("=== Menu Pracowników ===");
        System.out.println("1. Dodaj pracownika");
        System.out.println("2. Zobacz wszystkich pracowników");
        System.out.println("3. Zobacz pracownika");
        System.out.println("4. Usuń pracownika");
        System.out.println("0. Powrót do głównego menu");

        int choice = getValidOptionInput(4);
        handleEmployeeOption(choice);
    }

    private void handleEmployeeOption(int choice) throws IOException {
        switch (choice) {
            case 1 -> addEmployee();
            case 2 -> viewEmployees();
            case 3 -> viewEmployee();
            case 4 -> deleteEmployee();
            case 0 -> displayMainMenu();
            default -> System.out.println("Nieznana opcja, spróbuj ponownie.");
        }
    }

    private void displayCommentMenu() throws IOException {
        System.out.println("=== Menu Opinii ===");
        System.out.println("1. Dodaj opinię");
        System.out.println("2. Zobacz wszystkie opinie");
        System.out.println("3. Zobacz opinie");
        System.out.println("4. Usuń opinię");
        System.out.println("0. Powrót do głównego menu");

        int choice = getValidOptionInput(4);
        handleCommentOption(choice);
    }

    private void handleCommentOption(int choice) throws IOException {
        switch (choice) {
            case 1 -> addComment();
            case 2 -> viewComments();
            case 3 -> viewComment();
            case 4 -> deleteComment();
            case 0 -> displayMainMenu();
            default -> System.out.println("Nieznana opcja, spróbuj ponownie.");
        }
    }

    private void displayTrendAnalysis() {
        System.out.println("=== Analiza Trendów ===");
        // Wywołanie analizy trendów (tu dodajemy wywołanie logiki analizy)
    }


    private void addEmployee() throws IOException {
        System.out.println("Podaj nazwe dla pracownika: ");
        String name = scanner.next();
        employeesManager.addEmployee(name);
        System.out.println("\nDodano pracownika.\n");
        displayMainMenu();
    }

    private void viewEmployees() throws IOException {
        List<Employee> employees = employeesManager.getAllEmployees();
        System.out.println("###############################");
        for (Employee e:employees){
            System.out.println(e);
        }
        System.out.println("###############################\n");
        displayEmployeeMenu();
    }
    private void viewEmployee() throws IOException {
        System.out.println("Podaj id pracownika.");
        int employeeId = getValidIdInput();
        var optionalEmployee = employeesManager.getEmployeeById(employeeId);
        if (optionalEmployee != null){
            Employee employee = optionalEmployee;
            System.out.println(employee);
        } else {
            System.out.println("\nNie znaleziono pracownika o takim id.\n");
        }
        displayEmployeeMenu();
    }

    private void deleteEmployee() throws IOException {
        System.out.println("Podaj id pracownika.");
        int employeeId = getValidIdInput();
        if(employeesManager.deleteEmployee(employeeId)){
            System.out.println("Pomyślnie usunieto pracownika.");
        } else {
            System.out.println("Nie znaleziono pracownika o takim id.");
        }
        displayEmployeeMenu();
    }

    private void addComment() throws IOException {
        System.out.println("Podaj id pracownika.");
        int employeeId = getValidIdInput();
        System.out.println("Dodaj opinie lub nic nie wpisuj.");
        scanner.nextLine();
        String content = scanner.nextLine();
        int weight = getValidWeightInput();
        scanner.nextLine();
        String type = getValidOpinionType();
        LocalDate date = getValidDate();

        if(commentManager.addComment(employeeId,content,weight,type,date)){
            System.out.println("Pomyslnie dodano opinie o pracownika.");
        } else {
            System.out.println("Nie znaleziono pracownika o takim id.");
        }
        displayCommentMenu();
    }

    private void viewComments() throws IOException {
        List<Comment> comments = commentManager.getAllComments();
        System.out.println("###############################");
        for (Comment c:comments){
            System.out.println(c);
        }
        System.out.println("###############################\n");
        displayCommentMenu();
    }
    private void viewComment() throws IOException {

        System.out.println("Podaj id pracownika.");
        int employeeId = getValidIdInput();
        System.out.println("Podaj id opinii.");
        int commentId = getValidIdInput();

        if(commentManager.getCommentById(employeeId,commentId)!=null){
            System.out.println(commentManager.getCommentById(employeeId,commentId));
        } else {
            System.out.println("Nie znaleziono opinii o takim id.");
        }

        displayCommentMenu();
    }

    private void deleteComment() throws IOException {
        System.out.println("Podaj id pracownika.");
        int employeeId = getValidIdInput();
        System.out.println("Podaj id opinii.");
        int commentId = getValidIdInput();
        if(commentManager.deleteComment(employeeId,commentId)==1){
            System.out.println("Pomyslnie usunieto opinie");
        } else if (commentManager.deleteComment(employeeId,commentId)==-1) {
            System.out.println("Nie znaleziono pracownika o takim id.");
        } else if (commentManager.deleteComment(employeeId,commentId)==-2) {
            System.out.println("Nie znaleziono opini o takim id.");
        }
        displayCommentMenu();
    }

    private void saveData() throws IOException {
        if(fileHandler.saveEmployees(employeesManager.getAllEmployees())){
            System.out.println("Pomyslnie zapisano dane.");
        } else {
            System.out.println("Wystapil blad podczas zapisywania danych.");
        }
        displayMainMenu();
    }

    private void loadData() throws IOException {
        if (fileHandler.loadEmployees() != null){
            employees = fileHandler.loadEmployees();
            employeesManager = new EmployeesManager(employees);
            commentManager = new CommentManager(employeesManager);
            System.out.println("Pomyslnie wczytano dane");
        } else {
            System.out.println("Wystapil blad podczas wczytywania danych.");
        }
        displayMainMenu();
    }

    private int getValidOptionInput(int optionsQuantity){
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

    private int getValidIdInput(){
        boolean valid = false;
        int choiceToInt = 0;
        while(!valid){
            System.out.println("Podaj numer id: ");
            String choice = scanner.next();
            try{
                choiceToInt = Integer.parseInt(choice);
                if (choiceToInt>=0)valid=true;
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidłowe wejście. Wprowadź liczbę.");
            }
        }
        return choiceToInt;
    }
    private int getValidWeightInput(){
        boolean valid = false;
        int weightToInt = 0;
        while(!valid){
            System.out.println("Podaj wagę swojej opinii z zakresu [0;10]: ");
            String choice = scanner.next();
            try{
                weightToInt = Integer.parseInt(choice);
                if (weightToInt>=0 && weightToInt<=10)valid=true;
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidłowe wejście. Wprowadź liczbę.");
            }
        }
        return weightToInt;
    }

    private String getValidOpinionType(){
        boolean valid = false;
        String type = "";
        while(!valid){
            System.out.println("Podaj typ swojej opinii (pozytywna/negatywna)");
            String input = scanner.next();
            if(input.equals("pozytywna") || input.equals("p") ||input.equals("negatywna") || input.equals("n"))valid=true;
        }
        return type;
    }

    private LocalDate getValidDate() {
        boolean valid = false;
        LocalDate date = null;
        while(!valid){
            System.out.println("Podaj date w formacie rok-miesiac-dzien");
            String input = scanner.next();
            try{
                LocalDate.parse(input, DATE_FORMAT);
                date = LocalDate.parse(input);
                valid = true;
            } catch (DateTimeParseException e) {
                System.out.println("Zly format daty.");
            }
        }
        return date;
    }
}
