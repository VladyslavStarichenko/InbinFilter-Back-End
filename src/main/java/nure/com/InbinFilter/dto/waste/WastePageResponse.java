package nure.com.InbinFilter.dto.waste;

import lombok.Data;
import java.util.List;
@Data
public class WastePageResponse {
    private List<WasteGetDto> wasteGetDtos;
    private int pageNumber;
    private int pageSize;
    private int totalElements;
    private int totalPages;
}
