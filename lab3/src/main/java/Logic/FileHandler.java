package Logic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String FILE_PATH = "employees_data.ser";
    public boolean saveEmployees(List<Employee> employees){
        try
        {
            FileOutputStream fileOut = new FileOutputStream(FILE_PATH);
            ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
            outStream.writeObject(employees);
            outStream.close();
            fileOut.close();
            return true;
        }catch(IOException i)
        {
            i.printStackTrace();
            return false;
        }
    }

    public List<Employee> loadEmployees(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<Employee>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Plik z danymi nie zostal znaleziony.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Blad podczas wczytywania danych z pliku: " + e.getMessage());
        }
        return null;
    }
}
