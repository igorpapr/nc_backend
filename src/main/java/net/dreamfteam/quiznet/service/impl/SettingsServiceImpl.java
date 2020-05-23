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
    private final IAuthenticationFacade authenticationFacade;


    @Autowired
    public SettingsServiceImpl(SettingsDao settingsDao, IAuthenticationFacade authenticationFacade) {
        this.settingsDao = settingsDao;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public void initSettings(String userId, Role role, String language) {
        settingsDao.initSettings(userId, role, language);
    }

    @Override
    public void editSettings(List<DtoSettings> settings) {

        settingsDao.editSettings(settings, authenticationFacade.getUserId());
    }

    @Override
    public List<Setting> getSettings() {
        return settingsDao.getSettings(authenticationFacade.getUserId());
    }

    @Override
    public DtoSetting<String> getLanguage() {
        return new DtoSetting<>(settingsDao.getLanguage(authenticationFacade.getUserId()));
    }

    @Override
    public DtoSetting<Boolean> getNotificationSetting() {
        return new DtoSetting<>(settingsDao.getNotificationSetting(authenticationFacade.getUserId()));
    }
}
