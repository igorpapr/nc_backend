package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.data.dao.NotificationDao;
import net.dreamfteam.quiznet.data.entities.Notification;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.NotificationService;
import net.dreamfteam.quiznet.web.dto.DtoNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDao notificationDao;

    @Autowired
    public NotificationServiceImpl(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    @Override
    public List<Notification> getUnseenByUserId(String userId) {
        return notificationDao.getUnseenByUserId(userId);
    }

    @Override
    public void insert(DtoNotification dtoNotification, MultipartFile image) throws ValidationException {
        Notification notification =
                Notification.builder()
                        .content(dtoNotification.getContent())
                        .userId(dtoNotification.getUserId())
                        .build();

        if (image != null) {
            try {
                notification.setImage(image.getBytes());
            } catch (IOException e) {
                throw new ValidationException("Broken image");
            }
        } else {
            notification.setImage(null);
        }

        notificationDao.insert(notification);

    }

    @Override
    public void updateSeen(String userId) {
        notificationDao.updateSeen(userId);
    }

    @Override
    public void deleteHalfYear() {
        notificationDao.deleteHalfYear();
    }
}
