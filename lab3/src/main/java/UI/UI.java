package UI;

import Logic.*;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UI{
    private Scanner scanner = new Scanner(System.in);
    private EmployeesManager employeesManager;
    private CommentManager commentManager;
    private TrendAnalyzer trendAnalyzer;
    private InputValidator inputValidator;
    FileHandler fileHandler = new FileHandler();
    private List<Employee> employees = new ArrayList<>();

    public UI(){
        this.inputValidator = new InputValidator(scanner);
        this.employeesManager = new EmployeesManager();
        this.commentManager = new CommentManager(employeesManager);
        this.trendAnalyzer = new TrendAnalyzer(employeesManager);
    }

    public void displayMainMenu() throws IOException {
        System.out.println("=== Glowne Menu ===");
        System.out.println("1. Pracownicy");
        System.out.println("2. Opinie");
        System.out.println("3. Analiza Trendow");
        System.out.println("4. Zapisz");
        System.out.println("5. Wczytaj dane");
        System.out.println("0. Wyjdz");

        int choice = inputValidator.getValidOptionInput(5);
        handleMenuOption(choice);
    }

    private void handleMenuOption(int choice) throws IOException {
        switch (choice) {
            case 1 -> displayEmployeeMenu();
            case 2 -> displayCommentMenu();
            case 3 -> displayTrendAnalysis();
            case 4 -> saveData();
            case 5 -> loadData();
            case 0 -> System.out.println("Wyjscie z aplikacji...");
            default -> System.out.println("Nieznana opcja, sprobuj ponownie.");
        }
    }

    private void displayEmployeeMenu() throws IOException {
        System.out.println("=== Menu Pracownikow ===");
        System.out.println("1. Dodaj pracownika");
        System.out.println("2. Zobacz wszystkich pracownikow");
        System.out.println("3. Zobacz pracownika");
        System.out.println("4. Usun pracownika");
        System.out.println("0. Powrot do glownego menu");

        int choice = inputValidator.getValidOptionInput(4);
        handleEmployeeOption(choice);
    }

    private void handleEmployeeOption(int choice) throws IOException {
        switch (choice) {
            case 1 -> addEmployee();
            case 2 -> viewEmployees();
            case 3 -> viewEmployee();
            case 4 -> deleteEmployee();
            case 0 -> displayMainMenu();
            default -> System.out.println("Nieznana opcja, sprobuj ponownie.");
        }
    }

    private void displayCommentMenu() throws IOException {
        System.out.println("=== Menu Opinii ===");
        System.out.println("1. Dodaj opinie");
        System.out.println("2. Zobacz wszystkie opinie");
        System.out.println("3. Zobacz opinie");
        System.out.println("4. Usun opinie");
        System.out.println("0. Powrot do glownego menu");

        int choice = inputValidator.getValidOptionInput(4);
        handleCommentOption(choice);
    }

    private void handleCommentOption(int choice) throws IOException {
        switch (choice) {
            case 1 -> addComment();
            case 2 -> viewComments();
            case 3 -> viewComment();
            case 4 -> deleteComment();
            case 0 -> displayMainMenu();
            default -> System.out.println("Nieznana opcja, sprobuj ponownie.");
        }
    }

    private void displayTrendAnalysis() throws IOException {
        System.out.println("=== Analiza Trendow ===");
        System.out.println("Podaj id pracownika.");
        int employeeId = inputValidator.getValidIdInput();
        String period = inputValidator.getValidPeriod();
        trendAnalyzer.analyzeTrend(employeeId, period);
        displayMainMenu();
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
        int employeeId = inputValidator.getValidIdInput();
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
        int employeeId = inputValidator.getValidIdInput();
        if(employeesManager.deleteEmployee(employeeId)){
            System.out.println("Pomyslnie usunieto pracownika.");
        } else {
            System.out.println("Nie znaleziono pracownika o takim id.");
        }
        displayEmployeeMenu();
    }

    private void addComment() throws IOException {
        System.out.println("Podaj id pracownika.");
        int employeeId = inputValidator.getValidIdInput();
        System.out.println("Dodaj opinie lub nic nie wpisuj.");
        scanner.nextLine();
        String content = scanner.nextLine();
        int weight = inputValidator.getValidWeightInput();
        scanner.nextLine();
        String type = inputValidator.getValidOpinionType();
        LocalDate date = inputValidator.getValidDate();

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
        int employeeId = inputValidator.getValidIdInput();
        System.out.println("Czy chcesz wyswietlic wszystkie opinie czy jakas szczegolna (wszystkie/jedna)?");
        String choice = inputValidator.getValidChoice();

        if(choice.equals("w")){
            List<Comment> comments = commentManager.getCommentsByEmployeeId(employeeId);
            if(comments != null){
                System.out.println("Komentarze dla pracownika id: " + employeeId);
                for (Comment comment : comments) {
                    System.out.println(comment);
                }
            } else {
                System.out.println("Nie znaleziono pracownika o takim id.");
            }

        } else if(choice.equals("j")){
            System.out.println("Podaj id opinii.");
            int commentId = inputValidator.getValidIdInput();
            if(commentManager.getCommentById(employeeId,commentId)!=null){
                System.out.println(commentManager.getCommentById(employeeId,commentId));
            } else {
                System.out.println("Nie znaleziono opinii o takim id.");
            }
        }



        displayCommentMenu();
    }

    private void deleteComment() throws IOException {
        System.out.println("Podaj id pracownika.");
        int employeeId = inputValidator.getValidIdInput();
        System.out.println("Podaj id opinii.");
        int commentId = inputValidator.getValidIdInput();
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
            trendAnalyzer = new TrendAnalyzer(employeesManager);
            System.out.println("Pomyslnie wczytano dane");
        } else {
            System.out.println("Wystapil blad podczas wczytywania danych.");
        }
        displayMainMenu();
    }
}
