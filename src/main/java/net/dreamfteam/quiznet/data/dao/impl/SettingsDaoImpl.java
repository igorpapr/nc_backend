package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.SettingsDao;
import net.dreamfteam.quiznet.data.entities.Settings;
import net.dreamfteam.quiznet.data.rowmappers.SettingsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SettingsDaoImpl implements SettingsDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SettingsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Settings initSettings(Settings settings) {
        return null; //TODO
    }

    @Override
    public Settings editSettings(Settings settings) {
        return null; //TODO
    }

    @Override
    public Settings getSettings(String userId) {
        Settings settings = jdbcTemplate.query("select title, value from settings inner join (select value, setting_id from user_settings where\n" +
                "user_id=UUID(?)) users on settings.setting_id=users.setting_id;", new Object[]{userId}, new SettingsMapper());

        return settings;
    }
}
