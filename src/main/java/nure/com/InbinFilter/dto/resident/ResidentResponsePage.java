package nure.com.InbinFilter.dto.resident;
import lombok.Data;

import java.util.List;

@Data
public class ResidentResponsePage {
    private List<ResidentGetDto> residentGetDtoList;
    private int pageNumber;
    private int pageSize;
    private int totalElements;
    private int totalPages;

}
