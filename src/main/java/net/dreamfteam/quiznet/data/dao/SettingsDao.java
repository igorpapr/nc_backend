package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.Setting;
import net.dreamfteam.quiznet.web.dto.DtoSettings;

import java.util.List;

public interface SettingsDao {
    void initSettings(String userId, Role role);

    void editSettings(List<DtoSettings> settings, String userId);

    List<Setting> getSettings(String userId);

    String getLanguage(String userId);

    boolean getNotificationSetting(String userId);


}
