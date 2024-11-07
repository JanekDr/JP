package GUI;

import Logic.Meal;
import Logic.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DietBuilderGUI extends JFrame {
    private DefaultListModel<Product> productListModel;
    private DefaultListModel<Meal> mealListModel;

    public DietBuilderGUI() {
        setTitle("Diet Builder");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        productListModel = new DefaultListModel<>();
        mealListModel = new DefaultListModel<>();

        initUI();
    }

    private void initUI() {
        // Split pane for products and meals
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createProductPanel(), createMealPanel());
        splitPane.setDividerLocation(450);
        add(splitPane);
    }

    private JPanel createProductPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Products"));

        JList<Product> productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(productList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Product");
        JButton editButton = new JButton("Edit Product");
        JButton deleteButton = new JButton("Delete Product");

        addButton.addActionListener(e -> addProduct());
        editButton.addActionListener(e -> editProduct(productList));
        deleteButton.addActionListener(e -> deleteProduct(productList));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMealPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Meals"));

        JList<Meal> mealList = new JList<>(mealListModel);
        mealList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(mealList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Meal");
        JButton editButton = new JButton("Edit Meal");
        JButton deleteButton = new JButton("Delete Meal");
        JButton generateShoppingListButton = new JButton("Generate Shopping List");

        addButton.addActionListener(e -> addMeal());
        editButton.addActionListener(e -> editMeal(mealList));
        deleteButton.addActionListener(e -> deleteMeal(mealList));
        generateShoppingListButton.addActionListener(e -> generateShoppingList());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(generateShoppingListButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addProduct() {
        ProductDialog dialog = new ProductDialog(this, "Add Product", null);
        dialog.setVisible(true);

        Product product = dialog.getProduct();
        if (product != null) {
            productListModel.addElement(product);
        }
    }

    private void editProduct(JList<Product> productList) {
        Product selectedProduct = productList.getSelectedValue();
        if (selectedProduct != null) {
            ProductDialog dialog = new ProductDialog(this, "Edit Product", selectedProduct);
            dialog.setVisible(true);

            Product updatedProduct = dialog.getProduct();
            if (updatedProduct != null) {
                productListModel.setElementAt(updatedProduct, productList.getSelectedIndex());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteProduct(JList<Product> productList) {
        Product selectedProduct = productList.getSelectedValue();
        if (selectedProduct != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected product?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                productListModel.removeElement(selectedProduct);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addMeal() {
        String mealName = JOptionPane.showInputDialog(this, "Enter Meal Name:");
        if (mealName != null && !mealName.trim().isEmpty()) {
            Meal meal = new Meal(mealName);
            mealListModel.addElement(meal);
            addProductsToMeal(meal);
        }
    }

    private void editMeal(JList<Meal> mealList) {
        Meal selectedMeal = mealList.getSelectedValue();
        if (selectedMeal != null) {
            String newName = JOptionPane.showInputDialog(this, "Edit Meal Name:", selectedMeal.getName());
            if (newName != null && !newName.trim().isEmpty()) {
                selectedMeal = new Meal(newName); // Recreating meal with new name
                mealListModel.setElementAt(selectedMeal, mealList.getSelectedIndex());
                addProductsToMeal(selectedMeal);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a meal to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteMeal(JList<Meal> mealList) {
        Meal selectedMeal = mealList.getSelectedValue();
        if (selectedMeal != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected meal?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                mealListModel.removeElement(selectedMeal);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a meal to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addProductsToMeal(Meal meal) {
        AddProductToMealDialog dialog = new AddProductToMealDialog(this, "Add Products to " + meal.getName(), productListModel);
        dialog.setVisible(true);

        java.util.List<MealProduct> mealProducts = dialog.getMealProducts();
        if (mealProducts != null) {
            for (MealProduct mp : mealProducts) {
                meal.addProduct(mp);
            }
        }
    }

    private void generateShoppingList() {
        if (mealListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No meals available to generate a shopping list.", "No Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Collect all meals
        java.util.List<Meal> meals = java.util.Collections.list(mealListModel.elements());

        // Choose file location
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Shopping List");
        fileChooser.setSelectedFile(new java.io.File("shopping_list.pdf"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            try {
                ShoppingListGenerator.generatePDF(meals, fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Shopping list generated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error generating PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DietBuilderGUI gui = new DietBuilderGUI();
            gui.setVisible(true);
        });
    }
}
