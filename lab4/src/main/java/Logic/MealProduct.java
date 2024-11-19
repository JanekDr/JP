package Logic;

import java.io.Serializable;

public class MealProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    private Product product;
    private double quantityMultiplier;
    public MealProduct(Product product, double quantityMultiplier) {
        this.product = product;
        this.quantityMultiplier = quantityMultiplier;
    }



    public int getProductId() {
        return product.getId();
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
