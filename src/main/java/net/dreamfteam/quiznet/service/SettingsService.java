package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.Setting;
import net.dreamfteam.quiznet.web.dto.DtoSettings;

import java.util.List;

public interface SettingsService {
    void initSettings(String userId, Role role, String language);

    void editSettings(List<DtoSettings> settings);

    List<Setting> getSettings();

    String getLanguage();

    boolean getNotificationSetting();
}
