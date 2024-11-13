package GUI;

import Logic.MealProduct;
import Logic.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class AddProductToMealDialog extends JDialog {
    private JList<Product> productJList;
    private DefaultListModel<Product> listModel;
    private JTextField quantityField;
    private JButton addButton;
    private JButton doneButton;
    private List<MealProduct> mealProducts;

    public AddProductToMealDialog(Frame owner, String title, List<Product> productList) {
        super(owner, title, true);
        setLayout(new BorderLayout());
        setSize(500, 400);
        setLocationRelativeTo(owner);

        listModel = new DefaultListModel<>();
        for(Product p:productList){
            listModel.addElement(p);
        }

        productJList = new JList<>(listModel);
        productJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        add(new JScrollPane(productJList), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));

        JPanel quantityPanel = new JPanel();
        quantityPanel.add(new JLabel("Ilosc(mnoznik, np. 2 dla x2):"));
        quantityField = new JTextField(10);
        quantityPanel.add(quantityField);
        bottomPanel.add(quantityPanel);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Dodaj Wybrane Produkty do Posilku");
        doneButton = new JButton("Zakoncz");
        buttonPanel.add(addButton);
        buttonPanel.add(doneButton);
        bottomPanel.add(buttonPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addSelectedProducts());
        doneButton.addActionListener(e -> dispose());

        mealProducts = new ArrayList<>();
    }

    private void addSelectedProducts() {
        List<Product> selectedProducts = productJList.getSelectedValuesList();
        if (selectedProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Prosze wybrac co najmniej jeden produkt.", "Brak Wyboru", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String multiplierText = quantityField.getText().trim();
        if (multiplierText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Prosze wprowadzić ilosc.", "Blad Wprowadzania", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double multiplier = Double.parseDouble(multiplierText);
            if (multiplier <= 0) {
                throw new NumberFormatException("Mnoznik musi byc dodatni.");
            }

            for (Product p : selectedProducts) {
                MealProduct mp = new MealProduct(p, multiplier);
                mealProducts.add(mp);
            }

            JOptionPane.showMessageDialog(this, "Wybrane produkty zostaly dodane do posilku.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            // Opcjonalnie: Możesz wyczyścić wybory po dodaniu
            productJList.clearSelection();
            quantityField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Prosze wprowadzic prawidlowa liczbe dla ilosci.", "Blad Wprowadzania", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<MealProduct> getMealProducts() {
        return mealProducts;
    }
}
