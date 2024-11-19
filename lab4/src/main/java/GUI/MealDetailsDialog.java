package GUI;

import Logic.Meal;
import Logic.MealProduct;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class MealDetailsDialog extends JDialog {
    private Meal meal;

    public MealDetailsDialog(Frame owner, Meal meal) {
        super(owner, "Szczegoly Posilku: " + meal.getName(), true);
        this.meal = meal;
        setSize(600, 400);
        setLocationRelativeTo(owner);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        String[] columnNames = {"ID Produktu", "Nazwa Produktu", "Ilosc (g)", "Kalorie"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        List<MealProduct> products = meal.getMealProducts();
        for (MealProduct mp : products) {
            Object[] row = {
                    mp.getProductId(),
                    mp.getProductName(),
                    String.format("%.2f", mp.getQuantity()),
                    String.format("%.2f", mp.getCalories())
            };
            tableModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel(new GridLayout(2, 1));

        JLabel totalCaloriesLabel = new JLabel(String.format("Calkowite Kalorie: %.2f kcal", meal.getTotalCalories()));
        summaryPanel.add(totalCaloriesLabel);

        JLabel totalMacrosLabel = new JLabel(String.format("Weglowodany: %.2f g, Tluszcze: %.2f g, Bialko: %.2f g",
                meal.getTotalCarbs(), meal.getTotalFats(), meal.getTotalProteins()));
        summaryPanel.add(totalMacrosLabel);

        add(summaryPanel, BorderLayout.SOUTH);

        JButton closeButton = new JButton("Zamknij");
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.NORTH);
    }
}
