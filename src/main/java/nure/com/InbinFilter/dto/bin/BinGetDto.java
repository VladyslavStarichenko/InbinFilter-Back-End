package nure.com.InbinFilter.dto.bin;

import lombok.Data;
import nure.com.InbinFilter.models.LitterType;

@Data
public class BinGetDto {
    private Long id;
    private String address;
    private LitterType litterType;
    private boolean full;
    private Integer fill;
    private Integer capacity;
}
