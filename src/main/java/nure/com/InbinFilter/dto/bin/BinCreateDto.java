package nure.com.InbinFilter.dto.bin;

import lombok.Data;
import nure.com.InbinFilter.models.LitterType;

@Data
public class BinCreateDto {
    private LitterType litterType;
    private Integer capacity;

}
