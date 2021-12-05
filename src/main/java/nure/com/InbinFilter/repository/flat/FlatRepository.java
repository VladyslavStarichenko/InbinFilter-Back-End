package nure.com.InbinFilter.repository.flat;

import nure.com.InbinFilter.models.Flat;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlatRepository extends PagingAndSortingRepository<Flat, Long> {
}
