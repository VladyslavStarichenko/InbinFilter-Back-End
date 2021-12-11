package nure.com.InbinFilter.service.cleaner;

import nure.com.InbinFilter.models.HouseComplex;
import nure.com.InbinFilter.models.user.Cleaner;

public interface CleanerService {


    void deleteCleaner(Long cleanerId);

    void addCleanerToComplex(HouseComplex houseComplex, Cleaner cleaner);

    void removeCleanerFromComplex(HouseComplex houseComplex, Cleaner cleaner);

    void clean(Long flatId);
}
