package nure.com.InbinFilter.dto.waste;

import lombok.Data;
import nure.com.InbinFilter.models.LitterType;
@Data
public class WasteGenericDto {

    private LitterType litterType;
    private Double amountPercent;

}
