package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.data.dao.NotificationDao;
import net.dreamfteam.quiznet.data.entities.Notification;
import net.dreamfteam.quiznet.service.NotificationService;
import net.dreamfteam.quiznet.service.SseService;
import net.dreamfteam.quiznet.web.dto.DtoNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDao notificationDao;
    private final SseService sseService;

    @Autowired
    public NotificationServiceImpl(NotificationDao notificationDao, SseService sseService) {
        this.notificationDao = notificationDao;
        this.sseService = sseService;
    }

    @Override
    public List<Notification> getUnseenByUserId(String userId) {
        return notificationDao.getUnseenByUserId(userId);
    }

    @Override
    public Notification getById(String notifId) {
        return notificationDao.getById(notifId);
    }

    @Override
    public void insert(DtoNotification dtoNotification) {
        Notification notification = Notification.builder()
                .content(dtoNotification.getContent())
                .contentUk(dtoNotification.getContentUk())
                .userId(dtoNotification.getUserId())
                .build();


        sseService.send(dtoNotification.getUserId(), "sent", notificationDao.insert(notification));
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
