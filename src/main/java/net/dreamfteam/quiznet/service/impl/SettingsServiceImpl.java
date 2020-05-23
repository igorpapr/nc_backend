package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.data.dao.SettingsDao;
import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.Setting;
import net.dreamfteam.quiznet.service.SettingsService;
import net.dreamfteam.quiznet.web.dto.DtoSetting;
import net.dreamfteam.quiznet.web.dto.DtoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingsServiceImpl implements SettingsService {

    private final SettingsDao settingsDao;



    @Autowired
    public SettingsServiceImpl(SettingsDao settingsDao) {
        this.settingsDao = settingsDao;
    }

    @Override
    public void initSettings(String userId, Role role, String language) {
        settingsDao.initSettings(userId, role, language);
    }

    @Override
    public void editSettings(List<DtoSettings> settings, String userId) {

        settingsDao.editSettings(settings, userId);
    }

    @Override
    public List<Setting> getSettings(String userId) {
        return settingsDao.getSettings(userId);
    }

    @Override
    public DtoSetting<String> getLanguage(String userId) {
        return new DtoSetting<>(settingsDao.getLanguage(userId));
    }

    @Override
    public DtoSetting<Boolean> getNotificationSetting(String userId) {
        return new DtoSetting<>(settingsDao.getNotificationSetting(userId));
    }
}
