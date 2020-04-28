package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.Settings;

public interface SettingsDao {
    void initSettings(String userId);
    Settings editSettings(Settings settings);
    Settings getSettings(String userId);
}
