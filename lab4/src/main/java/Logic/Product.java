package Logic;

public class Product {
    private String name;
    private double carbs;
    private double protein;
    private double fat;
    private String category;


    public Product(String name, double carbs, double protein, double fat, String category) {
        this.name = name;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
        this.category = category;
    }

    public double getCalories(){
        return (carbs*4)+(protein*4)+(fat*9);
    }

    public String getName() {
        return name;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    public String getCategory() {
        return category;
    }
}
