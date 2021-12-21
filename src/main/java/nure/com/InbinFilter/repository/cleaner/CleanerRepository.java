package nure.com.InbinFilter.repository.cleaner;

import nure.com.InbinFilter.models.Flat;
import nure.com.InbinFilter.models.user.Cleaner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CleanerRepository extends PagingAndSortingRepository<Cleaner,Long> {


}
