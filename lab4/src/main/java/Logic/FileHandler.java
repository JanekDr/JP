package Logic;

import java.io.*;
import java.util.List;

public class FileHandler {

    public static void saveData(File file, List<Product> products, List<Meal> meals) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(products);
            oos.writeObject(meals);
        }
    }

    @SuppressWarnings("unchecked")
    public static Object[] loadData(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Product> products = (List<Product>) ois.readObject();
            List<Meal> meals = (List<Meal>) ois.readObject();

            return new Object[]{products, meals};
        }
    }
}
