package uz.gbway.enavbatthermalprintingservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.gbway.enavbatthermalprintingservice.dto.req.print.PrintReqDto;
import uz.gbway.enavbatthermalprintingservice.util.QrCodeUtil;
import uz.gbway.enavbatthermalprintingservice.util.ResourceLoaderUtil;
import uz.gbway.enavbatthermalprintingservice.util.TimeUtil;

import javax.print.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.List;

@Slf4j
@Service
public class PrintService {
    private final QrCodeUtil qrCodeUtil;
    private final TimeUtil timeUtil;
    private final ResourceLoaderUtil resourceLoaderUtil;

    public PrintService(QrCodeUtil qrCodeUtil, TimeUtil timeUtil, ResourceLoaderUtil resourceLoaderUtil) {
        this.qrCodeUtil = qrCodeUtil;
        this.timeUtil = timeUtil;
        this.resourceLoaderUtil = resourceLoaderUtil;
    }

    public int print(PrintReqDto req) {

        try {

            // Printer Onlinemi yoki Offline tekshiradi
            if (!checkIsPrinterOnline()) {
                throw new RuntimeException("Printer is not online");
            }

            PrinterJob job = PrinterJob.getPrinterJob();


            Paper paper = new Paper();

            double width = 210; // 80mm in points
            double height = 550; // long enough for a receipt

            paper.setSize(width, height);
            paper.setImageableArea(0, 0, width, height); // no margins

            PageFormat format = job.defaultPage();
            format.setPaper(paper);
            format.setOrientation(PageFormat.PORTRAIT);


            qrCodeInfoENavbatVBook(job, format, req);

            job.print(); // avtomatik chiqarish

        } catch (Exception e) {
            return 500;
        }


        return 200;
    }

