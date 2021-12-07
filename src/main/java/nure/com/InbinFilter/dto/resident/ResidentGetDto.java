package nure.com.InbinFilter.dto.resident;

import lombok.Data;

@Data
public class ResidentGetDto {
    private Long id;
    private String name;
    private String address;
    private Double bill;
}
