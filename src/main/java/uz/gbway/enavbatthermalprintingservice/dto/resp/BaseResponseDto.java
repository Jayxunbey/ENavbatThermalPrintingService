package uz.gbway.enavbatthermalprintingservice.dto.resp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponseDto {
    private Integer resultCode;
    private String resultNote;
}
