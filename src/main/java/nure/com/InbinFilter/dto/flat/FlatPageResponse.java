package nure.com.InbinFilter.dto.flat;

import lombok.Data;

import java.util.List;

@Data
public class FlatPageResponse {
    List<FlatGetDto> flats;
    private int pageNumber;
    private int pageSize;
    private String sortedBy;
    private int totalElements;
    private int totalPages;
}
