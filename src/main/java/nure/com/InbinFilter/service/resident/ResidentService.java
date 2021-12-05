package nure.com.InbinFilter.service.resident;

import nure.com.InbinFilter.dto.resident.ResidentGetDto;
import nure.com.InbinFilter.models.user.Resident;
import org.springframework.data.domain.Page;

public interface ResidentService {

    Page<Resident> getAllResidents(int pageNumber, int sizeOfPage, Long id);

    ResidentGetDto getResidentById(Long id);

    ResidentGetDto getResidentAccount(String userName);
}
