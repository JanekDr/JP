package Logic;

public class MealProduct {
    private int id;
    private static int mealProductIdCounter = 1;
    private Product product; // Referencja do oryginalnego produktu
    private double quantityMultiplier; // np. 0.5 oznacza połowę podstawowej ilości

    public MealProduct(Product product, double quantityMultiplier) {
        this.id = mealProductIdCounter++;
        this.product = product;
        this.quantityMultiplier = quantityMultiplier;
    }

    // Gettery
    public int getId() {
        return id;
    }

    public int getProductId() {
        return product.getId();
    }

    public Product getProduct() {
        return product;
    }

    public String getProductName() {
        return product.getName();
    }

    public double getCarbs() {
        return product.getCarbs() * quantityMultiplier;
    }

    public double getProtein() {
        return product.getProteins() * quantityMultiplier;
    }

    public double getFat() {
        return product.getFats() * quantityMultiplier;
    }

    public String getCategory() {
        return product.getCategory();
    }

    public double getQuantity() {
        return product.getQuantity() * quantityMultiplier;
    }

    public double getQuantityMultiplier() {
        return quantityMultiplier;
    }

    public void setQuantityMultiplier(double quantityMultiplier) {
        this.quantityMultiplier = quantityMultiplier;
    }

    public double getCalories() {
        return product.getCalories() * quantityMultiplier;
    }

    @Override
    public String toString() {
        return String.format("%s (x%.2f)", product.getName(), quantityMultiplier);
    }
}
