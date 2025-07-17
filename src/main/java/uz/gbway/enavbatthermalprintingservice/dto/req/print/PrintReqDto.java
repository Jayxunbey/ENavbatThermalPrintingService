package uz.gbway.enavbatthermalprintingservice.dto.req.print;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PrintReqDto {
    private String postName;
    private String qrNumber;
    private String plateNumber;
    private List<String> comments;
    private String date;
}

