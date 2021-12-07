package nure.com.InbinFilter.dto.waste;

import lombok.Data;
import nure.com.InbinFilter.models.LitterType;

@Data
public class WasteCreateDto {
    LitterType litterType;
    Double amount;
}
