package nure.com.InbinFilter.service.waste;

import nure.com.InbinFilter.models.LitterType;
import nure.com.InbinFilter.models.Waste;
import nure.com.InbinFilter.models.user.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WasteService {

    Page<Waste> getAllWastesByResident(Integer pageNumber, Integer sizeOfPage, Long residentId);

    Waste commitWaste(LitterType litterType, Integer amount);

    Page<Waste> getAllWastesPageByFlat(Integer pageNumber, Integer sizeOfPage, String flatAddress);

    Page<Waste> getAllWastes(Integer pageNumber, Integer sizeOfPage);

    List<Waste> getAllWastesByFlat(String flatAddress);

    List<Waste> getAllWastesByResident(User user);
}
