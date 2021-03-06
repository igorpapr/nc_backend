package net.dreamfteam.quiznet.configs.scheduler;

import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.configs.constants.Constants;
import net.dreamfteam.quiznet.data.dao.UserDao;
import net.dreamfteam.quiznet.service.NotificationService;
import net.dreamfteam.quiznet.service.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static javax.management.timer.Timer.ONE_DAY;
import static javax.management.timer.Timer.ONE_SECOND;

@Slf4j
@Component
public class UserScheduler {

    private final UserDao userDao;
    private final NotificationService notificationService;
    private final SseService sseService;

    @Autowired
    public UserScheduler(UserDao userDao, NotificationService notificationService, SseService sseService) {
        this.userDao = userDao;
        this.notificationService = notificationService;
        this.sseService = sseService;
    }

    @Scheduled(fixedDelay = ONE_DAY)
    public void deleteUnacceptedUsers() {
        log.info(String.format(Constants.DELETED_UNACCEPTED_USERS, userDao.deleteIfLinkExpired()));
    }

    @Scheduled(fixedDelay = ONE_DAY)
    public void deleteHalfYearNotifications() {
        notificationService.deleteHalfYear();
        log.info("Deleted half year notifications");
    }

    // for heroku https://devcenter.heroku.com/articles/error-codes#h15-idle-connection
    @Scheduled(fixedDelay = ONE_SECOND * 40)
    public void sendSseSubscribersMessage() {
        for (String key : sseService.getSseMap()
                                    .keySet()) {
            sseService.send(key, "it works");
        }
        log.info("Send message to all sse subscribers");
    }

}
