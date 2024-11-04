package Logic;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TrendAnalyzer {

    private EmployeesManager employeesManager;

    public TrendAnalyzer(EmployeesManager employeesManager) {
        this.employeesManager = employeesManager;
    }

    public void analyzeTrend(int employeeId, String period) {
        Employee employee = employeesManager.getEmployeeById(employeeId);
        if (employee == null) {
            System.out.println("Nie znaleziono pracownika o ID: " + employeeId);
            return;
        }

        LocalDate now = LocalDate.now();
        LocalDate startDate;

        switch (period.toLowerCase()) {
            case "tygodniowy":
                startDate = now.minusWeeks(1);
                break;
            case "miesieczny":
                startDate = now.minusMonths(1);
                break;
            case "kwartalny":
                startDate = now.minusMonths(3);
                break;
            default:
                System.out.println("Nieznany okres: " + period);
                return;
        }

        List<Comment> commentsInPeriod = employee.getComments().stream()
                .filter(comment -> comment.getDate().isAfter(startDate))
                .collect(Collectors.toList());

        long positiveCount = commentsInPeriod.stream()
                .filter(comment -> "pozytywna".equalsIgnoreCase(comment.getType()))
                .count();

        int positiveWeight = commentsInPeriod.stream()
                .filter(comment -> "pozytywna".equalsIgnoreCase(comment.getType()))
                .mapToInt(Comment::getWeight)
                .sum();

        long negativeCount = commentsInPeriod.stream()
                .filter(comment -> "negatywna".equalsIgnoreCase(comment.getType()))
                .count();

        int negativeWeight = commentsInPeriod.stream()
                .filter(comment -> "negatywna".equalsIgnoreCase(comment.getType()))
                .mapToInt(Comment::getWeight)
                .sum();

        System.out.println("#########################################################");
        System.out.println("Analiza trendu dla pracownika: " + employee.getName());
        System.out.println("Okres: " + period);
        System.out.println("Pozytywne opinie: " + positiveCount + ", laczna waga: " + positiveWeight);
        System.out.println("Negatywne opinie: " + negativeCount + ", laczna waga: " + negativeWeight);
        System.out.println();
        if (positiveCount*positiveWeight > negativeCount*negativeWeight) {
            System.out.println("W tym okresie przewazaly opinie pozytywne.");
        } else if (positiveCount * positiveWeight < negativeCount * negativeWeight) {
            System.out.println("W tym okresie przewazaly opinie negatywne.");
        } else {
            System.out.println("Liczba pozytywnych i negatywnych opinii jest rowna.");
        }
        System.out.println("#########################################################");
    }
}
