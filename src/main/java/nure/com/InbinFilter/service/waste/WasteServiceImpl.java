package nure.com.InbinFilter.service.waste;

import nure.com.InbinFilter.dto.waste.WasteGenericDto;
import nure.com.InbinFilter.dto.waste.WasteGetDto;
import nure.com.InbinFilter.dto.waste.WastePageResponse;
import nure.com.InbinFilter.exeption.CustomException;
import nure.com.InbinFilter.models.*;
import nure.com.InbinFilter.models.user.Resident;
import nure.com.InbinFilter.models.user.User;
import nure.com.InbinFilter.repository.LitterRepository;
import nure.com.InbinFilter.repository.flat.FlatRepository;
import nure.com.InbinFilter.repository.waste.WasteRepository;
import nure.com.InbinFilter.security.service.UserServiceSCRT;
import nure.com.InbinFilter.service.bin.BinServiceImpl;
import nure.com.InbinFilter.service.resident.ResidentServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WasteServiceImpl implements WasteService {

    private final WasteRepository wasteRepository;
    private final ResidentServiceImpl residentServiceImpl;
    private final LitterRepository litterRepository;
    private final BinServiceImpl binServiceImpl;
    private final FlatRepository flatRepository;
    private final UserServiceSCRT userServiceSCRT;
    private final ModelMapper modelMapper;


    @Autowired
    public WasteServiceImpl(WasteRepository wasteRepository, ResidentServiceImpl residentServiceImpl, LitterRepository litterRepository, BinServiceImpl binServiceImpl, FlatRepository flatRepository, UserServiceSCRT userServiceSCRT, ModelMapper modelMapper) {
        this.wasteRepository = wasteRepository;
        this.residentServiceImpl = residentServiceImpl;
        this.litterRepository = litterRepository;
        this.binServiceImpl = binServiceImpl;
        this.flatRepository = flatRepository;
        this.userServiceSCRT = userServiceSCRT;
        this.modelMapper = modelMapper;
    }


    @Override
    public Page<Waste> getAllWastesByResident(Integer pageNumber, Integer sizeOfPage, Long residentId) {
        Pageable pageable = PageRequest.of(pageNumber, sizeOfPage);
        Resident resident = residentServiceImpl.getResidentById(residentId);
        return wasteRepository.getAllByResident(pageable, resident);
    }

    @Override
    public Waste commitWaste(LitterType litterType, Double amount) {
        Resident resident = residentServiceImpl.getResidentAccount();
        Waste waste = new Waste();
        Bin bin = new Bin();
        Litter litter = new Litter();
        Optional<Litter> litterDb = litterRepository.findByLitterType(litterType.name());
        Optional<Bin> binDb = binServiceImpl.getAllBins(resident.getFlat().getId()).stream()
                .filter(b -> b.getLitterType() == litterType)
                .findFirst();

        if (binDb.isPresent()) {
            bin = binDb.get();
        }
        if (litterDb.isPresent()) {
            litter = litterDb.get();
        }
        if(ifIsFullBin(bin,amount)){
            throw new CustomException("Bin is full", HttpStatus.BAD_REQUEST);
        }
        waste.setAmount(amount);
        waste.setBin(bin);
        waste.setLitter(litter);
        waste.setResident(resident);
        residentServiceImpl.updateResidentBill(litter.getPrice() * amount, resident);
        binServiceImpl.wasteBin(bin,amount);
        return wasteRepository.save(waste);
    }

    private Boolean ifIsFullBin(Bin bin, Double amount){
        return bin.isFull() && bin.getCapacity() - amount < 0;
    }

    @Override
    public Page<Waste> getAllWastesPageByFlat(Integer pageNumber, Integer sizeOfPage, String flatAddress) {
        Pageable pageable = PageRequest.of(pageNumber, sizeOfPage);
        Optional<Flat> flatDb = flatRepository.findByAddress(flatAddress);
        Flat flat = new Flat();
        if (flatDb.isPresent()) {
            flat = flatDb.get();
        }
        return wasteRepository.getAllByResident_FlatOrderByResident(flat, pageable);
    }

    @Override
    public List<Waste> getAllWastesByResident(User user) {
        Resident resident = residentServiceImpl.getResidentById(user.getResident().getId());
        return wasteRepository.getAllByResident(resident);
    }

    @Override
    public Page<Waste> getAllWastes(Integer pageNumber, Integer sizeOfPage) {
        Pageable pageable = PageRequest.of(pageNumber, sizeOfPage);
        User currentLoggedInUser = userServiceSCRT.getCurrentLoggedInUser();
        return wasteRepository.findAll(pageable,currentLoggedInUser.getId());
    }

    @Override
    public List<Waste> getAllWastesByFlat(String flatAddress) {
        Optional<Flat> flatDb = flatRepository.findByAddress(flatAddress);
        Flat flat = new Flat();
        if(flatDb.isPresent()){
            flat = flatDb.get();
            return wasteRepository.findWasteByResident_Flat(flat);
        }
        throw new CustomException("There is no flat by specified address", HttpStatus.BAD_REQUEST);

    }

    public WasteGetDto fromWaste(Waste waste){
        WasteGetDto wasteGetDto = new WasteGetDto();
        wasteGetDto.setId(waste.getId());
        wasteGetDto.setLitterType(waste.getLitter().getLitterType());
        wasteGetDto.setPriceToPay(waste.getAmount()*waste.getLitter().getPrice());
        wasteGetDto.setResidentName(waste.getResident().getUser().getUserName());
        wasteGetDto.setAmount(waste.getAmount());
        return wasteGetDto;
    }

    public WastePageResponse fromPage(Page<WasteGetDto> wastePageResponses, List<WasteGetDto> wasteGetDtos) {
        WastePageResponse response = new WastePageResponse();
        response.setWasteGetDtos(wasteGetDtos);
        return response;
    }

    public WasteGenericDto getWastesByLitterType(List<Waste> wastes, LitterType litterType){
        List<Waste> collectByLitterType = wastes.stream()
                .filter(w -> w.getLitter().getLitterType() == litterType)
                .collect(Collectors.toList());
        if(collectByLitterType.size() == 0){
            WasteGenericDto wasteGenericDto = new WasteGenericDto();
            wasteGenericDto.setLitterType(litterType);
            wasteGenericDto.setAmountPercent(0.0);
            return wasteGenericDto;

        }

        List<Double> sum = collectByLitterType.stream().map(Waste::getAmount)
                .collect(Collectors.toList());

        List<Double> totalSum = wastes.stream().map(Waste::getAmount)
                .collect(Collectors.toList());

        Double resultAmount = sum.stream().reduce(Math.round(0*100.0)/100.0, Double::sum);

        Double resultTotalAmount = totalSum.stream().reduce(Math.round(0*100.0)/100.0, Double::sum);

        WasteGenericDto wasteGenericDto = new WasteGenericDto();
        wasteGenericDto.setLitterType(litterType);
        double percent = ((double) resultAmount*100)/ ((double)resultTotalAmount);
        wasteGenericDto.setAmountPercent(Math.round(percent*100.0)/100.0);
        return wasteGenericDto;

    }

    public Double getTotalBill(List<WasteGetDto> wastes){
        List<Double> bills = wastes.stream()
                .map(WasteGetDto::getPriceToPay)
                .collect(Collectors.toList());

        return bills.stream().reduce((double)0, Double::sum);
    }


}
