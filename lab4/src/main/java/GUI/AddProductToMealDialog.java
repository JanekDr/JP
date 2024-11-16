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
    private JTextField multiplierField;
    private JButton addButton;
    private JButton cancelButton;
    private List<MealProduct> mealProducts;

    public AddProductToMealDialog(Window owner, String title, List<Product> availableProducts) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        setSize(400, 300);
        setLocationRelativeTo(owner);
        initUI(availableProducts);
    }


    private void initUI(List<Product> availableProducts) {
        setLayout(new BorderLayout());

        productJList = new JList<>(new DefaultListModel<>());
        DefaultListModel<Product> listModel = (DefaultListModel<Product>) productJList.getModel();
        for (Product p : availableProducts) {
            listModel.addElement(p);
        }
        productJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        add(new JScrollPane(productJList), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());

        bottomPanel.add(new JLabel("Ilość (mnożnik):"));
        multiplierField = new JTextField("1.0", 5);
        bottomPanel.add(multiplierField);

        addButton = new JButton("Dodaj");
        cancelButton = new JButton("Anuluj");
        bottomPanel.add(addButton);
        bottomPanel.add(cancelButton);

        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> onAdd());
        cancelButton.addActionListener(e -> onCancel());
    }

    private void onAdd() {
        List<Product> selectedProducts = productJList.getSelectedValuesList();
        if (selectedProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Proszę wybrać przynajmniej jeden produkt.", "Brak Wyboru", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String multiplierText = multiplierField.getText().trim();
        double multiplier;
        try {
            multiplier = Double.parseDouble(multiplierText);
            if (multiplier <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Proszę wprowadzić prawidłową liczbę dodatnią jako mnożnik.", "Błąd Wprowadzania", JOptionPane.ERROR_MESSAGE);
            return;
        }

        mealProducts = new ArrayList<>();
        for (Product p : selectedProducts) {
            MealProduct mp = new MealProduct(p, multiplier);
            mealProducts.add(mp);
        }

        dispose();
    }

    private void onCancel() {
        mealProducts = null;
        dispose();
    }

    public List<MealProduct> getMealProducts() {
        return mealProducts;
    }
}
