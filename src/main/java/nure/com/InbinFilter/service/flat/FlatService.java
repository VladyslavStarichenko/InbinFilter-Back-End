package nure.com.InbinFilter.service.flat;

import nure.com.InbinFilter.dto.flat.FlatCreateDto;
import nure.com.InbinFilter.models.Flat;
import org.springframework.data.domain.Page;

public interface FlatService {

    Flat createFlat(FlatCreateDto flat);

    void deleteFlatById(Long id);

    Flat updateFlat(Long id, String newName);

    Page<Flat> getAllFlats(int pageNumber, int sizeOfPage,String sortBy);

    Flat getFlatById(Long id);


}
