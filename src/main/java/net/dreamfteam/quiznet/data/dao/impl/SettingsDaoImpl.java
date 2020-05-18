package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.SettingsDao;
import net.dreamfteam.quiznet.data.entities.Setting;
import net.dreamfteam.quiznet.data.entities.Settings;
import net.dreamfteam.quiznet.data.rowmappers.SettingMapper;
import net.dreamfteam.quiznet.data.rowmappers.SettingsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SettingsDaoImpl implements SettingsDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SettingsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void initSettings(String userId) {
        List<String> titles = getTitles();

        for (String title:titles) {
            jdbcTemplate.update("insert into user_settings (user_id, setting_id, value) " +
                    "select uuid(?) , setting_id, default_value from settings " +
                    "where title=?;",userId,title);
        }


    }

    private List<String> getTitles(){
        return jdbcTemplate.queryForList("SELECT title FROM settings",String.class);
    }

    @Override
    public Settings editSettings(Settings settings) {
        jdbcTemplate.update("update user_settings set value= ? where user_id = uuid(?) " +
                "and setting_id = (select setting_id from settings where title='friendsActivities'); update user_settings set value= ?" +
                "where user_id = uuid(?) and setting_id = (select setting_id from settings where title='announcements');", new Object[]{settings.isSeeFriendsActivities(), settings.getUserId(), settings.isSeeAnnouncements(), settings.getUserId()});
        return settings;
    }

    @Override
    public List<Setting> getSettings(String userId) {

        return jdbcTemplate.query("SELECT settings.setting_id, title, " +
                "descroption, value " +
                "FROM settings INNER JOIN " +
                "users on settings.setting_id=users.setting_id " +
                "WHERE user_id=UUID(?);", new Object[]{userId}, new SettingMapper());
    }
}
