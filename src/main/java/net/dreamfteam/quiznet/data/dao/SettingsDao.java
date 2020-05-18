package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.Setting;
import net.dreamfteam.quiznet.data.entities.Settings;

import java.util.List;

public interface SettingsDao {
    void initSettings(String userId);
    Settings editSettings(Settings settings);
    List<Setting> getSettings(String userId);
}
