package nure.com.InbinFilter.service.bin;

import nure.com.InbinFilter.dto.bin.BinCreateDto;
import nure.com.InbinFilter.models.Bin;
import nure.com.InbinFilter.models.LitterType;

import java.util.List;

public interface BinService {
    Bin createBin(BinCreateDto binCreateDto, Long flatId);

    void deleteBinById(Long id);

    Bin updateBin(Long id, Integer capacity, LitterType litterType, Long flatId);

    List<Bin> getAllBins(Long flatId);



}
