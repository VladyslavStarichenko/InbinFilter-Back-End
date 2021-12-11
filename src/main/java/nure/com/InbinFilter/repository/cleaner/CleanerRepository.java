package nure.com.InbinFilter.repository.cleaner;

import nure.com.InbinFilter.models.user.Cleaner;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CleanerRepository extends PagingAndSortingRepository<Cleaner,Long> {

}
