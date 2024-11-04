package UI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputValidator {
    private Scanner scanner;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public InputValidator(Scanner scanner) {
        this.scanner = scanner;
    }

    public int getValidOptionInput(int optionsQuantity) {
        int choiceToInt = 0;
        boolean valid = false;

        while (!valid) {
            System.out.println("Podaj numer opcji z zakresu [0;" + optionsQuantity + "]: ");
            String choice = scanner.next();
            try {
                choiceToInt = Integer.parseInt(choice);
                if (choiceToInt >= 0 && choiceToInt <= optionsQuantity) {
                    valid = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidlowe wejscie. Wprowadz liczbe.");
            }
        }
        return choiceToInt;
    }

    public int getValidIdInput() {
        int choiceToInt = 0;
        boolean valid = false;

        while (!valid) {
            System.out.println("Podaj numer id: ");
            String choice = scanner.next();
            try {
                choiceToInt = Integer.parseInt(choice);
                if (choiceToInt >= 0) {
                    valid = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidlowe wejscie. Wprowadz liczbe.");
            }
        }
        return choiceToInt;
    }

    public int getValidWeightInput() {
        int weightToInt = 0;
        boolean valid = false;

        while (!valid) {
            System.out.println("Podaj wage swojej opinii z zakresu [0;10]: ");
            String choice = scanner.next();
            try {
                weightToInt = Integer.parseInt(choice);
                if (weightToInt >= 0 && weightToInt <= 10) {
                    valid = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidlowe wejscie. Wprowadz liczbe.");
            }
        }
        return weightToInt;
    }

    public String getValidOpinionType() {
        String type = "";
        boolean valid = false;

        while (!valid) {
            System.out.println("Podaj typ swojej opinii (pozytywna/negatywna): ");
            String input = scanner.next();
            if (input.equals("pozytywna") || input.equals("p")) {
                valid = true;
                type = "pozytywna";
            } else if (input.equals("negatywna") || input.equals("n")) {
                valid = true;
                type = "negatywna";
            } else {
                System.out.println("Podane dane sa niepoprawne. Podaj je jeszcze raz.");
            }
        }
        return type;
    }

    public LocalDate getValidDate() {
        LocalDate date = null;
        boolean valid = false;

        while (!valid) {
            System.out.println("Podaj date w formacie rok-miesiac-dzien: ");
            String input = scanner.next();
            try {
                date = LocalDate.parse(input, DATE_FORMAT);
                valid = true;
            } catch (DateTimeParseException e) {
                System.out.println("Zly format daty.");
            }
        }
        return date;
    }

    public String getValidPeriod() {
        String input = "";
        boolean valid = false;

        while (!valid) {
            System.out.println("Podaj okres analizy (tygodniowy/miesieczny/kwartalny): ");
            input = scanner.next();
            if (input.equals("tygodniowy") || input.equals("miesieczny") || input.equals("kwartalny")) {
                valid = true;
            } else {
                System.out.println("Podane dane sa niepoprawne.");
            }
        }
        return input;
    }

    public String getValidChoice(){
        String choice = "";
        boolean valid = false;

        while (!valid) {
            String input = scanner.next();
            if (input.equals("wszystkie") || input.equals("w")) {
                valid = true;
                choice = "w";
            } else if (input.equals("jedna") || input.equals("j")) {
                valid = true;
                choice = "j";
            } else {
                System.out.println("Podane dane sa niepoprawne. Podaj je jeszcze raz.");
            }
        }
        return choice;
    }
}
