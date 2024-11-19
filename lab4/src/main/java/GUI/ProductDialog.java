package GUI;

import Logic.Product;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProductDialog extends JDialog {
    private JTextField nameField;
    private JTextField carbsField;
    private JTextField proteinField;
    private JTextField fatField;
    private JComboBox<String> categoryComboBox;
    private JTextField quantityField;
    private JButton okButton;
    private JButton cancelButton;
    private Product product;

    private static final String[] CATEGORIES = {
            "Warzywa",
            "Owoce",
            "Mieso",
            "Nabial",
            "Produkty zbozowe",
            "Tluszcze",
            "Slodycze",
            "Inne"
    };

    public ProductDialog(Frame owner, String title, Product product) {
        super(owner, title, true);
        this.product = product;
        setSize(400, 300);
        setLocationRelativeTo(owner);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));

        panel.add(new JLabel("Nazwa:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Weglowodany (g):"));
        carbsField = new JTextField();
        panel.add(carbsField);

        panel.add(new JLabel("Bialko (g):"));
        proteinField = new JTextField();
        panel.add(proteinField);

        panel.add(new JLabel("Tluszcze (g):"));
        fatField = new JTextField();
        panel.add(fatField);

        panel.add(new JLabel("Kategoria:"));
        categoryComboBox = new JComboBox<>(CATEGORIES);
        categoryComboBox.setSelectedIndex(0); // Domyślnie wybrana pierwsza kategoria
        panel.add(categoryComboBox);

        panel.add(new JLabel("Ilosc (np. w gramach):"));
        quantityField = new JTextField();
        panel.add(quantityField);

        okButton = new JButton("OK");
        cancelButton = new JButton("Anuluj");
        panel.add(okButton);
        panel.add(cancelButton);

        add(panel);

        if (product != null) {
            nameField.setText(product.getName());
            carbsField.setText(String.valueOf(product.getCarbs()));
            proteinField.setText(String.valueOf(product.getProteins()));
            fatField.setText(String.valueOf(product.getFats()));
            categoryComboBox.setSelectedItem(product.getCategory());
            quantityField.setText(String.valueOf(product.getQuantity()));
        }

        okButton.addActionListener(e -> onOK());
        cancelButton.addActionListener(e -> onCancel());
    }

    private void onOK() {
        String name = nameField.getText().trim();
        String carbsText = carbsField.getText().trim();
        String proteinText = proteinField.getText().trim();
        String fatText = fatField.getText().trim();
        String category = (String) categoryComboBox.getSelectedItem();
        String quantityText = quantityField.getText().trim();

        if (name.isEmpty() || carbsText.isEmpty() || proteinText.isEmpty() || fatText.isEmpty() || category.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Prosze wypelnic wszystkie pola.", "Brak Danych", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double carbs = Double.parseDouble(carbsText);
            double protein = Double.parseDouble(proteinText);
            double fat = Double.parseDouble(fatText);
            double quantity = Double.parseDouble(quantityText);

            if (carbs < 0 || protein < 0 || fat < 0 || quantity < 0) {
                JOptionPane.showMessageDialog(this, "Wartosci musza byc dodatnie.", "Blad Wprowadzania", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (product == null) {
                product = new Product(0, name, carbs, protein, fat, category, quantity);//product manager nadpisuje zle Id
            } else {
                product.setName(name);
                product.setCarbs(carbs);
                product.setProteins(protein);
                product.setFats(fat);
                product.setCategory(category);
                product.setQuantity(quantity);
            }
            System.out.println("Product created: " + product);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Prosze wprowadzic prawidlowe liczby.", "Blad Wprowadzania", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        product = null;
        dispose();
    }

    public Product getProduct() {
        return product;
    }
}
