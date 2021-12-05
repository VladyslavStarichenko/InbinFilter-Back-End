package nure.com.InbinFilter.service.resident;

import nure.com.InbinFilter.dto.resident.ResidentGetDto;
import nure.com.InbinFilter.dto.resident.ResidentResponsePage;
import nure.com.InbinFilter.exeption.CustomException;
import nure.com.InbinFilter.models.Flat;
import nure.com.InbinFilter.models.user.Resident;
import nure.com.InbinFilter.repository.flat.FlatRepository;
import nure.com.InbinFilter.repository.resident.ResidentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResidentServiceImpl implements ResidentService{

    private final ResidentRepository residentRepository;
    private final ModelMapper modelMapper;
    private final FlatRepository flatRepository;


    @Autowired
    public ResidentServiceImpl(ResidentRepository residentRepository, ModelMapper modelMapper, FlatRepository flatRepository) {
        this.residentRepository = residentRepository;
        this.modelMapper = modelMapper;
        this.flatRepository = flatRepository;
    }


    @Override
    public Page<Resident> getAllResidents(int pageNumber, int sizeOfPage, Long id) {
        Pageable pageable = PageRequest.of(pageNumber,sizeOfPage);
        Optional<Flat> flat = flatRepository.findById(id);
        if(flat.isPresent()){
            return residentRepository.findAllByFlat(pageable,flat.get());
        }
        else throw new CustomException("There is no flat in the complex with specified id", HttpStatus.NOT_FOUND);

    }

    @Override
    public ResidentGetDto getResidentById(Long id) {
        Optional<Resident> residentById = residentRepository.findById(id);
        if(residentById.isPresent()){
            return fromResident(residentById.get());
        }
        throw new CustomException("There is no resident with specified id", HttpStatus.NOT_FOUND);
    }


    public ResidentGetDto getResidentAccount(String userName) {
        Optional<Resident> getResidentAccount = residentRepository.findResidentByUser_UserName(userName);
        if(getResidentAccount.isPresent()){
            return fromResident(getResidentAccount.get());
        }
        throw new CustomException("There is no user with specified Name",HttpStatus.NOT_FOUND);
    }

    public ResidentGetDto fromResident(Resident resident){
        ResidentGetDto residentGetDto = new ResidentGetDto();
        residentGetDto.setName(resident.getUser().getUserName());
        residentGetDto.setAddress(resident.getFlat().getAddress());
        residentGetDto.setId(resident.getId());
        return residentGetDto;

    }

    public ResidentResponsePage fromPage(Page<ResidentGetDto> residentGetDtoPage, List<ResidentGetDto> residentGetDtos) {
        ResidentResponsePage response = modelMapper.map(residentGetDtoPage, ResidentResponsePage.class);
        response.setResidentGetDtoList(residentGetDtos);
        return response;
    }




}
