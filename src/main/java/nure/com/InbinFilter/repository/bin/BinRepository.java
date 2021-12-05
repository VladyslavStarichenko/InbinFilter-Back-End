package nure.com.InbinFilter.repository.bin;

import nure.com.InbinFilter.models.Bin;
import nure.com.InbinFilter.models.Flat;
import nure.com.InbinFilter.models.LitterType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BinRepository extends PagingAndSortingRepository<Bin, Long> {

    List<Bin> findAllByFlat_Id(Long id);


    List<Bin> getAllByLitterTypeAndFlat(LitterType litterType, Flat flat);
}
