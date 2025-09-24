package uz.gbway.enavbatthermalprintingservice.shablon;

import org.springframework.stereotype.Component;
import uz.gbway.enavbatthermalprintingservice.dto.req.print.PrintNewQueueReqDto;
import uz.gbway.enavbatthermalprintingservice.dto.req.print.PrintReqDto;
import uz.gbway.enavbatthermalprintingservice.util.QrCodeUtil;
import uz.gbway.enavbatthermalprintingservice.util.ResourceLoaderUtil;
import uz.gbway.enavbatthermalprintingservice.util.ShablonUtil;
import uz.gbway.enavbatthermalprintingservice.util.TimeUtil;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.List;

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
                    pageWidth);


// qr code info

            shablonUtil.drawCenteredImage(grPage, queueNumberQrCode, y += 10, pageWidth);

            y += queueNumberQrCode.getHeight();

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

// Queue info

            y = shablonUtil.drawCenteredAndLineBreakerText(
                    grPage,
                    req.getQueue(),
                    "Arial Unicode MS",
                    35,
                    y += 20,
                    pageWidth,
                    15);

            y += 10;

// comments info

            java.util.List<String> comments = req.getQueueComments();

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


// Preliminary time info

            shablonUtil.drawCenteredText(
                    grPage,
                    timeUtil.epochToRegex("dd-MM-yyyy  HH:mm", req.getArrivalTime()),
                    "Calibri Light",
                    12,
                    y += 15,
                    pageWidth);



// TODO Preliminary time comments

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
}

