package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.Setting;
import net.dreamfteam.quiznet.web.dto.DtoSettings;

import java.util.List;

public interface SettingsService {
    void initSettings(String id, Role role);

    void editSettings(List<DtoSettings> settings, String userId);

    List<Setting> getSettings(String userId);

    String getLanguage(String userId);
}
