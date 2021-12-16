package nure.com.InbinFilter.service.resident;

import nure.com.InbinFilter.dto.resident.ResidentGetDto;
import nure.com.InbinFilter.dto.resident.ResidentResponsePage;
import nure.com.InbinFilter.exeption.CustomException;
import nure.com.InbinFilter.models.Flat;
import nure.com.InbinFilter.models.user.Resident;
import nure.com.InbinFilter.models.user.User;
import nure.com.InbinFilter.repository.flat.FlatRepository;
import nure.com.InbinFilter.repository.resident.ResidentRepository;
import nure.com.InbinFilter.security.service.UserServiceSCRT;
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
public class ResidentServiceImpl implements ResidentService {

    private final ResidentRepository residentRepository;
    private final ModelMapper modelMapper;
    private final FlatRepository flatRepository;
    private final UserServiceSCRT userServiceSCRT;


    @Autowired
    public ResidentServiceImpl(ResidentRepository residentRepository, ModelMapper modelMapper, FlatRepository flatRepository, UserServiceSCRT userServiceSCRT) {
        this.residentRepository = residentRepository;
        this.modelMapper = modelMapper;
        this.flatRepository = flatRepository;
        this.userServiceSCRT = userServiceSCRT;
    }


    @Override
    public Page<Resident> getAllResidentsByFlat(int pageNumber, int sizeOfPage, Long id) {
        Pageable pageable = PageRequest.of(pageNumber, sizeOfPage);
        Optional<Flat> flat = flatRepository.findById(id);
        if (flat.isPresent()) {
            return residentRepository.findAllByFlat(pageable, flat.get());
        } else throw new CustomException("There is no flat in the complex with specified id", HttpStatus.NOT_FOUND);

    }

    public Page<Resident> getAllDebtorsByFlat(int pageNumber, int sizeOfPage, Long id) {
        Pageable pageable = PageRequest.of(pageNumber, sizeOfPage);
        Optional<Flat> flat = flatRepository.findById(id);
        if (flat.isPresent()) {
            return residentRepository.findAllDebtors(pageable, flat.get().getId());

        } else throw new CustomException("There is no flat in the complex with specified id", HttpStatus.NOT_FOUND);

    }

    public List<Resident> getAllDebtorsByFlatList(Long flatId) {
        Optional<Flat> flat = flatRepository.findById(flatId);
        if (flat.isPresent()) {
            List<Resident> residents = residentRepository.findAllByFlat_Id(flat.get().getId());
            return residents.stream()
                    .filter(resident -> resident.getBill() > 0)
                    .collect(Collectors.toList());
        } else throw new CustomException("There is no flat in the complex with specified id", HttpStatus.NOT_FOUND);
    }

    public void updateResidentBill(Double bill, Resident resident) {
        resident.setBill(resident.getBill() + bill);
        residentRepository.save(resident);
    }

    @Override
    public Resident getResidentById(Long id) {
        Optional<Resident> residentById = residentRepository.findById(id);
        if (residentById.isPresent()) {
            return residentById.get();
        }
        throw new CustomException("There is no resident with specified id", HttpStatus.NOT_FOUND);
    }


    public Resident getResidentAccount() {
        User user = userServiceSCRT.getCurrentLoggedInUser();
        Optional<Resident> getResidentAccount = residentRepository.findResidentByUser(user.getId());
        if (getResidentAccount.isPresent()) {
            return getResidentAccount.get();
        }
        throw new CustomException("There is no user with specified Name", HttpStatus.NOT_FOUND);
    }

    public static ResidentGetDto fromResident(Resident resident) {
        ResidentGetDto residentGetDto = new ResidentGetDto();
        residentGetDto.setName(resident.getUser().getUserName());
        residentGetDto.setAddress(resident.getFlat().getAddress());
        residentGetDto.setId(resident.getId());
        residentGetDto.setBill(resident.getBill());
        return residentGetDto;

    }

    public ResidentResponsePage fromPage(Page<ResidentGetDto> residentGetDtoPage, List<ResidentGetDto> residentGetDtos) {
        ResidentResponsePage response = modelMapper.map(residentGetDtoPage, ResidentResponsePage.class);
        response.setResidentGetDtoList(residentGetDtos);
        return response;
    }


}
