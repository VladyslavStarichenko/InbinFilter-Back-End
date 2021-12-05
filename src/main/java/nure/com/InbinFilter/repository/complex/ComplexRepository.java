package nure.com.InbinFilter.repository.complex;

import nure.com.InbinFilter.models.HouseComplex;
import nure.com.InbinFilter.models.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComplexRepository extends PagingAndSortingRepository<HouseComplex, Long> {

    @Query(value = " FROM HouseComplex c WHERE c.admin = ?1")
    Optional<HouseComplex> getHouseComplexByAdmin(User user);
}
