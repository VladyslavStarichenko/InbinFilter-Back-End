package nure.com.InbinFilter.service.cleaner;

import nure.com.InbinFilter.dto.cleaner.CleanerGetDto;
import nure.com.InbinFilter.exeption.CustomException;
import nure.com.InbinFilter.models.Bin;
import nure.com.InbinFilter.models.HouseComplex;
import nure.com.InbinFilter.models.user.Cleaner;
import nure.com.InbinFilter.repository.bin.BinRepository;
import nure.com.InbinFilter.repository.cleaner.CleanerRepository;
import nure.com.InbinFilter.repository.complex.ComplexRepository;
import nure.com.InbinFilter.service.bin.BinServiceImpl;
import nure.com.InbinFilter.service.flat.FlatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CleanerServiceImpl implements CleanerService {


    private final BinServiceImpl binService;
    private final FlatServiceImpl flatService;
    private final BinRepository binRepository;
    private final ComplexRepository complexRepository;
    private final CleanerRepository cleanerRepository;

    @Autowired
    public CleanerServiceImpl(BinServiceImpl binService, FlatServiceImpl flatService, BinRepository binRepository, ComplexRepository complexRepository, CleanerRepository cleanerRepository) {
        this.binService = binService;
        this.flatService = flatService;
        this.binRepository = binRepository;
        this.complexRepository = complexRepository;
        this.cleanerRepository = cleanerRepository;
    }

    @Override
    public void deleteCleaner(Long cleanerId) {
        Optional<Cleaner> cleaner = cleanerRepository.findById(cleanerId);
        if (cleaner.isPresent()){
            cleanerRepository.delete(cleaner.get());
        }else {
            throw new CustomException("There is no cleaners with specified id", HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public void addCleanerToComplex(HouseComplex houseComplex, Cleaner cleaner) {
        List<Cleaner> cleaners = houseComplex.getCleaners();
        cleaners.add(cleaner);
        houseComplex.setCleaners(cleaners);
        complexRepository.save(houseComplex);
    }

    @Override
    public void removeCleanerFromComplex(HouseComplex houseComplex, Cleaner cleaner) {
        List<Cleaner> cleaners = houseComplex.getCleaners();
        cleaners.remove(cleaner);
        houseComplex.setCleaners(cleaners);
        complexRepository.save(houseComplex);

    }


    @Override
    public void clean(Long id) {
        Optional<Bin> bin = binRepository.findById(id);

        if(bin.isPresent()){

            Bin binDb = bin.get();
            binDb.setCapacity(binDb.getCapacity()+binDb.getFill());
            binDb.setFull(false);
            binDb.setFill(0.0);

            binRepository.save(binDb);
        }

    }

    public static CleanerGetDto fromCleaner(Cleaner cleaner) {
        CleanerGetDto cleanerGetDto = new CleanerGetDto();
        cleanerGetDto.setId(cleaner.getId());
        cleanerGetDto.setName(cleaner.getUser().getUserName());
        return cleanerGetDto;
    }


}
