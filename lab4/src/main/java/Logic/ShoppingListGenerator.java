package Logic;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingListGenerator {
    private ProductManager productManager;

    public ShoppingListGenerator(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void generatePDF(List<Meal> selectedMeals, String filePath) {
        if (selectedMeals == null || selectedMeals.isEmpty()) {
            return;
        }

        // Krok 1: Agregacja produktów według kategorii
        Map<String, Map<String, Double>> categorizedProducts = aggregateProductsByCategory(selectedMeals);

        // Krok 2: Generowanie PDF
        try {
            createPDF(categorizedProducts, filePath);
        } catch (IOException ignored) {
        }
    }
    private Map<String, Map<String, Double>> aggregateProductsByCategory(List<Meal> meals) {
        Map<String, Map<String, Double>> categoryMap = new HashMap<>();
        for (Meal meal : meals) {
            for (MealProduct mp : meal.getMealProducts()) {
                Product product = productManager.findProductById(mp.getProductId());
                if (product != null) {
                    String category = product.getCategory();
                    String productName = product.getName();
                    double quantity = mp.getQuantityMultiplier() * product.getQuantity();

                    categoryMap.putIfAbsent(category, new HashMap<>());
                    Map<String, Double> productsInCategory = categoryMap.get(category);
                    productsInCategory.put(productName, productsInCategory.getOrDefault(productName, 0.0) + quantity);
                }
            }
        }

        return categoryMap;
    }

    private void createPDF(Map<String, Map<String, Double>> categorizedProducts, String filePath) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);


        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
        float yPosition = PDRectangle.A4.getHeight() - 50;
        float margin = 50;
        float leading = 14.5f;

        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Lista Zakupow");
        contentStream.endText();

        yPosition -= 30;
        contentStream.setFont(PDType1Font.HELVETICA, 12);

        for (String category : categorizedProducts.keySet()) {
            if (yPosition <= 50) {
                contentStream.close();
                page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                yPosition = PDRectangle.A4.getHeight() - 50;
            }

            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(category);
            contentStream.endText();
            yPosition -= 20;
            contentStream.setFont(PDType1Font.HELVETICA, 12);

            Map<String, Double> products = categorizedProducts.get(category);
            for (Map.Entry<String, Double> entry : products.entrySet()) {
                String productLine = String.format("- %s x %.2f (g)", entry.getKey(), entry.getValue());
                contentStream.beginText();
                contentStream.newLineAtOffset(margin + 20, yPosition);
                contentStream.showText(productLine);
                contentStream.endText();
                yPosition -= leading;
            }

            yPosition -= 10;
        }

        contentStream.close();
        document.save(filePath);
        document.close();
    }

}