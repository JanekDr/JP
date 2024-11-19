package Logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Meal implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private static int mealIdCounter = 1;
    private String name;
    private List<MealProduct> mealProducts;

    public Meal(String name) {
        this.id = mealIdCounter++;
        this.name = name;
        this.mealProducts = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public List<MealProduct> getMealProducts() {
        return mealProducts;
    }


    public void addProduct(MealProduct mealProduct) {
        mealProducts.add(mealProduct);
    }

    public void deleteProduct(MealProduct mealProduct) {
        mealProducts.remove(mealProduct);
    }

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

    public void setName(String newName) {
        this.name = newName;
    }
}
