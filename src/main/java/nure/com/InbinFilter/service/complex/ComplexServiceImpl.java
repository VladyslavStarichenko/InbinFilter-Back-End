package nure.com.InbinFilter.service.complex;

import lombok.extern.slf4j.Slf4j;
import nure.com.InbinFilter.exeption.CustomException;
import nure.com.InbinFilter.models.HouseComplex;
import nure.com.InbinFilter.models.user.Cleaner;
import nure.com.InbinFilter.models.user.User;
import nure.com.InbinFilter.repository.complex.ComplexRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ComplexServiceImpl implements ComplexService {

    @Autowired
    private final ComplexRepository complexRepository;
    private final ModelMapper modelMapper;

    public ComplexServiceImpl(ComplexRepository complexRepository, ModelMapper modelMapper) {
        this.complexRepository = complexRepository;
        this.modelMapper = modelMapper;
    }


//    @Override
//    public HouseComplex getComplex() {
//        User user = userServiceSCRT.getCurrentLoggedInUser();
//        HouseComplex complex = getComplex(user);
//        if (complex != null) {
//            return complex;
//        }
//        log.warn("IN getComplex");
//        throw new CustomException("There is no house complex for logged in user", HttpStatus.NOT_FOUND);
//    }

    public HouseComplex getComplex(User user) {
        if (complexRepository.getHouseComplexByAdmin(user).isPresent() &&
                user.getRole().getName().equals("ROLE_COMPLEX_ADMIN")) {
            log.info("IN getComplex: was found complex with id {}", user.getHouseComplex().getId());
            return complexRepository.getHouseComplexByAdmin(user).get();
        }
        throw new CustomException("There is no complex you're own",HttpStatus.NOT_FOUND);
    }

    public HouseComplex getComplexById(Long id){
        Optional<HouseComplex> complexById = complexRepository.findById(id);
        if(complexById.isPresent()){
            HouseComplex houseComplex = complexById.get();
        }
        throw new CustomException("There is no complex with specified Id", HttpStatus.NOT_FOUND);
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
