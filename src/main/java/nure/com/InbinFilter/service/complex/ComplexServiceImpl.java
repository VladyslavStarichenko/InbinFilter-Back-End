package nure.com.InbinFilter.service.complex;

import lombok.extern.slf4j.Slf4j;
import nure.com.InbinFilter.exeption.CustomException;
import nure.com.InbinFilter.models.Flat;
import nure.com.InbinFilter.models.HouseComplex;
import nure.com.InbinFilter.models.user.Cleaner;
import nure.com.InbinFilter.models.user.User;
import nure.com.InbinFilter.repository.complex.ComplexRepository;
import nure.com.InbinFilter.security.service.UserServiceSCRT;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ComplexServiceImpl implements ComplexService {

    @Autowired
    private final ComplexRepository complexRepository;
    private final UserServiceSCRT userServiceSCRT;
    private final ModelMapper modelMapper;


    public ComplexServiceImpl(ComplexRepository complexRepository, ComplexRepository complexRepository1, UserServiceSCRT userServiceSCRT, ModelMapper modelMapper){
        this.complexRepository = complexRepository1;
        this.userServiceSCRT = userServiceSCRT;
        this.modelMapper = modelMapper;

    }

    @Override
    public HouseComplex getComplex() {
        User user = userServiceSCRT.getCurrentLoggedInUser();
        HouseComplex complex = getComplex(user);
        if (complex != null) {
            return complex;
        }
        log.warn("IN getComplex");
        throw new CustomException("There is no house complex for logged in user", HttpStatus.NOT_FOUND);
    }

    public HouseComplex getComplex(User user) {
        if (complexRepository.getHouseComplexByAdmin(user).isPresent() &&
                user.getRole().getName().equals("ROLE_ADMIN")) {
            log.info("IN getComplex: was found complex with id {}", user.getHouseComplex().getId());
            return complexRepository.getHouseComplexByAdmin(user).get();
        }
        return null;
    }

    public void addFlat(Flat flat, HouseComplex houseComplex) {
        List<Flat> houseComplexFlats = houseComplex.getFlats();
        if (houseComplexFlats.contains(flat)) {
            log.warn("IN addFlat: fail to add flat with id{} to collection", flat.getId());
            throw new CustomException("This flat is already added", HttpStatus.IM_USED);
        }
        houseComplexFlats.add(flat);
        log.info("IN addFlat: flat with id {} was successfully added to complex with id {}", flat.getId(), houseComplex.getId());
    }

    public void removeFlat(Flat flat, HouseComplex houseComplex) {
        List<Flat> houseComplexFlats = houseComplex.getFlats();
        if (houseComplexFlats.contains(flat)) {
            log.warn("IN removeFlat: fail to remove flat with id{} from collection", flat.getId());
            throw new CustomException("This flat is not belong to complex", HttpStatus.NOT_FOUND);
        }
        houseComplex.getFlats().remove(flat);
        log.info("IN removeFlat: flat with id {} was successfully removed from complex with id {}", flat.getId(), houseComplex.getId());
    }

    public void addCleaner(Cleaner cleaner, HouseComplex houseComplex) {
        List<Cleaner> houseComplexCleaners = houseComplex.getCleaners();
        if (houseComplexCleaners.contains(cleaner)) {
            log.warn("IN addCleaner: fail to add cleaner with id{} to collection", cleaner.getId());
            throw new CustomException("This cleaner is already added", HttpStatus.IM_USED);
        }
        log.info("IN addCleaner: cleaner with id {} was successfully added to complex with id {}", cleaner.getId(), houseComplex.getId());
        houseComplex.getCleaners().add(cleaner);
    }

    public void removeCleaner(Cleaner cleaner, HouseComplex houseComplex) {
        List<Cleaner> houseComplexCleaners = houseComplex.getCleaners();
        if (houseComplexCleaners.contains(cleaner)) {
            log.warn("IN removeCleaner: fail to remove cleaner with id{} from collection", cleaner.getId());
            throw new CustomException("This cleaner is not belong to complex", HttpStatus.NOT_FOUND);
        }
        houseComplex.getCleaners().remove(cleaner);
        log.info("IN removeCleaner: cleaner with id {} was successfully removed from complex with id {}", cleaner.getId(), houseComplex.getId());
    }




}
