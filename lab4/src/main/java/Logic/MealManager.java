package Logic;


import java.util.ArrayList;
import java.util.List;


public class MealManager {
    private List<Meal> meals;

    public MealManager() {
        this.meals = new ArrayList<>();
    }
    public Meal addMeal(String name) {
        Meal meal = new Meal(name);
        meals.add(meal);
        return meal;
    }

    public boolean removeMeal(Meal meal) {
        return meals.remove(meal);
    }


    public List<Meal> getAllMeals() {
        return new ArrayList<>(meals);
    }

    public void setMeals(List<Meal> meals) {
        this.meals = new ArrayList<>(meals);
    }


    public void addProductToMeal(Meal meal, MealProduct mealProduct) {
        meal.addProduct(mealProduct);
    }

}
