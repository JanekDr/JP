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
    private JComboBox<String> categoryComboBox;
    private JTextField quantityField;
    private Product product;

    public ProductDialog(Frame owner, String title, Product existingProduct) {
        super(owner, title, true);
        setLayout(new GridLayout(7, 2, 10, 10));
        setSize(400, 400);
        setLocationRelativeTo(owner);

        add(new JLabel("Nazwa:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Weglowodany (g):"));
        carbsField = new JTextField();
        add(carbsField);

        add(new JLabel("Tluszcze (g):"));
        fatsField = new JTextField();
        add(fatsField);

        add(new JLabel("Bialko (g):"));
        proteinsField = new JTextField();
        add(proteinsField);

        add(new JLabel("Kategoria:"));
        categoryComboBox = new JComboBox<>(new String[] {"Zboza", "Owoce", "Warzywa", "Bialka", "Tluszcze", "Produkty Sniadaniowe", "Inne"});
        add(categoryComboBox);

        add(new JLabel("Podstawowa Ilosc (g):"));
        quantityField = new JTextField();
        add(quantityField);

        JButton saveButton = new JButton("Zapisz");
        JButton cancelButton = new JButton("Anuluj");
        add(saveButton);
        add(cancelButton);

        if (existingProduct != null) {
            nameField.setText(existingProduct.getName());
            carbsField.setText(String.valueOf(existingProduct.getCarbs()));
            fatsField.setText(String.valueOf(existingProduct.getFats()));
            proteinsField.setText(String.valueOf(existingProduct.getProteins()));
            categoryComboBox.setSelectedItem(existingProduct.getCategory());
            quantityField.setText(String.valueOf(existingProduct.getQuantity()));
        }

        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                double carbs = Double.parseDouble(carbsField.getText());
                double fats = Double.parseDouble(fatsField.getText());
                double protein = Double.parseDouble(proteinsField.getText());
                String category = (String) categoryComboBox.getSelectedItem();
                double quantity = Double.parseDouble(quantityField.getText());

                if (name.isEmpty() || category.isEmpty()) {
                    throw new IllegalArgumentException("Nazwa i Kategoria nie moga byc puste.");
                }

                if (existingProduct == null) {
                    product = new Product(name, carbs, protein, fats, category, quantity);
                } else {
                    // Aktualizacja istniejącego produktu
                    existingProduct.setName(name);
                    existingProduct.setCarbs(carbs);
                    existingProduct.setFats(fats);
                    existingProduct.setProteins(protein);
                    existingProduct.setCategory(category);
                    existingProduct.setQuantity(quantity);
                    product = existingProduct;
                }

                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Prosze wprowadzic prawidlowe wartosci liczbowe.", "Blad Wprowadzania", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Blad Wprowadzania", JOptionPane.ERROR_MESSAGE);
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
