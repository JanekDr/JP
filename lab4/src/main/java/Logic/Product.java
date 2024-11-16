package Logic;

import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
//    private static int productIdCounter = 1;
    private String name;
    private double carbs;
    private double protein;
    private double fat;
    private String category;
    private double quantity; // w gramach

    public Product(int id, String name, double carbs, double protein, double fat, String category, double quantity) {
        this.id = id;
        this.name = name;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
        this.category = category;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getProteins() {
        return protein;
    }

    public double getFats() {
        return fat;
    }

    public String getCategory() {
        return category;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public void setProteins(double protein) {
        this.protein = protein;
    }

    public void setFats(double fat) {
        this.fat = fat;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }


    public double getCalories() {
        return (carbs * 4) + (protein * 4) + (fat * 9);
    }

    @Override
    public String toString() {
        return String.format("%s (%.2fg)", name, quantity);
    }
}
