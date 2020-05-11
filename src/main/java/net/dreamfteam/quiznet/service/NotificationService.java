package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.Notification;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.web.dto.DtoNotification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NotificationService {
    List<Notification> getUnseenByUserId(String userId);

    void insert(DtoNotification dtoNotification, MultipartFile image) throws ValidationException;

    void updateSeen(String userId);

    void deleteHalfYear();
}
