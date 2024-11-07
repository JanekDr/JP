package Logic;

import java.util.ArrayList;
import java.util.List;

public class Meal {
    private String name;
    private List<MealProduct> mealProducts;

    public Meal(String name) {
        this.name = name;
        this.mealProducts = new ArrayList<>();
    }

    public void addProduct(MealProduct mealProduct){
        mealProducts.add(mealProduct);
    }

    public void deleteProduct(MealProduct mealProduct){
        return;
    }
}
