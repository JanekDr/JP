package GUI;

import Logic.Meal;
import Logic.MealProduct;
import Logic.MealProductTableModel;
import Logic.ProductManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class EditMealDialog extends JDialog {
    private JTextField nameField;
    private JButton addProductButton;
    private JButton removeProductButton;
    private JButton okButton;
    private JButton cancelButton;
    private JTable mealProductTable;
    private MealProductTableModel tableModel;
    private Meal meal;
    private ProductManager productManager;

    public EditMealDialog(Frame owner, String title, Meal meal, ProductManager productManager) {
        super(owner, title, true);
        this.meal = meal;
        this.productManager = productManager;
        setSize(600, 400);
        setLocationRelativeTo(owner);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // Górny panel z nazwą posiłku
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Nazwa Posiłku:"));
        nameField = new JTextField(meal.getName(), 20);
        topPanel.add(nameField);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Środkowy panel z tabelą składników
        tableModel = new MealProductTableModel(meal.getMealProducts());
        mealProductTable = new JTable(tableModel);
        mealProductTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(mealProductTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Dolny panel z przyciskami
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        addProductButton = new JButton("Dodaj Produkt");
        removeProductButton = new JButton("Usuń Produkt");
        okButton = new JButton("OK");
        cancelButton = new JButton("Anuluj");

        bottomPanel.add(addProductButton);
        bottomPanel.add(removeProductButton);
        bottomPanel.add(okButton);
        bottomPanel.add(cancelButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Dodanie ActionListenerów
        addProductButton.addActionListener(e -> onAddProduct());
        removeProductButton.addActionListener(e -> onRemoveProduct());
        okButton.addActionListener(e -> onOK());
        cancelButton.addActionListener(e -> onCancel());

        // Obsługa zamknięcia dialogu za pomocą przycisku "X"
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void onAddProduct() {
        // Dialog do wyboru produktu i ustawienia ilości
        AddProductToMealDialog dialog = new AddProductToMealDialog(this, "Dodaj Produkt do Posiłku", productManager.getAllProducts());
        dialog.setVisible(true);

        List<MealProduct> mealProducts = dialog.getMealProducts();
        if (mealProducts != null && !mealProducts.isEmpty()) {
            for (MealProduct mp : mealProducts) {
                tableModel.addMealProduct(mp);
            }
        }
    }

    private void onRemoveProduct() {
        int selectedRow = mealProductTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz usunąć wybrany produkt z posiłku?", "Potwierdzenie Usunięcia", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.removeMealProduct(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Proszę wybrać produkt do usunięcia.", "Brak Wyboru", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onOK() {
        String newName = nameField.getText().trim();
        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nazwa posiłku nie może być pusta.", "Błąd Wprowadzania", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Aktualizacja nazwy posiłku
        meal.setName(newName);

        // Aktualizacja składników posiłku
        meal.getMealProducts().clear();
        meal.getMealProducts().addAll(tableModel.getMealProducts());

        dispose();
    }

    private void onCancel() {
        // Anuluj zmiany
        dispose();
    }

    public Meal getMeal() {
        return meal;
    }
}
