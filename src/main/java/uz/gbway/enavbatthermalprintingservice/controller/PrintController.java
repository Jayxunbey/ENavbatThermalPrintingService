package uz.gbway.enavbatthermalprintingservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.gbway.enavbatthermalprintingservice.dto.CheckRequest;

import java.awt.*;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class PrintController {

    @PostMapping("/print")
    public ResponseEntity<String> printCheck(@RequestBody CheckRequest request) {
        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable((graphics, pageFormat, pageIndex) -> {
                if (pageIndex > 0) return Printable.NO_SUCH_PAGE;

                graphics.setFont(new Font("Monospaced", Font.PLAIN, 12));
                int y = 100;
                graphics.drawString("🧾 Chek", 100, y); y += 20;
                graphics.drawString("Maxsulot: " + request.getProduct(), 100, y); y += 20;
                graphics.drawString("Narx: " + request.getPrice(), 100, y); y += 20;
                graphics.drawString("Sana: " + LocalDateTime.now(), 100, y);

                return Printable.PAGE_EXISTS;
            });

            job.print(); // avtomatik chiqarish

            return ResponseEntity.ok("✅ Chek printerga yuborildi.");
        } catch (PrinterException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Xatolik: " + e.getMessage());
        }
    }
}
