package nure.com.InbinFilter.dto.waste;

import lombok.Data;
import nure.com.InbinFilter.models.Bin;
import nure.com.InbinFilter.models.LitterType;

@Data
public class WasteGetDto {
    private Long id;
    private String residentName;
    private Bin bin;
    private LitterType litterType;
    private Integer amount;

}