    private void qrCodeInfoENavbatVBook(PrinterJob job, PageFormat format, PrintReqDto req) {
        Book book = new Book();

        BufferedImage qrBufferedImage = qrCodeUtil.generate(req.getQrNumber(), 180, 180);

        BufferedImage playMarketDownload = resourceLoaderUtil.loadPlayMarketDownlaodImage();

        // TODO davom qil
        BufferedImage playMarketDownloadQR = qrCodeUtil.generate("https://play.google.com/store/apps/details?id=com.eskishahar.app.enavbat&hl=ru", 180, 180);


        final int pageWidth = 210;

        book.append((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;

            javax.print.PrintService printService = job.getPrintService();

            Graphics2D grPage = (Graphics2D) graphics;

            // 180 daraja aylantirish (teskari chiqayotgan bo‘lsa)
            grPage.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            grPage.rotate(Math.toRadians(0), pageFormat.getImageableWidth(), pageFormat.getImageableHeight());


            int y = 0;

// post malumot

            y = drawCenteredAndLineBreakerText(
                    grPage,
                    req.getPostName(),
                    "Cascadia Code",
                    15,
                    y+=20,
                    pageWidth,
                    15);

// qr number info

            drawCenteredText(
                    grPage,
                    req.getQrNumber(),
                    "Arial Unicode MS",
                    20,
                    y+=50,
                    pageWidth);


// qr code info

            drawCenteredImage(grPage, qrBufferedImage, y+=4, pageWidth);

            y += qrBufferedImage.getHeight();

// plate number info

            y = drawCenteredAndLineBreakerText(
                    grPage,
                    req.getPlateNumber(),
                    "Arial Unicode MS",
                    15,
                    y+=20,
                    pageWidth,
                    15);

            y+=10;

// comments info

            List<String> comments = req.getComments();

            grPage.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 15));

            for (String comment : comments) {

                y = drawCenteredAndLineBreakerText(
                        grPage,
                        comment,
                        "Calibri Light",
                        11,
                        y+=6,
                        pageWidth,
                        15);

            }


// created time info

            drawCenteredText(
                    grPage,
                    timeUtil.epochToRegex("dd-MM-yyyy  HH:mm", req.getDate()),
                    "Calibri Light",
                    12,
                    y+=15,
                    pageWidth);


// play market info

            drawImage(grPage, playMarketDownload, 10, y+=25, pageWidth);

            drawText(
                    grPage,
                    "GET IN ON",
                    "Arial Unicode MS",
                    9,
                    52,
                    y+=17,
                    pageWidth);

            drawText(
                    grPage,
                    "GOOGLE PLAY",
                    "Arial Unicode MS",
                    9,
                    52,
                    y+=15,
                    pageWidth);


            y += qrBufferedImage.getHeight();

            ////////////////////////////////////////////////////////////////////////////////


            return Printable.PAGE_EXISTS;

        }, format);

        job.setPageable(book);
    }



    private boolean checkIsPrinterOnline() {

        try {
            Process process = Runtime.getRuntime().exec("wmic printer where Default='TRUE' get WorkOffline");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equalsIgnoreCase("WorkOffline") || line.trim().isEmpty()) continue;

                String status = line.trim();
                return status.equalsIgnoreCase("FALSE"); // FALSE => Online, TRUE => Offline
            }

            reader.close();
            throw new IllegalStateException("Default printer topilmadi.");
        } catch (Exception e) {
            throw new RuntimeException("Printer holatini tekshirishda xatolik: " + e.getMessage());
        }

    }

    private void drawText(Graphics2D g2d, String text, String fontName, int fontSize, int x, int y, int pageWidth) {
        Font font = new Font(fontName, Font.CENTER_BASELINE, fontSize);
        g2d.setFont(font);

//        FontMetrics metrics = g2d.getFontMetrics(font);
//        int textWidth = metrics.stringWidth(text);

//        int x = Math.max((pageWidth - textWidth) / 2, 0);

        g2d.drawString(text, x, y);
    }

    public int drawCenteredAndLineBreakerText(Graphics2D g2d, String text,String fontName, int fontSize, int y, int pageWidth, float margin) {

        Font font = new Font(fontName, Font.PLAIN, fontSize);
        g2d.setFont(font);

        return writeAsLineBreaking(g2d, text, y, pageWidth, font,margin);


    }

    private int writeAsLineBreaking(Graphics2D g2d, String text, int y, int pageWidth, Font font, float margin) {



        FontRenderContext frc = g2d.getFontRenderContext();
        AttributedString attrStr = new AttributedString(text);
        attrStr.addAttribute(TextAttribute.FONT, font);

        AttributedCharacterIterator paragraph = attrStr.getIterator();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);

        float wrappingWidth = (float) (pageWidth - margin*2); // 15px margin each side

        while (lineMeasurer.getPosition() < paragraph.getEndIndex()) {
            TextLayout layout = lineMeasurer.nextLayout(wrappingWidth);
            y += layout.getAscent();
            float drawPosX = (float)(pageWidth - layout.getAdvance()) / 2+margin/2;
            layout.draw(g2d, drawPosX, y);
            y += layout.getDescent() + layout.getLeading();
        }

        return y;

    }

    private void qrCodeInfoENavbat(PrinterJob job, PageFormat format, PrintReqDto req) {

        BufferedImage qrBufferedImage = qrCodeUtil.generate(req.getQrNumber(), 150, 150);

        final int pageWidth = 210; //getPageWidth(80, 203.0);

        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;

            Graphics2D grPage = (Graphics2D) graphics;

            // 180 daraja aylantirish (teskari chiqayotgan bo‘lsa)
            grPage.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            grPage.rotate(Math.toRadians(0), pageFormat.getImageableWidth(), pageFormat.getImageableHeight());


            int y = 30;

// center test info

            drawCenteredText(
                    grPage,
                    "Centered",
                    "Monospaced",
                    20,
                    y+=15,
                    pageWidth);


            y+=20;



// post malumot

            drawCenteredText(
                    grPage,
                    "\"" + req.getPostName() + "\"",
                    "Monospaced",
                    15,
                    y+=30,
                    pageWidth);

            drawCenteredText(
                    grPage,
                    "чегара пости",
                    "Monospaced",
                    15,
                    y+=10,
                    pageWidth);


// qr number info

            drawCenteredText(
                    grPage,
                    req.getQrNumber(),
                    "Bahnschrift Light",
                    22,
                    y+=50,
                    pageWidth);


// qr code info

            drawCenteredImage(grPage, qrBufferedImage, y+=10, pageWidth);

//            grPage.drawImage(qrBufferedImage, 60, y, null); // Centered


            y += qrBufferedImage.getHeight()+10;

// plate number info

            grPage.setFont(new Font("Monospaced", Font.PLAIN, 15));

            grPage.drawString(req.getPlateNumber(), 190, y);
            y += 15;

// comments info

            List<String> comments = req.getComments();

            grPage.setFont(new Font("Monospaced", Font.PLAIN, 15));

            for (String comment : comments) {

                grPage.drawString(comment, 200, y);
                y += 5;

            }



            ////////////////////////////////////////////////////////////////////////////////


            return Printable.PAGE_EXISTS;

        }, format);
    }

    private int getPageWidth(int paperWidthMM, double dpi) {
        int pageWidth = (int) (paperWidthMM * dpi / 25.4); // 80mm in pixels

        return pageWidth;

    }

    private void drawImage(Graphics2D g2d, Image image, int x, int y, int pageWidth) {
        g2d.drawImage(image, x, y, null); // Centered

    }

    public void drawCenteredImage(Graphics2D g2d, Image image, int y, int pageWidth) {

        int imageWidth = image.getWidth(null);

        int x = Math.max((pageWidth - imageWidth) / 2, 0);

        g2d.drawImage(image, x, y, null); // Centered

    }



    public void drawCenteredText(Graphics2D g2d, String text,String fontName, int fontSize, int y, int pageWidth) {

        Font font = new Font(fontName, Font.CENTER_BASELINE, fontSize);
        g2d.setFont(font);

        FontMetrics metrics = g2d.getFontMetrics(font);
        int textWidth = metrics.stringWidth(text);

        int x = Math.max((pageWidth - textWidth) / 2, 0);
        g2d.drawString(text, x, y);

    }


}
