package Logic;

import javax.swing.table.AbstractTableModel;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MealProductTableModel extends AbstractTableModel {
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
                JOptionPane.showMessageDialog(null, "Prosze wprowadzic prawidlowa liczbe dodatnią.", "Blad Wprowadzania", JOptionPane.ERROR_MESSAGE);
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

    public List<MealProduct> getMealProducts() {
        return new ArrayList<>(mealProducts);
    }
}
