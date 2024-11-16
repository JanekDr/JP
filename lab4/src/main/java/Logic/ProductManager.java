package Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ProductManager {
    private List<Product> products;
    private int nextId;

    public ProductManager() {
        this.products = new ArrayList<>();
        this.nextId = 1; // Początkowy ID
    }

    public Product addProduct(Product productToAdd) {
        Product product = new Product(
                nextId++,
                productToAdd.getName(),
                productToAdd.getCarbs(),
                productToAdd.getProteins(),
                productToAdd.getFats(),
                productToAdd.getCategory(),
                productToAdd.getQuantity());
        products.add(product);
        System.out.println("Product created: " + product);
        return product;
    }

    public boolean editProduct(int id, Product updatedProduct) {
        Optional<Product> optProduct = products.stream().filter(p -> p.getId() == id).findFirst();
        if (optProduct.isPresent()) {
            Product product = optProduct.get();
            product.setName(updatedProduct.getName());
            product.setCarbs(updatedProduct.getCarbs());
            product.setProteins(updatedProduct.getProteins());
            product.setFats(updatedProduct.getFats());
            product.setCategory(updatedProduct.getCategory());
            product.setQuantity(updatedProduct.getQuantity());
            return true;
        }
        return false;
    }

    public boolean removeProduct(int id) {
        return products.removeIf(p -> p.getId() == id);
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }


    public void setProducts(List<Product> products) {
        this.products = new ArrayList<>(products);
        this.nextId = products.stream().mapToInt(Product::getId).max().orElse(0) + 1;
    }


    public Product findProductById(int id) {
        return products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }
}