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


    Boolean existsHouseComplexByName(String name);

    @Query(value = "SELECT * FROM complex WHERE id IN (SELECT complex_id FROM cleaner_complex WHERE cleaner_id IN (SELECT id FROM cleaners WHERE cleaners.user_id IN (SELECT user_id FROM users WHERE user_name= ?)) )", nativeQuery = true)
    HouseComplex getHouseComplexByCleaner(String userName);

    @Query(value = "UPDATE cleaner_complex SET cleaner_id = ? ,complex_id = ? WHERE cleaner_id IN(SELECT id FROM cleaners WHERE user_id IN (SELECT id FRom users Where user_name = ?))", nativeQuery = true)
    void updateCleanerComplex(Long cleaner_id, Long complex_id, String userName);
}
