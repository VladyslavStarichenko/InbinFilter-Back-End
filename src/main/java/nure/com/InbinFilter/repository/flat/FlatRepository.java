package nure.com.InbinFilter.repository.flat;

import nure.com.InbinFilter.models.Flat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlatRepository extends PagingAndSortingRepository<Flat, Long> {
    Boolean existsFlatByAddress(String address);

    @Override
    Optional<Flat> findById(Long id);

    Optional<Flat> findByAddress(String address);

    @Override
    Page<Flat> findAll(Pageable pageable);
}
