package uz.gbway.enavbatthermalprintingservice.shablon;

import org.springframework.stereotype.Component;
import uz.gbway.enavbatthermalprintingservice.dto.req.print.PrintInvoiceReqDto;
import uz.gbway.enavbatthermalprintingservice.dto.req.print.PrintNewQueueReqDto;
import uz.gbway.enavbatthermalprintingservice.dto.req.print.PrintReqDto;
import uz.gbway.enavbatthermalprintingservice.util.QrCodeUtil;
import uz.gbway.enavbatthermalprintingservice.util.ResourceLoaderUtil;
import uz.gbway.enavbatthermalprintingservice.util.ShablonUtil;
import uz.gbway.enavbatthermalprintingservice.util.TimeUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;

@Component
public class Shablon {
    private final QrCodeUtil qrCodeUtil;
    private final ResourceLoaderUtil resourceLoaderUtil;
    private final TimeUtil timeUtil;
    private final ShablonUtil shablonUtil;

    public Shablon(QrCodeUtil qrCodeUtil, ResourceLoaderUtil resourceLoaderUtil, TimeUtil timeUtil, ShablonUtil shablonUtil) {
        this.qrCodeUtil = qrCodeUtil;
        this.resourceLoaderUtil = resourceLoaderUtil;
        this.timeUtil = timeUtil;
        this.shablonUtil = shablonUtil;
    }

