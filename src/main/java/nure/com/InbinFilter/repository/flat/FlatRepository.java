package nure.com.InbinFilter.repository.flat;

import nure.com.InbinFilter.models.Flat;
import nure.com.InbinFilter.models.HouseComplex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlatRepository extends PagingAndSortingRepository<Flat, Long> {
    Boolean existsFlatByAddress(String address);

    @Override
    Optional<Flat> findById(Long id);

    Optional<Flat> findByAddress(String address);

    @Override
    Page<Flat> findAll(Pageable pageable);

    Page<Flat> getAllByComplex(Pageable pageable,HouseComplex houseComplex);

    @Query(value = "SELECT * From flat WHERE complex_id IN (SELECT complex_id FROM cleaner_complex WHERE cleaner_id IN(SELECT id FROM cleaners WHERE user_id IN (SELECT id FROM users WHERE user_name =?) )) ", nativeQuery = true)
    List<Flat> findAllFlatsBins (String userName);

}
