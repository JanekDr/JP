package GUI;

import Logic.Meal;
import Logic.MealProduct;
import Logic.Product;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class EditMealDialog extends JDialog {
    private Meal meal;
    private JTable mealProductTable;
    private MealProductTableModel tableModel;
    private JButton addButton;
    private JButton removeButton;
    private JButton saveButton;
    private JButton cancelButton;

    public EditMealDialog(Frame owner, Meal meal) {
        super(owner, "Edytuj Posilek: " + meal.getName(), true);
        this.meal = meal;
        setSize(600, 400);
        setLocationRelativeTo(owner);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Tabela do wyświetlania i edycji MealProducts
        tableModel = new MealProductTableModel(meal.getMealProducts());
        mealProductTable = new JTable(tableModel);
        mealProductTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mealProductTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(mealProductTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel przycisków
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Dodaj Produkt");
        removeButton = new JButton("Usun Wybrany Produkt");
        saveButton = new JButton("Zapisz");
        cancelButton = new JButton("Anuluj");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Dodanie akcji do przycisków
        addButton.addActionListener(e -> addProduct());
        removeButton.addActionListener(e -> removeSelectedProduct());
        saveButton.addActionListener(e -> saveChanges());
        cancelButton.addActionListener(e -> dispose());
    }

    private void addProduct() {
        AddProductToMealDialog addDialog = new AddProductToMealDialog((Frame) getOwner(), "Dodaj Produkty do Posilku", getAllProducts());
        addDialog.setVisible(true);

        List<MealProduct> newMealProducts = addDialog.getMealProducts();
        if (newMealProducts != null && !newMealProducts.isEmpty()) {
            for (MealProduct mp : newMealProducts) {
                meal.addProduct(mp);
                tableModel.addMealProduct(mp);
            }
        }
    }

    private void removeSelectedProduct() {
        int selectedRow = mealProductTable.getSelectedRow();
        if (selectedRow >= 0) {
            MealProduct mp = tableModel.getMealProductAt(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Czy na pewno chcesz usunac produkt: " + mp.getProductName() + "?",
                    "Potwierdzenie Usuniecia",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                meal.deleteProduct(mp);
                tableModel.removeMealProduct(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Proszę wybrać produkt do usunięcia.", "Brak Wyboru", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveChanges() {
        // Zaktualizuj mnożniki ilości na podstawie tabeli
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            MealProduct mp = tableModel.getMealProductAt(i);
            double newMultiplier = (double) tableModel.getValueAt(i, 1);
            if (newMultiplier <= 0) {
                JOptionPane.showMessageDialog(this, "Ilosc musi być dodatnia.", "Blad Walidacji", JOptionPane.ERROR_MESSAGE);
                return;
            }
            mp.setQuantityMultiplier(newMultiplier);
        }
        dispose();
    }

    private List<Product> getAllProducts() {
        // Pobierz wszystkie produkty z listy produktów w głównym GUI
        if (getOwner() instanceof DietBuilderGUI) {
            DietBuilderGUI gui = (DietBuilderGUI) getOwner();
            return gui.getAllProducts();
        }
        return new ArrayList<>();
    }

    // Model tabeli do edycji MealProducts
    class MealProductTableModel extends AbstractTableModel {
        private String[] columnNames = {"Nazwa Produktu", "Ilosc (mnoznik)"};
        private List<MealProduct> mealProducts;

        public MealProductTableModel(List<MealProduct> mealProducts) {
            this.mealProducts = new ArrayList<>(mealProducts);
        }

        @Override
        public int getRowCount() {
            return mealProducts.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            MealProduct mp = mealProducts.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return mp.getProductName();
                case 1:
                    return mp.getQuantityMultiplier();
                default:
                    return null;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1; // Tylko ilość jest edytowalna
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 1) {
                try {
                    double multiplier = Double.parseDouble(aValue.toString());
                    if (multiplier > 0) {
                        mealProducts.get(rowIndex).setQuantityMultiplier(multiplier);
                        fireTableCellUpdated(rowIndex, columnIndex);
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(EditMealDialog.this, "Prosze wprowadzic prawidlowa liczbe dodatnia.", "Blad Wprowadzania", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        public MealProduct getMealProductAt(int row) {
            return mealProducts.get(row);
        }

        public void addMealProduct(MealProduct mp) {
            mealProducts.add(mp);
            fireTableRowsInserted(mealProducts.size() - 1, mealProducts.size() - 1);
        }

        public void removeMealProduct(int row) {
            mealProducts.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }
}

