package net.dreamfteam.quiznet.configs.scheduler;

import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.data.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import static javax.management.timer.Timer.ONE_DAY;

@Slf4j
@Component
public class UserScheduler {

    private UserDao userDao;

    @Autowired
    public UserScheduler(UserDao userDao) {
        this.userDao = userDao;
    }

    @Scheduled(fixedDelay = ONE_DAY)
    public void deleteUnacceptedUsers() {
        log.info(String.format("%d unaccepted Users was deleted ", userDao.deleteIfLinkExpired()));
    }

}
