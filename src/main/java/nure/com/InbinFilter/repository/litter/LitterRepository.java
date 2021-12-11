package nure.com.InbinFilter.repository.litter;

import nure.com.InbinFilter.models.Litter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface LitterRepository extends PagingAndSortingRepository<Litter,Long> {

    @Query(value = "SELECT DISTINCT * FROM litter l where l.litter_type = ? ",nativeQuery = true)
    Optional<Litter> findByLitterType(String litterType);
}
