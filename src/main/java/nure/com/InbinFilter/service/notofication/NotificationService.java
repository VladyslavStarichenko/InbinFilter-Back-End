package nure.com.InbinFilter.service.notofication;

import nure.com.InbinFilter.models.Notification;
import nure.com.InbinFilter.models.user.Resident;
import org.springframework.data.domain.Page;

public interface NotificationService {
    Page<Notification> gelAllResidentNotifications(Integer pageNumber, Integer sizeOfPage);

    void deleteAllNotifications();

    Notification createNotification(Notification notification, Resident resident);
}
