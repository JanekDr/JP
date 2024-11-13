package Logic;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.List;

public class ShoppingListGenerator {

    public static void generatePDF(List<Meal> meals, String filePath) throws IOException {
        PDDocument document = new PDDocument();

        try {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Ustawienia czcionki i rozmiaru
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 750);
            contentStream.showText("Lista Zakupow");
            contentStream.endText();

            contentStream.setFont(PDType1Font.HELVETICA, 12);
            float yPosition = 700;

            for (Meal meal : meals) {
                if (yPosition < 100) { // Dodaj nową stronę, jeśli brakuje miejsca
                    contentStream.close();
                    page = new PDPage(PDRectangle.LETTER);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yPosition = 750;
                }

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Posilek: " + meal.getName());
                contentStream.endText();
                yPosition -= 20;

                for (MealProduct mp : meal.getMealProducts()) {
                    String line = String.format("- %s x %.2f", mp.getProductName(), mp.getQuantityMultiplier());
                    contentStream.beginText();
                    contentStream.newLineAtOffset(70, yPosition);
                    contentStream.showText(line);
                    contentStream.endText();
                    yPosition -= 15;
                }

                yPosition -= 10; // Przerwa między posiłkami
            }

            contentStream.close();
            document.save(filePath);
        } finally {
            document.close();
        }
    }
}
