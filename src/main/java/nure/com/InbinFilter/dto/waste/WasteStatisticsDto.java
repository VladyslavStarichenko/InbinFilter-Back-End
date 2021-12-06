package nure.com.InbinFilter.dto.waste;
import lombok.Data;

import java.util.List;

@Data
public class WasteStatisticsDto {
    List<WasteGenericDto> wastes;
    Long residentId;
}
