package nure.com.InbinFilter.service.notofication;

import nure.com.InbinFilter.dto.notification.NotificationGetDto;
import nure.com.InbinFilter.dto.notification.NotificationPageResponse;
import nure.com.InbinFilter.models.Notification;
import nure.com.InbinFilter.models.user.Resident;
import nure.com.InbinFilter.repository.notification.NotificationRepository;
import nure.com.InbinFilter.security.service.UserServiceSCRT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository notificationRepository;
    private final UserServiceSCRT userServiceSCRT;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, UserServiceSCRT userServiceSCRT) {
        this.notificationRepository = notificationRepository;
        this.userServiceSCRT = userServiceSCRT;
    }

    @Override
    public Page<Notification> gelAllResidentNotifications(Integer pageNumber, Integer sizeOfPage) {
        Resident resident = userServiceSCRT.getCurrentLoggedInUser().getResident();
        Pageable pageable = PageRequest.of(pageNumber, sizeOfPage);
        return notificationRepository.getAllByResident(pageable,resident);
    }

    @Override
    public void deleteAllNotifications() {
        Resident resident = userServiceSCRT.getCurrentLoggedInUser().getResident();
        notificationRepository.deleteAllByResident(resident);
    }

    @Override
    public Notification createNotification(Notification notification, Resident resident) {
       notification.setResident(resident);
       return notificationRepository.save(notification);
    }

    public NotificationGetDto fromNotification(Notification notification){
        NotificationGetDto notificationGetDto = new NotificationGetDto();
        notificationGetDto.setId(notification.getId());
        notificationGetDto.setResidentName(notification.getResident().getUser().getUserName());
        notificationGetDto.setMessage(notification.getMessage());
        return notificationGetDto;
    }

    public NotificationPageResponse fromPage(Page<NotificationGetDto> notificationPage, List<NotificationGetDto> notificationGetDtos) {
        NotificationPageResponse response = new NotificationPageResponse();
        response.setNotificationGetDtos(notificationGetDtos);
        return response;
    }

    public Notification toNotification(Double bill){
        Notification notification = new Notification();
        notification.setMessage("Your current bill equals: " + bill + " pay it to the end of month");
        return notification;
    }
}
