package nure.com.InbinFilter.dto.notification;

import lombok.Data;

import java.util.List;

@Data
public class NotificationPageResponse {
    List<NotificationGetDto> notificationGetDtos;
    private int pageNumber;
    private int pageSize;
    private String sortedBy;
    private int totalElements;
    private int totalPages;
}
