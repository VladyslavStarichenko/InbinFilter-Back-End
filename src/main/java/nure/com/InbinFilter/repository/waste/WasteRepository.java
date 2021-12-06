package nure.com.InbinFilter.repository.waste;

import nure.com.InbinFilter.models.Flat;
import nure.com.InbinFilter.models.Waste;
import nure.com.InbinFilter.models.user.Resident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WasteRepository extends PagingAndSortingRepository<Waste, Long> {


    Page<Waste> getAllByResident(Pageable pageable, Resident resident);

    Page<Waste> getAllByResident_FlatOrderByResident(Flat resident_flat, Pageable pageable);

    @Query(value = "SELECT * FROM Waste w where w.resident_id " +
            "IN(SELECT resident_id FROM residents WHERE flat_id " +
            "IN(SELECT flat_id FROM flat f WHERE  f.complex_id " +
            "IN (SELECT complex_id FROM complex c WHERE  c.user_id = ?))) ",
            nativeQuery = true)
    Page<Waste> findAll(Pageable pageable, UUID id);

    @Query(value = "SELECT * FROM Waste w where w.resident_id " +
            "IN(SELECT resident_id FROM residents WHERE flat_id " +
            "IN(SELECT flat_id FROM flat f WHERE  f.complex_id " +
            "IN (SELECT complex_id FROM complex c WHERE  c.user_id = ?))) ",
            nativeQuery = true)
    List<Waste> findAll(UUID user_id);


    List<Waste> findWasteByResident_Flat(Flat resident_flat);

    List<Waste> getAllByResident(Resident resident);


}
