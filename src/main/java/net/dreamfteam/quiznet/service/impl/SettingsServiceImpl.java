package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.configs.security.AuthenticationFacade;
import net.dreamfteam.quiznet.data.dao.SettingsDao;
import net.dreamfteam.quiznet.data.entities.Settings;
import net.dreamfteam.quiznet.service.SettingsService;
import net.dreamfteam.quiznet.web.dto.DtoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsServiceImpl implements SettingsService {

    SettingsDao settingsDao;
    AuthenticationFacade authenticationFacade;

    @Autowired
    public SettingsServiceImpl(SettingsDao settingsDao, AuthenticationFacade authenticationFacade) {
        this.settingsDao = settingsDao;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public void initSettings() {
        settingsDao.initSettings(authenticationFacade.getUserId());
    }

    @Override
    public Settings editSettings(DtoSettings settings) {
        Settings settings1 = Settings.builder().
                seeAnnouncements(Boolean.parseBoolean(settings.getSeeAnnouncements()))
                .seeFriendsActivities(Boolean.parseBoolean(settings.getSeeFriendsActivities()))
                .userId(authenticationFacade.getUserId())
                .build();

        return settingsDao.editSettings(settings1);
    }

    @Override
    public Settings getSettings(String userId) {
        return settingsDao.getSettings(userId);
    }
}
