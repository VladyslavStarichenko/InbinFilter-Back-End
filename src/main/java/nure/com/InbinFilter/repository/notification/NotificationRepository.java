package nure.com.InbinFilter.repository.notification;

import nure.com.InbinFilter.models.Notification;
import nure.com.InbinFilter.models.user.Resident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends PagingAndSortingRepository<Notification,Long> {

    Page<Notification> getAllByResident (Pageable pageable, Resident resident);


    void deleteAllByResident(Resident resident);
}
