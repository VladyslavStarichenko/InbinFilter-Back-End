package nure.com.InbinFilter.dto.complex;


import lombok.Data;
import nure.com.InbinFilter.models.HouseComplex;
import nure.com.InbinFilter.models.user.Status;
import nure.com.InbinFilter.models.user.User;

import java.util.ArrayList;

@Data
public class ComplexCreateDto {

    private String name;

    public static HouseComplex fromDto(ComplexCreateDto complexCreateDto, User user){
        HouseComplex houseComplex = new HouseComplex();
        houseComplex.setAdmin(user);
        houseComplex.setName(complexCreateDto.name);
        houseComplex.setCleaners(new ArrayList<>());
        houseComplex.setFlats(new ArrayList<>());
        houseComplex.setStatus(Status.ACTIVE);
        return houseComplex;
    }

}
