package uz.gbway.enavbatthermalprintingservice.dto.req.print;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PrintInvoiceReqDto {
    private String mainCaption;
    private List<RowValues> rowValues;

    private String arrivalTime;
    private List<String> arrivalTimeComments;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RowValues {
        private String keyText;
        private String valueText;
    }

}

