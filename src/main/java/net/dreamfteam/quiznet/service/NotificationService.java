package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.Notification;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.web.dto.DtoNotification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NotificationService {
    List<Notification> getUnseenByUserId(String userId);

    Notification getById(String notifId);

    void insert(DtoNotification dtoNotification);

    void updateSeen(String userId);

    void deleteHalfYear();
}
