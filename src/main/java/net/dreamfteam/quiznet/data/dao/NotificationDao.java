package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.Notification;

import java.util.List;
import java.util.Map;

public interface NotificationDao {
    List<Notification> getUnseenByUserId(String userId);

    void insert(Notification notification);

    void updateSeen(String userId);

    void deleteHalfYear();
}
