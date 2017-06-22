package Util;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.inject.Inject;
import controllers.admin.OrderController;
import models.order.Order;
import models.order.OrderProduct;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugins.S3Plugin;

import javax.persistence.Transient;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by muneeb on 16/03/17.
 */
public class PdfUtility {

    private static final Logger LOG = LoggerFactory.getLogger(PdfUtility.class);
    public static String getInvoice(Order order) {
        String fileName = "/tmp/invoice-"+order.id+".pdf";


        PDFont pdfFont = PDType1Font.HELVETICA;
        float fontSize = 10;
        float headingSize = 14;
        float leading = 1.1f * fontSize;
        final PDPage singlePage = new PDPage();
        final PDFont courierBoldFont = PDType1Font.COURIER_BOLD;
        final PDFont normalFont = PDType1Font.HELVETICA;
        try (final PDDocument document = new PDDocument())
        {
            document.addPage(singlePage);
            PDPageContentStream contentStream = new PDPageContentStream(document, singlePage);

            //PDImageXObject pdImage = PDImageXObject.createFromFile("C:/logo.png", document);

            drawLine(contentStream,50);
            drawLine(contentStream,70);

            contentStream.beginText();

            int yOffset = 770;

            contentStream.setFont(courierBoldFont, headingSize);
            contentStream.newLineAtOffset(singlePage.getMediaBox().getWidth()/2-20, yOffset);
            contentStream.setLeading(leading);
            contentStream.showText("Invoice ");





            contentStream.setFont(courierBoldFont, fontSize);

            contentStream.newLineAtOffset(-singlePage.getMediaBox().getWidth()/2+30,-20);
            yOffset = yOffset-20;

            contentStream.showText("Order id :"+order.id);

            contentStream.newLineAtOffset(400,0);
            contentStream.showText("Order date :"+order.getUpdatedTime());


            contentStream.newLineAtOffset(-390,-20);
            yOffset = yOffset-20;


            contentStream.showText("#");
            contentStream.newLineAtOffset(30,0);
            contentStream.showText("Product");
            contentStream.newLineAtOffset(300,0);
            contentStream.showText("Qty");
            contentStream.newLineAtOffset(80,0);
            contentStream.showText("Price/unit");
            contentStream.newLineAtOffset(80,0);
            contentStream.showText("Total");

            contentStream.newLineAtOffset(-490,-20);
            yOffset = 0;
            int pageCount = 1;

            contentStream.setFont(normalFont, 10);
            int i = 1;
            int k = 0;
            //for(int j=0;j<30;j++) {
                for(OrderProduct product: order.items) {
                    contentStream.showText(i+"");
                    contentStream.newLineAtOffset(30,0);
                    contentStream.showText(product.name);
                    contentStream.newLineAtOffset(320,0);
                    contentStream.showText(product.quantity+"");
                    contentStream.newLineAtOffset(65,0);
                    contentStream.showText(product.price+"");
                    contentStream.newLineAtOffset(80,0);
                    contentStream.showText(product.quantity*product.price+"");
                    contentStream.newLineAtOffset(-495,-15);
                    yOffset = yOffset+15;
                    i++;
                    k++;
                    if(k%40 == 0) {
                        yOffset = 0;
                        contentStream.close();
                        contentStream = addNewPage(document);
                        pageCount++;
                        contentStream.newLineAtOffset(-490,-20);
                    }
                }
           // }


            contentStream.setFont(pdfFont, 13);
            contentStream.newLineAtOffset(singlePage.getMediaBox().getWidth()-180,-10);
            contentStream.showText("Total : "+order.totalPrice);
            contentStream.newLine();

            contentStream.endText();

            LOG.info("yOffset is {}",singlePage.getMediaBox().getHeight()+yOffset);

            if(pageCount == 1) {
                drawLine(contentStream,yOffset+72);
            } else {
                if(yOffset != 0) //no line if its a start of page
                    drawLine(contentStream,yOffset+60);
            }

            contentStream.close();  // Stream must be closed before saving document.
            document.save(fileName);
        }
        catch (IOException ioEx)
        {
            LOG.error("Exception while trying to create simple document - {}", ioEx);
        }
        return fileName;
    }

    private static void  drawRect(PDPageContentStream content, Color color, Rectangle rect, boolean fill) throws IOException{
        content.addRect(rect.x, rect.y, rect.width, rect.height);
        if (fill) {
            content.setNonStrokingColor(color);
            content.fill();
        } else {
            content.setStrokingColor(color);
            content.stroke();
        }
    }


    private static void drawLine(PDPageContentStream contentStream,int y) throws IOException{
        int startX = 0;
        PDPage page =  new PDPage();
        int startY = (int)page.getMediaBox().getHeight()-y;
        int width = (int)page.getMediaBox().getWidth();
        int height = 1;
        Color color = Color.DARK_GRAY;

        drawRect(contentStream, color, new java.awt.Rectangle(startX, startY, width, height), true);

//        startY = startY-20;
//        drawRect(contentStream, color, new java.awt.Rectangle(startX, startY, width, height), true);
    }


    private static PDPageContentStream addNewPage(PDDocument document) throws IOException{

        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        float fontSize = 10;
        final PDFont courierBoldFont = PDType1Font.COURIER_BOLD;
        final PDFont normalFont = PDType1Font.HELVETICA;
        PDFont pdfFont = PDType1Font.HELVETICA;

        float headingSize = 14;
        float leading = 1.1f * fontSize;
        final PDPage singlePage = new PDPage();

        drawLine(contentStream,30);
        drawLine(contentStream,50);

        contentStream.beginText();

        int yOffset = 770;

        contentStream.setFont(courierBoldFont, headingSize);
        contentStream.newLineAtOffset(singlePage.getMediaBox().getWidth()/2-20, yOffset);
        contentStream.setLeading(leading);
        contentStream.showText("Invoice ");

        contentStream.newLineAtOffset(-250,-20);
        contentStream.setFont(courierBoldFont, fontSize);
        contentStream.showText("#");
        contentStream.newLineAtOffset(30,0);
        contentStream.showText("Product");
        contentStream.newLineAtOffset(300,0);
        contentStream.showText("Qty");
        contentStream.newLineAtOffset(80,0);
        contentStream.showText("Price/unit");
        contentStream.newLineAtOffset(80,0);
        contentStream.showText("Total");

        contentStream.setFont(normalFont, 10);
        return contentStream;
    }

}
