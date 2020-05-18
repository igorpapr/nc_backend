package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.configs.security.AuthenticationFacade;
import net.dreamfteam.quiznet.data.dao.SettingsDao;
import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.Setting;
import net.dreamfteam.quiznet.data.entities.Settings;
import net.dreamfteam.quiznet.service.SettingsService;
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
    public void initSettings(String id, Role role) {
        settingsDao.initSettings(id,role);
    }

    @Override
    public void editSettings(List<DtoSettings> settings, String userId) {
        settingsDao.editSettings(settings, userId);
    }

    @Override
    public List<Setting> getSettings(String userId) {
        return settingsDao.getSettings(userId);
    }
}
