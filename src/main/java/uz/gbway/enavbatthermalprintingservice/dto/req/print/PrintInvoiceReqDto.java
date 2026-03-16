package uz.gbway.enavbatthermalprintingservice.dto.req.print;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PrintInvoiceReqDto {
    private String postName;
    private String queueNumber;
    private String plateNumber;
    private String queue;
    private List<String> queueComments;
    private String arrivalTime;
    private List<String> arrivalTimeComments;
}

