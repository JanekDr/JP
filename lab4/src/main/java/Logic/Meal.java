package Logic;

import java.util.ArrayList;
import java.util.List;

public class Meal {
    private int id;
    private static int mealIdCounter = 1;
    private String name;
    private List<MealProduct> mealProducts;

    public Meal(String name) {
        this.id = mealIdCounter++;
        this.name = name;
        this.mealProducts = new ArrayList<>();
    }

    // Gettery i Settery
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<MealProduct> getMealProducts() {
        return mealProducts;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addProduct(MealProduct mealProduct) {
        mealProducts.add(mealProduct);
    }

    public void deleteProduct(MealProduct mealProduct) {
        mealProducts.remove(mealProduct);
    }

    // Metody do obliczania sum makroskładników i kalorii
    public double getTotalCarbs() {
        return mealProducts.stream().mapToDouble(MealProduct::getCarbs).sum();
    }

    public double getTotalFats() {
        return mealProducts.stream().mapToDouble(MealProduct::getFat).sum();
    }

    public double getTotalProteins() {
        return mealProducts.stream().mapToDouble(MealProduct::getProtein).sum();
    }

    public double getTotalCalories() {
        return mealProducts.stream().mapToDouble(MealProduct::getCalories).sum();
    }

    @Override
    public String toString() {
        return name;
    }
}
