package nure.com.InbinFilter.service.bin;

import nure.com.InbinFilter.dto.bin.BinCreateDto;
import nure.com.InbinFilter.dto.bin.BinGetDto;
import nure.com.InbinFilter.exeption.CustomException;
import nure.com.InbinFilter.models.Bin;
import nure.com.InbinFilter.models.Flat;
import nure.com.InbinFilter.models.LitterType;
import nure.com.InbinFilter.repository.bin.BinRepository;
import nure.com.InbinFilter.service.flat.FlatServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BinServiceImpl implements BinService {

    private final FlatServiceImpl flatServiceImpl;
    private final BinRepository binRepository;
    private final ModelMapper modelMapper;

    public BinServiceImpl(FlatServiceImpl flatServiceImpl, BinRepository binRepository, ModelMapper modelMapper) {
        this.flatServiceImpl = flatServiceImpl;
        this.binRepository = binRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Bin createBin(BinCreateDto binCreateDto, Long flatId) {
        Flat flat = flatServiceImpl.getFlatById(flatId);
        if (!checkLitterType(binCreateDto.getLitterType(), flat)) {
            Bin bin = new Bin();
            bin.setCapacity(binCreateDto.getCapacity());
            bin.setFlat(flatServiceImpl.getFlatById(flatId));
            bin.setFill(0);
            bin.setLitterType(binCreateDto.getLitterType());
            bin.setFull(false);
            bin.setWastes(new ArrayList<>());
            return binRepository.save(bin);
        } else throw new CustomException("Bin with specified LitterType is already exists", HttpStatus.IM_USED);

    }

    @Override
    public void deleteBinById(Long id) {
        Optional<Bin> binToDelete = binRepository.findById(id);
        if (binToDelete.isPresent()) {
            binRepository.delete(binToDelete.get());
        } else {
            throw new CustomException("There is no bin with specified id", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Bin updateBin(Long id, Integer capacity, LitterType litterType, Long flatId) {
        Flat flat = flatServiceImpl.getFlatById(flatId);
        Optional<Bin> binToUpdate = binRepository.findById(id);
        Bin bin;
        Bin newBin;
        if (binToUpdate.isPresent()) {
          bin = binToUpdate.get();
          if (checkLitterType(bin.getLitterType(), flat)){

              throw new CustomException("Bin with specified LitterType is already exists", HttpStatus.IM_USED);
          }
          else{
              newBin = binToUpdate.get();
              bin.setCapacity(capacity);
              bin.setLitterType(litterType);

          }
        }
        else{
            throw new CustomException("Failed to update bin", HttpStatus.NOT_FOUND);
        }

       return binRepository.save(newBin);

    }
    public void wasteBin(Bin bin, Integer amount){
        bin.setFill(bin.getFill()+amount);
        bin.setCapacity(bin.getCapacity()- bin.getFill());
        binRepository.save(bin);
    }

    public void makeReport(Bin bin){
        if(bin.isFull()||bin.getFill() > bin.getCapacity()/2){
            bin.setFull(true);
            binRepository.save(bin);
        }
        throw new CustomException("Bin is not Full to make a report", HttpStatus.BAD_REQUEST);
    }

    private Boolean checkLitterType(LitterType litterType, Flat flat) {
        return binRepository.getAllByLitterTypeAndFlat(litterType, flat).size() > 0;
    }

    @Override
    public List<Bin> getAllBins(Long flatId) {
        return binRepository.findAllByFlat_Id(flatId);
    }

    public Bin toBin(BinCreateDto binCreateDto) {
        return modelMapper.map(binCreateDto, Bin.class);
    }

    public BinGetDto fromBin(Bin bin) {
        BinGetDto binGetDto = modelMapper.map(bin, BinGetDto.class);
        binGetDto.setAddress(bin.getFlat().getAddress());
        return binGetDto;
    }
}
