package uz.gbway.enavbatthermalprintingservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.gbway.enavbatthermalprintingservice.dto.req.print.PrintInvoiceReqDto;
import uz.gbway.enavbatthermalprintingservice.dto.req.print.PrintNewQueueReqDto;
import uz.gbway.enavbatthermalprintingservice.dto.req.print.PrintReqDto;
import uz.gbway.enavbatthermalprintingservice.util.QrCodeUtil;
import uz.gbway.enavbatthermalprintingservice.util.ResourceLoaderUtil;
import uz.gbway.enavbatthermalprintingservice.shablon.Shablon;
import uz.gbway.enavbatthermalprintingservice.util.TimeUtil;

import java.awt.print.*;

@Slf4j
@Service
public class PrintService {
    private final QrCodeUtil qrCodeUtil;
    private final TimeUtil timeUtil;
    private final ResourceLoaderUtil resourceLoaderUtil;
    private final Shablon shablon;

    public PrintService(QrCodeUtil qrCodeUtil, TimeUtil timeUtil, ResourceLoaderUtil resourceLoaderUtil, Shablon shablon) {
        this.qrCodeUtil = qrCodeUtil;
        this.timeUtil = timeUtil;
        this.resourceLoaderUtil = resourceLoaderUtil;
        this.shablon = shablon;
    }

    public int print(PrintReqDto req) {

        try {


            PrinterJob job = PrinterJob.getPrinterJob();


            Paper paper = new Paper();

            double width = 210; // 80mm in points
            double height = 550; // long enough for a receipt

            paper.setSize(width, height);
            paper.setImageableArea(0, 0, width, height); // no margins

            PageFormat format = job.defaultPage();
            format.setPaper(paper);
            format.setOrientation(PageFormat.PORTRAIT);


            shablon.qrCodeInfoTalonShablon(job, format, req);

            job.print(); // avtomatik chiqarish




        } catch (Exception e) {

            log.error(e.getMessage(), e);
            return 500;

        }

        log.info("--> Chop etildi. <--");
        return 200;
    }

    public int printNewQueue(PrintNewQueueReqDto req) {

        try {

            PrinterJob job = PrinterJob.getPrinterJob();

            Paper paper = new Paper();

            double width = 210; // 80mm in points
            double height = 580; // long enough for a receipt

            paper.setSize(width, height);
            paper.setImageableArea(0, 0, width, height); // no margins

            PageFormat format = job.defaultPage();
            format.setPaper(paper);
            format.setOrientation(PageFormat.PORTRAIT);

            shablon.newEQueueInfoShablon(job, format, req);

            job.print(); // avtomatik chiqarish


        } catch (Exception e) {

            log.error(e.getMessage(), e);
            return 500;

        }

        log.info("--> Chop etildi. <--");
        return 200;

    }

    public int printInvoice(PrintInvoiceReqDto req) {
        try {

            PrinterJob job = PrinterJob.getPrinterJob();

            Paper paper = new Paper();

            double width = 210; // 80mm in points
            double height = 580; // long enough for a receipt

            paper.setSize(width, height);
            paper.setImageableArea(0, 0, width, height); // no margins

            PageFormat format = job.defaultPage();
            format.setPaper(paper);
            format.setOrientation(PageFormat.PORTRAIT);

            shablon.invoice(job, format, req);

            job.print(); // avtomatik chiqarish


        } catch (Exception e) {

            log.error(e.getMessage(), e);
            return 500;

        }

        log.info("--> Chop etildi. <--");
        return 200;
    }
}
