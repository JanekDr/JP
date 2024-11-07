package Logic;

public class MealProduct extends Product {
    private double quantity;

    public MealProduct(String name, double carbs, double fats, double proteins, String category, double quantity, double quantityMultiplier) {
        super(name, carbs, fats, proteins, category);
        this.quantity = quantity;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getTotalCalories(){
        return super.getCalories()*quantity;
    }

    @Override
    public String toString() {
        return String.format("%s (x%.2f)", super.getName(), quantity);
    }
}
