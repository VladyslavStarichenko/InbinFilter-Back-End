package nure.com.InbinFilter.service.flat;

import lombok.extern.slf4j.Slf4j;
import nure.com.InbinFilter.dto.complex.HouseComplexGetDto;
import nure.com.InbinFilter.dto.flat.FlatCreateDto;
import nure.com.InbinFilter.dto.flat.FlatGetDto;
import nure.com.InbinFilter.dto.flat.FlatPageResponse;
import nure.com.InbinFilter.exeption.CustomException;
import nure.com.InbinFilter.models.Flat;
import nure.com.InbinFilter.models.HouseComplex;
import nure.com.InbinFilter.models.user.Status;
import nure.com.InbinFilter.repository.flat.FlatRepository;
import nure.com.InbinFilter.service.cleaner.CleanerServiceImpl;
import nure.com.InbinFilter.service.complex.ComplexServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FlatServiceImpl implements FlatService {

    private final FlatRepository flatRepository;
    private final ModelMapper modelMapper;
    private final ComplexServiceImpl complexService;



    @Autowired
    public FlatServiceImpl(FlatRepository flatRepository, ModelMapper modelMapper,  ComplexServiceImpl complexService) {
        this.flatRepository = flatRepository;
        this.modelMapper = modelMapper;
        this.complexService = complexService;

    }


    @Override
    public Flat createFlat(FlatCreateDto flat) {
        HouseComplex complex = complexService.getComplex();
        if (flatRepository.existsFlatByAddress(flat.getAddress())) {
            throw new CustomException("Flat with same address is already exists", HttpStatus.IM_USED);
        }
        Flat flatToSave = toFlat(flat);
        flatToSave.setStatus(Status.ACTIVE);
        flatToSave.setComplex(complex);
        flatToSave.setBins(new ArrayList<>());
        flatToSave.setResidents(new ArrayList<>());
        return flatRepository.save(flatToSave);
    }

    Page<Flat> getFlatsByComplex(Long id, Integer pageNumber, Integer sizeOfPage){
        Pageable pageable = PageRequest.of(pageNumber,sizeOfPage);
        HouseComplex houseComplex = complexService.getComplexById(id);
        return flatRepository.getAllByComplex(pageable, houseComplex);
    }

    @Override
    public void deleteFlatById(Long id) {
        Optional<Flat> flatById = flatRepository.findById(id);
        flatById.ifPresent(flatRepository::delete);
    }

    @Override
    public Flat updateFlat(Long id, String newAddress) {
        Optional<Flat> flatById = flatRepository.findById(id);
        if (flatById.isPresent()) {
            Flat flat = flatById.get();
            flat.setAddress(newAddress);
            return flatRepository.save(flat);
        }
        throw new CustomException("There is no flat with id: " + id, HttpStatus.NOT_FOUND);
    }

    @Override
    public Page<Flat> getAllFlats(int pageNumber, int sizeOfPage,String sortBy) {
        if(sortBy == null){
            sortBy = "address";
        }
        Pageable pageable = PageRequest.of(pageNumber,sizeOfPage, Sort.by(Sort.Order.asc(sortBy)));
        return flatRepository.findAll(pageable);
    }




    @Override
    public Flat getFlatById(Long id) {
        if(flatRepository.findById(id).isPresent()){
            return flatRepository.findById(id).get();
        }
        throw new CustomException("There is no flat with specified id", HttpStatus.NOT_FOUND);
    }

    public Flat getFlatByAddress(String address){
        if(flatRepository.findByAddress(address).isPresent()){
            return flatRepository.findByAddress(address).get();
        }
        throw new CustomException("There is no flat with specified address", HttpStatus.NOT_FOUND);
    }

    public FlatGetDto fromFlat(Flat flat) {
        FlatGetDto flatGetDto = modelMapper.map(flat, FlatGetDto.class);
        flatGetDto.setFlatId(flat.getId());
        return flatGetDto;
    }

    public Flat toFlat(FlatCreateDto flatCreateDto) {
        return modelMapper.map(flatCreateDto, Flat.class);
    }

    public FlatPageResponse fromPage(Page<FlatGetDto> flatPage, List<FlatGetDto> flats, String sortedBy) {
        FlatPageResponse response = modelMapper.map(flatPage,FlatPageResponse.class);
        if(sortedBy == null){
            sortedBy = "address";
        }
        response.setPageSize(flatPage.getSize());
        response.setFlats(flats);
        response.setSortedBy(sortedBy);
        return response;
    }

    public HouseComplexGetDto fromHouseComplex(HouseComplex houseComplex) {
        HouseComplexGetDto houseComplexGetDto = modelMapper.map(houseComplex, HouseComplexGetDto.class);
        houseComplexGetDto.setFlats(houseComplex.getFlats().stream()
                .map(this::fromFlat)
                .collect(Collectors.toList()));
        houseComplexGetDto.setCleaners(houseComplex.getCleaners().stream()
        .map(CleanerServiceImpl::fromCleaner)
        .collect(Collectors.toList()));
        return houseComplexGetDto;
    }


}
