package nure.com.InbinFilter.repository.resident;

import nure.com.InbinFilter.models.Flat;
import nure.com.InbinFilter.models.user.Resident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResidentRepository extends PagingAndSortingRepository<Resident, Long> {
//    Boolean existsResidentByFlat_Address(String address);

    @Override
    Optional<Resident> findById(Long id);

    Page<Resident> findAllByFlat(Pageable pageable, Flat flat);

    Optional<Resident> findResidentByUser_UserName(String userName);
}
