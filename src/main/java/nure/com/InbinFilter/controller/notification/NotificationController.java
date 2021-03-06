package nure.com.InbinFilter.controller.notification;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import nure.com.InbinFilter.dto.notification.NotificationGetDto;
import nure.com.InbinFilter.models.Notification;
import nure.com.InbinFilter.models.user.Resident;
import nure.com.InbinFilter.service.notofication.NotificationServiceImpl;
import nure.com.InbinFilter.service.resident.ResidentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notifications")
@Api(value = "Operations with exercises")
@CrossOrigin(origins = {"http://localhost:3000", "http://someserver:8000"},
        methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST},
        allowCredentials = "true", maxAge = 3600, allowedHeaders = "*")
public class NotificationController {


    private final NotificationServiceImpl notificationServiceImpl;
    private final ResidentServiceImpl residentServiceImpl;

    @Autowired
    public NotificationController(NotificationServiceImpl notificationServiceImpl, ResidentServiceImpl residentServiceImpl) {
        this.notificationServiceImpl = notificationServiceImpl;
        this.residentServiceImpl = residentServiceImpl;
    }

    @ApiOperation(value = "Create Notifications for debtors")
    @PreAuthorize("hasRole('ROLE_COMPLEX_ADMIN')")
    @PostMapping("/{flatId}")
    public ResponseEntity<String> notificateDebtors(@ApiParam(value = "Flat id") @PathVariable Long flatId) {
        List<Resident> debtors = residentServiceImpl.getAllDebtorsByFlatList(flatId);
        List<Notification> collect = debtors
                .stream().map(resident ->
                        notificationServiceImpl.createNotification(notificationServiceImpl.toNotification(resident.getBill()), resident))
                .collect(Collectors.toList());
        return new ResponseEntity<>("Notifications was successfully sent ", HttpStatus.CREATED);
    }


    @ApiOperation(value = "Get My Notifications")
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    @GetMapping("myNotifications")
    public ResponseEntity<List<NotificationGetDto>> getMyWasteStatistics() {

        List<Notification> allResidentNotifications = notificationServiceImpl.gelAllResidentNotifications();
        List<NotificationGetDto> response = allResidentNotifications
                .stream()
                .map(notificationServiceImpl::fromNotification)
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
