package uz.gbway.enavbatthermalprintingservice.service;

import org.springframework.stereotype.Service;
import uz.gbway.enavbatthermalprintingservice.dto.req.print.PrintReqDto;

import java.awt.*;
import java.awt.print.*;
import java.time.LocalDateTime;

@Service
public class PrintService {
    public int print(PrintReqDto req) {

        try {

            PrinterJob job = PrinterJob.getPrinterJob();

//            String content = """
//                            Confirmation
//                    ----------------------------
//                    Post: Yallama
//                    Queue Number: EN02072500001
//                    FIO: Jayxunbey Muxammadov
//                    Price: 15 000 so‘m
//
//
//                        Uzbekistan - Tashkent
//                    """;


            Paper paper = new Paper();
            double width = 227; // 80mm in points
            double height = 800; // long enough for a receipt
            paper.setSize(width, height);
            paper.setImageableArea(0, 0, width, height); // no margins

            PageFormat format = job.defaultPage();
            format.setPaper(paper);
            format.setOrientation(PageFormat.PORTRAIT);


            job.setPrintable((graphics, pageFormat, pageIndex) -> {
                if (pageIndex > 0) return Printable.NO_SUCH_PAGE;

//                graphics.setFont(new Font("Monospaced", Font.PLAIN, 10));
//                int y = 1;

//                graphics.drawString("Chek E-Navbat", 15, y); y += 20;
//                graphics.drawString("Maxsulot: " + req.getProduct(), 15, y); y += 20;
//                graphics.drawString("Narx: " + req.getPrice(), 15, y); y += 20;
//                graphics.drawString("Sana: " + LocalDateTime.now(), 15, y);

                Graphics2D g2d = (Graphics2D) graphics;

                // 180 daraja aylantirish (teskari chiqayotgan bo‘lsa)
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g2d.rotate(Math.toRadians(0), pageFormat.getImageableWidth() , pageFormat.getImageableHeight() / 2);

                g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));

                int y = 10;


                g2d.drawString("Xorazm E-Navbat", 10, y); y += 15;
                g2d.drawLine(0, y, 10, y); y += 15;
                g2d.drawString("Número: L256-180310-1", -50, y); y += 15;
                g2d.drawString("Fecha: 30 Marzo 2018 11:00", 10, y); y += 15;
                g2d.drawString("Cliente: Test", 0, y); y += 15;
                g2d.drawString("Total: 15 EUR", -100, y); y += 15;
                g2d.drawString("Estado: PAGADO", 50, y); y += 15;

                return Printable.PAGE_EXISTS;

            },format);

            job.print(); // avtomatik chiqarish

        } catch (PrinterException e) {
            return 500;
        }


        return 200;
    }
}
