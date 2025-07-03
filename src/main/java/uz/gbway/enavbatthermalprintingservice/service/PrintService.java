package uz.gbway.enavbatthermalprintingservice.service;

import org.springframework.stereotype.Service;
import uz.gbway.enavbatthermalprintingservice.dto.req.print.PrintReqDto;
import uz.gbway.enavbatthermalprintingservice.util.QrCodeUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.util.List;

@Service
public class PrintService {
    private final QrCodeUtil qrCodeUtil;

    public PrintService(QrCodeUtil qrCodeUtil) {
        this.qrCodeUtil = qrCodeUtil;
    }

    public int print(PrintReqDto req) {

        try {

            PrinterJob job = PrinterJob.getPrinterJob();




            Paper paper = new Paper();

            double width = 210; // 80mm in points
            double height = 1000; // long enough for a receipt

            paper.setSize(width, height);
            paper.setImageableArea(0, 0, width, height); // no margins

            PageFormat format = job.defaultPage();
            format.setPaper(paper);
            format.setOrientation(PageFormat.PORTRAIT);

            qrCodeInfoENavbat(job, format, req);

            job.print(); // avtomatik chiqarish

        } catch (Exception e) {
            return 500;
        }


        return 200;
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
            grPage.setFont(new Font("Monospaced", Font.PLAIN, 20));
            grPage.drawString(req.getQrNumber(), 210, y);
            y += 15;

// qr code info
            grPage.drawImage(qrBufferedImage, 60, y, null); // Centered


            y += 10;

// plate number info
            grPage.setFont(new Font("Monospaced", Font.PLAIN, 15));

            grPage.drawString(req.getPlateNumber(), 211, y);
            y += 15;

// comments info

            List<String> comments = req.getComments();

            grPage.setFont(new Font("Monospaced", Font.PLAIN, 15));

            for (String comment : comments) {

                grPage.drawString(comment, 10, y);
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

    public void drawCenteredText(Graphics2D g2d, String text,String fontName, int fontSize, int y, int pageWidth) {

        Font font = new Font(fontName, Font.PLAIN, fontSize);
        g2d.setFont(font);

        FontMetrics metrics = g2d.getFontMetrics(font);
        int textWidth = metrics.stringWidth(text);

        int x = Math.max((pageWidth - textWidth) / 2, 0);
        g2d.drawString(text, x, y);

    }


}
