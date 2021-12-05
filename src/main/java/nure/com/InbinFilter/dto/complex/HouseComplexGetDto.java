package nure.com.InbinFilter.dto.complex;

import lombok.Data;
import nure.com.InbinFilter.dto.flat.FlatGetDto;
import nure.com.InbinFilter.models.user.Cleaner;

import java.util.List;

@Data
public class HouseComplexGetDto {

    private Long id;
    private String name;
    private String author;
    private List<FlatGetDto> flats;
    private List<Cleaner> cleaners;

}
