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
    public void initSettings(String userId) {
        jdbcTemplate.update("insert into user_settings (user_id, setting_id, value) " +
                " select uuid(?) , setting_id, default_value from settings where title='announcements';" +
                "insert into user_settings (user_id, setting_id, value) " +
                " select uuid(?) , setting_id, default_value from settings where title='friendsActivities';",
                userId, userId);

    }

    @Override
    public Settings editSettings(Settings settings) {
        jdbcTemplate.update("update user_settings set value= ? where user_id = uuid(?) " +
                "and setting_id = (select setting_id from settings where title='friendsActivities'); update user_settings set value= ?" +
                 "where user_id = uuid(?) and setting_id = (select setting_id from settings where title='announcements');", new Object[]{settings.isSeeFriendsActivities(), settings.getUserId(), settings.isSeeAnnouncements(), settings.getUserId()});
        return settings;
    }

    @Override
    public Settings getSettings(String userId) {
        Settings settings = jdbcTemplate.query("select title, value from settings inner join (select value, setting_id from user_settings where\n" +
                "user_id=UUID(?)) users on settings.setting_id=users.setting_id;", new Object[]{userId}, new SettingsMapper());

        return settings;
    }
}
