package nure.com.InbinFilter.dto.complex;

import lombok.Data;
import nure.com.InbinFilter.dto.cleaner.CleanerGetDto;
import nure.com.InbinFilter.dto.flat.FlatGetDto;

import java.util.List;

@Data
public class HouseComplexGetDto {

    private Long id;
    private String name;
    private List<FlatGetDto> flats;
    private List<CleanerGetDto> cleaners;

}
