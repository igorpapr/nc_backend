package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.Settings;

public interface SettingsDao {
    Settings initSettings(Settings settings);
    Settings editSettings(Settings settings);
    Settings getSettings(String userId);
}
