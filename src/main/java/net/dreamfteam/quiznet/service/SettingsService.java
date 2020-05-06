package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.Settings;
import net.dreamfteam.quiznet.web.dto.DtoSettings;

public interface SettingsService {
    void initSettings(String id);

    Settings editSettings(DtoSettings settings);

    Settings getSettings(String userId);
}
