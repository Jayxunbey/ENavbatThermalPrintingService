package uz.gbway.enavbatthermalprintingservice.dto.resp.error;

import lombok.*;
import uz.gbway.enavbatthermalprintingservice.dto.resp.BaseResponseDto;


import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FieldErrorRespDto extends BaseResponseDto {
    public Map<String, String> error;
}

