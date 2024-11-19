package GUI;

import Logic.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DietBuilderGUI extends JFrame {
    private DefaultListModel<Product> productListModel;
    private DefaultListModel<Meal> mealListModel;
    private ProductManager productManager;
    private MealManager mealManager;
    private ShoppingListGenerator shoppingListGenerator;

    public DietBuilderGUI() {
        setTitle("Diet Builder");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        productListModel = new DefaultListModel<>();
        mealListModel = new DefaultListModel<>();
        productManager = new ProductManager();
        mealManager = new MealManager();
        shoppingListGenerator = new ShoppingListGenerator(productManager);

        initUI();
    }

    private void initUI() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Plik");
        JMenuItem loadDataItem = new JMenuItem("Wczytaj dane");
        loadDataItem.addActionListener(e -> loadData());
        fileMenu.add(loadDataItem);

        JMenuItem saveDataItem = new JMenuItem("Zapisz dane");
        saveDataItem.addActionListener(e -> saveData());
        fileMenu.add(saveDataItem);
        fileMenu.addSeparator();

        JMenuItem generatePDFItem = new JMenuItem("Generuj liste zakupow");
        generatePDFItem.addActionListener(e -> generateShoppingList());
        fileMenu.add(generatePDFItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

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

        mealList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
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

        addButton.addActionListener(e -> addMeal());
        editButton.addActionListener(e -> editMeal(mealList));
        deleteButton.addActionListener(e -> deleteMeal(mealList));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addProduct() {
        ProductDialog dialog = new ProductDialog(this, "Dodaj Produkt", null);
        dialog.setVisible(true);

        Product product = dialog.getProduct();
        if (product != null) {
            Product newProduct = productManager.addProduct(product);
            productListModel.addElement(newProduct);
        }
    }


    private void editProduct(JList<Product> productList) {
        Product selectedProduct = productList.getSelectedValue();
        if (selectedProduct != null) {
            ProductDialog dialog = new ProductDialog(this, "Edytuj Produkt", selectedProduct);
            dialog.setVisible(true);

            Product updatedProduct = dialog.getProduct();
            if (updatedProduct != null) {
                boolean success = productManager.editProduct(selectedProduct.getId(), updatedProduct);
                if (success) {
                    productList.repaint();
                    updateMealsWithProduct(productManager.findProductById(selectedProduct.getId()));
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Prosze wybrac produkt do edycji.", "Brak Wyboru", JOptionPane.WARNING_MESSAGE);
        }
    }


    private void deleteProduct(JList<Product> productList) {
        Product selectedProduct = productList.getSelectedValue();
        if (selectedProduct != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Czy na pewno chcesz usunac wybrany produkt?\n" +
                            "Spowoduje to usuniecie produktu rowniez z powiazanych posilkow.", "Potwierdzenie Usuniecia", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = productManager.removeProduct(selectedProduct);
                if (success) {
                    productListModel.removeElement(selectedProduct);
                    removeProductFromAllMeals(selectedProduct);
                    JOptionPane.showMessageDialog(this, "Produkt zostal usuniety ze wszystkich posilkow.", "Usunieto Produkt", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Prosze wybrac produkt do usuniecia.", "Brak Wyboru", JOptionPane.WARNING_MESSAGE);
        }
    }


    private void removeProductFromAllMeals(Product product) {
        StringBuilder affectedMeals = new StringBuilder();

        for (int i = 0; i < mealListModel.size(); i++) {
            Meal meal = mealListModel.getElementAt(i);
            List<MealProduct> productsToRemove = new ArrayList<>();

            for (MealProduct mp : meal.getMealProducts()) {
                if (mp.getProductId() == product.getId()) {
                    productsToRemove.add(mp);
                }
            }

            if (!productsToRemove.isEmpty()) {
                for (MealProduct mp : productsToRemove) {
                    meal.deleteProduct(mp);
                }
                affectedMeals.append(meal.getName()).append("\n");
                mealListModel.setElementAt(meal, i);
            }
        }

        if (affectedMeals.length() > 0) {
            JOptionPane.showMessageDialog(this, "Produkt zostal usuniety z nastepujacych posilkow:\n" + affectedMeals.toString(), "Produkt usuniety z posilkow", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateMealsWithProduct(Product updatedProduct) {
        for (Meal meal : mealManager.getAllMeals()) {
            boolean updated = false;

            for (MealProduct mp : meal.getMealProducts()) {
                if (mp.getProductId() == updatedProduct.getId()) {
                    updated = true;
                }
            }

            if (updated) {
                mealListModel.setElementAt(meal, mealListModel.indexOf(meal));
            }
        }
    }


    private void addMeal() {
        String mealName = JOptionPane.showInputDialog(this, "Wprowadz nazwe posilku:");
        if (mealName != null && !mealName.trim().isEmpty()) {
            Meal meal = mealManager.addMeal(mealName);
            mealListModel.addElement(meal);
            addProductsToMeal(meal);
        }
    }


    private void editMeal(JList<Meal> mealList) {
        Meal selectedMeal = mealList.getSelectedValue();
        if (selectedMeal != null) {
            EditMealDialog dialog = new EditMealDialog(this, "Edytuj Posilek", selectedMeal, productManager);
            dialog.setVisible(true);
            mealListModel.setElementAt(selectedMeal, mealListModel.indexOf(selectedMeal));
        } else {
            JOptionPane.showMessageDialog(this, "Prosze wybrac posilek do edycji.", "Brak Wyboru", JOptionPane.WARNING_MESSAGE);
        }
    }



    private void deleteMeal(JList<Meal> mealList) {
        Meal selectedMeal = mealList.getSelectedValue();
        if (selectedMeal != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz usunac wybrany posilek?", "Potwierdzenie Usuniecia", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = mealManager.removeMeal(selectedMeal);
                if (success) {
                    mealListModel.removeElement(selectedMeal);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Prosze wybrac posilek do usuniecia.", "Brak Wyboru", JOptionPane.WARNING_MESSAGE);
        }
    }


    private void addProductsToMeal(Meal meal) {
        AddProductToMealDialog dialog = new AddProductToMealDialog(this, "Dodaj Produkty do " + meal.getName(), productManager.getAllProducts());
        dialog.setVisible(true);

        List<MealProduct> mealProducts = dialog.getMealProducts();
        if (mealProducts != null && !mealProducts.isEmpty()) {
            for (MealProduct mp : mealProducts) {
                mealManager.addProductToMeal(meal, mp);
            }
            mealListModel.setElementAt(meal, mealListModel.indexOf(meal));
        }
    }

    private void showMealDetails(Meal meal) {
        MealDetailsDialog detailsDialog = new MealDetailsDialog(this, meal);
        detailsDialog.setVisible(true);
    }

    private void loadData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Wczytaj Dane");
        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            try {
                Object[] data = FileHandler.loadData(fileToLoad);
                List<Product> products = (List<Product>) data[0];
                List<Meal> meals = (List<Meal>) data[1];

                setProducts(products);
                setMeals(meals);

                JOptionPane.showMessageDialog(this, "Dane zostaly pomyslnie wczytane.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Blad podczas wczytywania danych: " + ex.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Zapisz Dane");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                FileHandler.saveData(fileToSave, productManager.getAllProducts(), mealManager.getAllMeals());
                JOptionPane.showMessageDialog(this, "Dane zostaly pomyslnie zapisane.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Blad podczas zapisywania danych: " + ex.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void generateShoppingList() {
        GenerateShoppingListDialog dialog = new GenerateShoppingListDialog(
                this,
                mealManager,
                shoppingListGenerator
        );
        dialog.setVisible(true);
    }

    public void setProducts(List<Product> products) {
        productManager.setProducts(products);
        productListModel.clear();
        for (Product p : productManager.getAllProducts()) {
            productListModel.addElement(p);
        }
    }

    public void setMeals(List<Meal> meals) {
        mealManager.setMeals(meals);
        mealListModel.clear();
        for (Meal m : mealManager.getAllMeals()) {
            mealListModel.addElement(m);
        }
    }
}
