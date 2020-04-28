package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.Settings;
import net.dreamfteam.quiznet.web.dto.DtoSettings;

public interface SettingsService {
    Settings initSettings(DtoSettings settings);

    Settings editSettings(DtoSettings settings);

    Settings getSettings(String userId);
}
