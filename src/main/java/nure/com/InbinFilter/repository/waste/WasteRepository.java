package nure.com.InbinFilter.repository.waste;

import nure.com.InbinFilter.models.Waste;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WasteRepository extends PagingAndSortingRepository<Waste, Long> {
}
