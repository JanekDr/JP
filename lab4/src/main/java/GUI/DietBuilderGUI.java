package GUI;

import Logic.Meal;
import Logic.MealProduct;
import Logic.Product;
import Logic.ShoppingListGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DietBuilderGUI extends JFrame {
    private DefaultListModel<Product> productListModel;
    private DefaultListModel<Meal> mealListModel;

    public DietBuilderGUI() {
        setTitle("Diet Builder");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        productListModel = new DefaultListModel<>();
        mealListModel = new DefaultListModel<>();

        initUI();
    }

    private void initUI() {
        // Split pane for products and meals
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createProductPanel(), createMealPanel());
        splitPane.setDividerLocation(500);
        add(splitPane);
    }

    private JPanel createProductPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Produkty"));

        JList<Product> productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(productList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj Produkt");
        JButton editButton = new JButton("Edytuj Produkt");
        JButton deleteButton = new JButton("Usun Produkt");

        addButton.addActionListener(e -> addProduct());
        editButton.addActionListener(e -> editProduct(productList));
        deleteButton.addActionListener(e -> deleteProduct(productList));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMealPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Posilki"));

        JList<Meal> mealList = new JList<>(mealListModel);
        mealList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(mealList), BorderLayout.CENTER);

        // Dodanie MouseListener do wykrywania podwójnych kliknięć
        mealList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Podwójne kliknięcie
                    int index = mealList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        Meal selectedMeal = mealListModel.getElementAt(index);
                        showMealDetails(selectedMeal);
                    }
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj Posilek");
        JButton editButton = new JButton("Edytuj Posilek");
        JButton deleteButton = new JButton("Usun Posilek");
        JButton generateShoppingListButton = new JButton("Generuj Liste Zakupow");

        addButton.addActionListener(e -> addMeal());
        editButton.addActionListener(e -> editMeal(mealList));
        deleteButton.addActionListener(e -> deleteMeal(mealList));
        generateShoppingListButton.addActionListener(e -> generateShoppingList());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(generateShoppingListButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addProduct() {
        ProductDialog dialog = new ProductDialog(this, "Dodaj Produkt", null);
        dialog.setVisible(true);

        Product product = dialog.getProduct();
        if (product != null) {
            productListModel.addElement(product);
        }
    }

    private void editProduct(JList<Product> productList) {
        Product selectedProduct = productList.getSelectedValue();
        if (selectedProduct != null) {
            ProductDialog dialog = new ProductDialog(this, "Edytuj Produkt", selectedProduct);
            dialog.setVisible(true);

            Product updatedProduct = dialog.getProduct();
            if (updatedProduct != null) {
                // Aktualizacja istniejącego produktu
                selectedProduct.setName(updatedProduct.getName());
                selectedProduct.setCarbs(updatedProduct.getCarbs());
                selectedProduct.setProteins(updatedProduct.getProteins());
                selectedProduct.setFats(updatedProduct.getFats());
                selectedProduct.setCategory(updatedProduct.getCategory());
                selectedProduct.setQuantity(updatedProduct.getQuantity());

                // Odświeżenie listy produktów
                productList.repaint();

                // Aktualizacja posiłków zawierających ten produkt
                updateMealsWithProduct(selectedProduct);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Proszę wybrać produkt do edycji.", "Brak Wyboru", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteProduct(JList<Product> productList) {
        Product selectedProduct = productList.getSelectedValue();
        if (selectedProduct != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz usunąć wybrany produkt?", "Potwierdzenie Usuniecia", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                productListModel.removeElement(selectedProduct);
                removeProductFromAllMeals(selectedProduct);
                JOptionPane.showMessageDialog(this, "Produkt zostal usuniety ze wszystkich posilkow.", "Usunieto Produkt", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Prosze wybrac produkt do usuniecia.", "Brak Wyboru", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removeProductFromAllMeals(Product product) {
        StringBuilder affectedMeals = new StringBuilder();

        for (int i = 0; i < mealListModel.size(); i++) {
            Meal meal = mealListModel.getElementAt(i);
            List<
                    MealProduct> productsToRemove = new ArrayList<>();

            for (MealProduct mp : meal.getMealProducts()) {
                if (mp.getProductId() == product.getId()) { // Porównanie po productId
                    productsToRemove.add(mp);
                }
            }

            if (!productsToRemove.isEmpty()) {
                for (MealProduct mp : productsToRemove) {
                    meal.deleteProduct(mp);
                }
                affectedMeals.append(meal.getName()).append("\n");
                // Aktualizuj listę posiłków, aby odświeżyć wyświetlanie
                mealListModel.setElementAt(meal, i);
            }
        }

        if (affectedMeals.length() > 0) {
            JOptionPane.showMessageDialog(this, "Produkt zostal usuniety z nastepujacych posilkow:\n" + affectedMeals.toString(), "Produkt Usunięty z Posiłków", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateMealsWithProduct(Product updatedProduct) {
        for (int i = 0; i < mealListModel.size(); i++) {
            Meal meal = mealListModel.getElementAt(i);
            boolean updated = false;

            for (MealProduct mp : meal.getMealProducts()) {
                if (mp.getProductId() == updatedProduct.getId()) { // Porównanie po productId
                    // Aktualizacja szczegółów produktu w posiłku
                    // Ponieważ MealProduct przechowuje referencję do Product, zmiany w Product są automatyczne
                    // Jeśli chciałbyś odświeżyć wyświetlanie, możesz to zrobić poprzez ponowne ustawienie elementu
                    updated = true;
                }
            }

            if (updated) {
                // Odświeżenie listy posiłków
                mealListModel.setElementAt(meal, i);
            }
        }
    }

    private void addMeal() {
        String mealName = JOptionPane.showInputDialog(this, "Wprowadz nazwe posilku:");
        if (mealName != null && !mealName.trim().isEmpty()) {
            Meal meal = new Meal(mealName);
            mealListModel.addElement(meal);
            addProductsToMeal(meal);
            // Informuj model, że posiłek został zmodyfikowany
            mealListModel.setElementAt(meal, mealListModel.getSize() - 1);
        }
    }

    private void editMeal(JList<Meal> mealList) {
        Meal selectedMeal = mealList.getSelectedValue();
        if (selectedMeal != null) {
            EditMealDialog editDialog = new EditMealDialog(this, selectedMeal);
            editDialog.setVisible(true);

            // Po zamknięciu dialogu, odśwież listę posiłków
            mealList.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Proszę wybrać posiłek do edycji.", "Brak Wyboru", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteMeal(JList<Meal> mealList) {
        Meal selectedMeal = mealList.getSelectedValue();
        if (selectedMeal != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz usunąć wybrany posiłek?", "Potwierdzenie Usunięcia", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                mealListModel.removeElement(selectedMeal);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Proszę wybrać posiłek do usunięcia.", "Brak Wyboru", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addProductsToMeal(Meal meal) {
        for(Product p : getAllProducts()){
            System.out.println(p);
        }
        AddProductToMealDialog dialog = new AddProductToMealDialog(this, "Dodaj Produkty do " + meal.getName(), getAllProducts());
        dialog.setVisible(true);

        List<MealProduct> mealProducts = dialog.getMealProducts();
        if (mealProducts != null && !mealProducts.isEmpty()) {
            for (MealProduct mp : mealProducts) {
                meal.addProduct(mp);
            }
            // Odświeżenie listy posiłków, jeśli to konieczne
            // Jeśli toString() posiłku nie zależy od jego zawartości, nie musisz nic robić
            // Jeśli zależy, możesz wywołać:
            mealListModel.setElementAt(meal, mealListModel.indexOf(meal));
        }
    }

    private void showMealDetails(Meal meal) {
        MealDetailsDialog detailsDialog = new MealDetailsDialog(this, meal);
        detailsDialog.setVisible(true);
    }

    private void generateShoppingList() {
        if (mealListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Brak posilkow do wygenerowania listy zakupow.", "Brak Danych", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Pobierz wszystkie posiłki
        List<Meal> meals = java.util.Collections.list(mealListModel.elements());

        // Wybierz lokalizację pliku
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Zapisz Liste Zakupow");
        fileChooser.setSelectedFile(new java.io.File("lista_zakupow.pdf"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            try {
                ShoppingListGenerator.generatePDF(meals, fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Lista zakupów zostala wygenerowana pomyslnie.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Blad podczas generowania PDF: " + ex.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < productListModel.size(); i++) {
            products.add(productListModel.getElementAt(i));
        }
        return products;
    }
}
