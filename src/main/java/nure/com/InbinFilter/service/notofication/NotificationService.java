package nure.com.InbinFilter.service.notofication;

import nure.com.InbinFilter.models.Notification;
import nure.com.InbinFilter.models.user.Resident;

import java.util.List;
public interface NotificationService {
    List<Notification> gelAllResidentNotifications();

    void deleteAllNotifications();

    Notification createNotification(Notification notification, Resident resident);
}