    public void newEQueueInfoShablon(PrinterJob job, PageFormat format, PrintNewQueueReqDto req) {

        Book book = new Book();

        BufferedImage queueNumberQrCode = qrCodeUtil.generate(req.getQueueNumber(), 150, 150);

        BufferedImage playMarketDownload = resourceLoaderUtil.loadPlayMarketDownlaodImage();

        // TODO davom qil
        BufferedImage playMarketDownloadQR = qrCodeUtil.generate("https://play.google.com/store/apps/details?id=com.eskishahar.app.enavbat&hl=ru", 120, 120);


        final int pageWidth = 200;


        book.append((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;


            Graphics2D grPage = (Graphics2D) graphics;

            // 180 daraja aylantirish (teskari chiqayotgan bo‘lsa)
            grPage.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            grPage.rotate(Math.toRadians(0), pageFormat.getImageableWidth(), pageFormat.getImageableHeight());


            int y = 0;

// post malumot


            y = shablonUtil.drawCenteredAndLineBreakerText(
                    grPage,
                    req.getPostName(),
                    "Cascadia Code",
                    15,
                    y += 5,
                    pageWidth,
                    15);

// qr number info

            shablonUtil.drawCenteredText(
                    grPage,
                    req.getQueueNumber(),
                    "Arial Unicode MS",
                    20,
                    y += 50,
                    pageWidth + 15);


// qr code info

            shablonUtil.drawCenteredImage(grPage, queueNumberQrCode, y += 10, pageWidth + 15);

            y += queueNumberQrCode.getHeight();

// plate number info

            y = shablonUtil.drawCenteredAndLineBreakerText(
                    grPage,
                    req.getPlateNumber(),
                    "Arial Unicode MS",
                    20,
                    y += 5,
                    pageWidth,
                    15);


// Queue info

            y = shablonUtil.drawCenteredAndLineBreakerText(
                    grPage,
                    req.getQueue(),
                    "Arial Unicode MS",
                    60,
                    y += 5,
                    pageWidth,
                    15);

// queueComments info

            y -= 5;

            java.util.List<String> queueComments = req.getQueueComments();

//            grPage.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 20));

            StringBuilder fullQueueComment = new StringBuilder();

            int isLastQueueCommentPost = queueComments.size() - 1;
            int nowQueueCommentPost = 0;

            for (String queueComment : queueComments) {

                fullQueueComment.append(queueComment);

                if (nowQueueCommentPost++ != isLastQueueCommentPost) {
                    fullQueueComment.append("/");
                }

            }

            y = shablonUtil.drawCenteredAndLineBreakerText(
                    grPage,
                    fullQueueComment.toString(),
                    "Calibri Light",
                    15,
                    y += 0,
                    pageWidth,
                    15);


// Preliminary time info

            shablonUtil.drawCenteredText(
                    grPage,
                    timeUtil.epochToRegex("dd-MM-yyyy  HH:mm", req.getArrivalTime()),
                    "Arial Unicode MS",
                    20,
                    y += 40,
                    pageWidth + 16);

// Preliminary time comment info

            y += 5;

            java.util.List<String> arrivalTimeComments = req.getArrivalTimeComments();

            grPage.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 15));

            for (String arrivalComment : arrivalTimeComments) {

                y = shablonUtil.drawCenteredAndLineBreakerText(
                        grPage,
                        arrivalComment,
                        "Calibri Light",
                        11,
                        y += 1,
                        pageWidth,
                        15);

            }

// Play market info

            int startOfPlayMarketBorderLine = y += 2;

            int x = 10;


            shablonUtil.drawImage(grPage, playMarketDownload, x + 2, y += 27, 40, pageWidth);

            shablonUtil.drawImage(grPage, playMarketDownloadQR, x + 115, y -= 20, 80, pageWidth);

            shablonUtil.drawText(
                    grPage,
                    "E-NAVBAT",
                    "Arial Unicode MS",
                    9,
                    x + 48,
                    y += 28,
                    pageWidth);

            shablonUtil.drawText(
                    grPage,
                    "ILOVASINI",
                    "Arial Unicode MS",
                    9,
                    x + 48,
                    y += 15,
                    pageWidth);

            shablonUtil.drawText(
                    grPage,
                    "YUKLAB OLING!",
                    "Arial Unicode MS",
                    9,
                    x + 48,
                    y += 15,
                    pageWidth);


// line
            shablonUtil.drawLine(
                    grPage,
                    x - 2,
                    startOfPlayMarketBorderLine + 5,
                    x + 187,
                    y - startOfPlayMarketBorderLine + 17

            );


            ////////////////////////////////////////////////////////////////////////////////


            return Printable.PAGE_EXISTS;

        }, format);

        job.setPageable(book);

    }

    public void qrCodeInfoTalonShablon(PrinterJob job, PageFormat format, PrintReqDto req) {

        Book book = new Book();

        BufferedImage qrBufferedImage = qrCodeUtil.generate(req.getQrNumber() + "#" + req.getPlateNumber(), 150, 150);

        BufferedImage playMarketDownload = resourceLoaderUtil.loadPlayMarketDownlaodImage();

        // TODO davom qil
        BufferedImage playMarketDownloadQR = qrCodeUtil.generate("https://play.google.com/store/apps/details?id=com.eskishahar.app.enavbat&hl=ru", 120, 120);


        final int pageWidth = 200;


        book.append((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;


            javax.print.PrintService printService = job.getPrintService();

            Graphics2D grPage = (Graphics2D) graphics;

            // 180 daraja aylantirish (teskari chiqayotgan bo‘lsa)
            grPage.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            grPage.rotate(Math.toRadians(0), pageFormat.getImageableWidth(), pageFormat.getImageableHeight());


            int y = 0;

// post malumot

            y = shablonUtil.drawCenteredAndLineBreakerText(
                    grPage,
                    req.getPostName(),
                    "Cascadia Code",
                    15,
                    y += 5,
                    pageWidth,
                    15);

// qr number info

            shablonUtil.drawCenteredText(
                    grPage,
                    req.getQrNumber(),
                    "Arial Unicode MS",
                    20,
                    y += 50,
                    pageWidth);


// qr code info

            shablonUtil.drawCenteredImage(grPage, qrBufferedImage, y += 10, pageWidth);

            y += qrBufferedImage.getHeight();

// plate number info

            y = shablonUtil.drawCenteredAndLineBreakerText(
                    grPage,
                    req.getPlateNumber(),
                    "Arial Unicode MS",
                    15,
                    y += 20,
                    pageWidth,
                    15);

            y += 10;

// comments info

            java.util.List<String> comments = req.getComments();

            grPage.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 15));

            for (String comment : comments) {

                y = shablonUtil.drawCenteredAndLineBreakerText(
                        grPage,
                        comment,
                        "Calibri Light",
                        11,
                        y += 6,
                        pageWidth,
                        15);

            }


// created time info

            shablonUtil.drawCenteredText(
                    grPage,
                    timeUtil.epochToRegex("dd-MM-yyyy  HH:mm", req.getDate()),
                    "Calibri Light",
                    12,
                    y += 15,
                    pageWidth);


// play market info

            int startOfPlayMarketBorderLine = y += 2;

            int x = 8;


            shablonUtil.drawImage(grPage, playMarketDownload, x + 2, y += 27, 40, pageWidth);

            shablonUtil.drawImage(grPage, playMarketDownloadQR, x + 115, y -= 20, 80, pageWidth);

            shablonUtil.drawText(
                    grPage,
                    "E-NAVBAT",
                    "Arial Unicode MS",
                    9,
                    x + 48,
                    y += 28,
                    pageWidth);

            shablonUtil.drawText(
                    grPage,
                    "ILOVASINI",
                    "Arial Unicode MS",
                    9,
                    x + 48,
                    y += 15,
                    pageWidth);

            shablonUtil.drawText(
                    grPage,
                    "YUKLAB OLING!",
                    "Arial Unicode MS",
                    9,
                    x + 48,
                    y += 15,
                    pageWidth);


// line
            shablonUtil.drawLine(
                    grPage,
                    x - 2,
                    startOfPlayMarketBorderLine + 5,
                    x + 187,
                    y - startOfPlayMarketBorderLine + 17

            );


            ////////////////////////////////////////////////////////////////////////////////


            return Printable.PAGE_EXISTS;

        }, format);

        job.setPageable(book);

    }

    public void invoice(PrinterJob job, PageFormat format, PrintInvoiceReqDto req) {

        Book book = new Book();

        // TODO davom qil
        BufferedImage qrPdfCheckOnlineLink = qrCodeUtil.generate(req.getPdfCheckLink(), 125, 125);

        final int pageWidth = 205;

        book.append((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;


            Graphics2D grPage = (Graphics2D) graphics;

            // 180 daraja aylantirish (teskari chiqayotgan bo‘lsa)
            grPage.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            grPage.rotate(Math.toRadians(0), pageFormat.getImageableWidth(), pageFormat.getImageableHeight());


            int y = 0;

// Caption

            y = shablonUtil.drawCenteredAndLineBreakerText(
                    grPage,
                    "АВТОТУРАРГОҲ УЧУН ТЎЛОВ ВАРАҚАСИ", // TODO caption
                    "Cascadia Code",
                    16,
                    y+=20,
                    pageWidth,
                    15);



// Invoice
            y = shablonUtil.drawSpaceBetweenTextKeyValue(
                    grPage,
                    "Инвойс рақами:",
                    req.getInvoice(),
                    "Arial Unicode MS",
                    11,
                    y+=20,
                    pageWidth,
                    16);

// Splitter
            y+=20;

// Row Values each

            for (PrintInvoiceReqDto.RowValues rowValue : req.getRowValues()) {

                y = shablonUtil.drawSpaceBetweenTextKeyValue(
                        grPage,
                        rowValue.getKeyText()+":",
                        rowValue.getValueText(),
                        "Sitka Heading Semibold",
                        7,
                        y+=5,
                        pageWidth,
                        15);
            }

// Qr Pdf Link

            shablonUtil.drawCenteredImage(grPage, qrPdfCheckOnlineLink, y += 30, pageWidth + 15);

// qr number info

//            shablonUtil.drawText(
//                    grPage,
//                    "YUKLAB OLING!",
//                    "Arial Unicode MS",
//                    9,
//                    x + 48,
//                    y.addAndGet(15),
//                    pageWidth);


// line
//            shablonUtil.drawLine(
//                    grPage,
//                    x - 2,
//                    startOfPlayMarketBorderLine + 5,
//                    x + 187,
//                    y.get() - startOfPlayMarketBorderLine + 17
//
//            );


            ////////////////////////////////////////////////////////////////////////////////


            return Printable.PAGE_EXISTS;

        }, format);

        job.setPageable(book);

    }


}


