package GUI;

import Logic.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProductDialog extends JDialog {
    private JTextField nameField;
    private JTextField carbsField;
    private JTextField fatsField;
    private JTextField proteinsField;
    private JTextField categoryField;
    private JTextField quantityField;
    private Product product;

    public ProductDialog(Frame owner, String title, Product existingProduct) {
        super(owner, title, true);
        setLayout(new GridLayout(7, 2, 10, 10));
        setSize(400, 300);
        setLocationRelativeTo(owner);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Carbs (g):"));
        carbsField = new JTextField();
        add(carbsField);

        add(new JLabel("Fats (g):"));
        fatsField = new JTextField();
        add(fatsField);

        add(new JLabel("Proteins (g):"));
        proteinsField = new JTextField();
        add(proteinsField);

        add(new JLabel("Category:"));
        categoryField = new JTextField();
        add(categoryField);

        add(new JLabel("Base Quantity (g):"));
        quantityField = new JTextField();
        add(quantityField);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        add(saveButton);
        add(cancelButton);

        if (existingProduct != null) {
            nameField.setText(existingProduct.getName());
            carbsField.setText(String.valueOf(existingProduct.getCarbs()));
            fatsField.setText(String.valueOf(existingProduct.getFat()));
            proteinsField.setText(String.valueOf(existingProduct.getProtein()));
            categoryField.setText(existingProduct.getCategory());
//            quantityField.setText(String.valueOf(existingProduct.getQuantity()));
        }

        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                double carbs = Double.parseDouble(carbsField.getText());
                double fats = Double.parseDouble(fatsField.getText());
                double proteins = Double.parseDouble(proteinsField.getText());
                String category = categoryField.getText().trim();
                double quantity = Double.parseDouble(quantityField.getText());

                if (name.isEmpty() || category.isEmpty()) {
                    throw new IllegalArgumentException("Name and Category cannot be empty.");
                }

                product = new Product(name, carbs, fats, proteins, category);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> {
            product = null;
            dispose();
        });
    }

    public Product getProduct() {
        return product;
    }
}
