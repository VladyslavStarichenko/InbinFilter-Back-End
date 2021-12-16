package nure.com.InbinFilter.repository.notification;

import nure.com.InbinFilter.models.Notification;
import nure.com.InbinFilter.models.user.Resident;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NotificationRepository extends PagingAndSortingRepository<Notification,Long> {

    List<Notification> getAllByResident ( Resident resident);


    void deleteAllByResident(Resident resident);
}
