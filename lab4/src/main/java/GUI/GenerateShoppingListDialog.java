package GUI;


import Logic.Meal;
import Logic.MealManager;
import Logic.ProductManager;
import Logic.ShoppingListGenerator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GenerateShoppingListDialog extends JDialog {
    private JList<Meal> mealJList;
    private DefaultListModel<Meal> mealListModel;
    private JButton generateButton;
    private JButton cancelButton;

    private MealManager mealManager;

    private ShoppingListGenerator shoppingListGenerator;

    public GenerateShoppingListDialog(Frame parent, MealManager mealManager, ShoppingListGenerator shoppingListGenerator) {
        super(parent, "Generuj Liste Zakupow", true);
        this.mealManager = mealManager;
        this.shoppingListGenerator = shoppingListGenerator;

        initUI();
        setLocationRelativeTo(parent);
        setSize(400, 500);
    }


    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(5,5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel titleLabel = new JLabel("Wybierz posilki:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        mealListModel = new DefaultListModel<>();
        List<Meal> allMeals = mealManager.getAllMeals();
        for (Meal meal : allMeals) {
            mealListModel.addElement(meal);
        }

        mealJList = new JList<>(mealListModel);
        mealJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(mealJList);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        generateButton = new JButton("Generuj");
        cancelButton = new JButton("Anuluj");

        buttonPanel.add(generateButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateShoppingList();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void generateShoppingList() {
        List<Meal> selectedMeals = mealJList.getSelectedValuesList();
        if (selectedMeals.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Prosze wybrac przynajmniej jeden posilek.", "Brak Wyboru", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Zapisz Liste Zakupow");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Pliki PDF (*.pdf)", "pdf");
        fileChooser.setFileFilter(filter);
        fileChooser.setSelectedFile(new java.io.File("lista_zakupow.pdf"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();

            if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
                fileToSave = new java.io.File(fileToSave.getParentFile(), fileToSave.getName() + ".pdf");
            }

            shoppingListGenerator.generatePDF(selectedMeals, fileToSave.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Lista zakupow zostala wygenerowana pomyslnie.", "Sukces", JOptionPane.INFORMATION_MESSAGE);

            dispose();
        }
    }
}

