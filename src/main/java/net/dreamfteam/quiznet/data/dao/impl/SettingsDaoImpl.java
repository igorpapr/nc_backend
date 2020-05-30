package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.configs.constants.SqlConstants;
import net.dreamfteam.quiznet.data.dao.SettingsDao;
import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.Setting;
import net.dreamfteam.quiznet.data.rowmappers.SettingMapper;
import net.dreamfteam.quiznet.web.dto.DtoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class SettingsDaoImpl implements SettingsDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SettingsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void initSettings(String userId, Role role, String language) {
        List<String> titles = getTitles(role == Role.ROLE_USER);

        for (String title:titles) {
            jdbcTemplate.update(SqlConstants.SETTINGS_INIT_SETTINGS_DEFAULTS,userId,title);
        }

        jdbcTemplate.update(SqlConstants.SETTINGS_INIT_SETTINGS_LANGUAGE,userId,language);
    }

    private List<String> getTitles(boolean isUser){
        if(isUser){
            return jdbcTemplate.queryForList(SqlConstants.SETTINGS_GET_TITLES_DEFAULT, String.class);
        }else{
            return jdbcTemplate.queryForList(SqlConstants.SETTINGS_GET_TITLES_PRIVILEGED, String.class);
        }
    }

    @Override
    public void editSettings(List<DtoSettings> settings, String userId) {
        for (DtoSettings setting: settings) {
            jdbcTemplate.update(SqlConstants.SETTINGS_EDIT_SETTINGS, setting.getValue(), userId, setting.getId());
        }
    }

    @Override
    public List<Setting> getSettings(String userId) {

        String langValue = getLanguage(userId);

        return jdbcTemplate.query(SqlConstants.SETTINGS_GET_SETTINGS,
                new Object[]{langValue,langValue,userId}, new SettingMapper());
    }

    @Override
    public String getLanguage(String userId){
        return jdbcTemplate.queryForObject(SqlConstants.SETTINGS_GET_LANGUAGE, new Object[]{userId}, String.class);
    }

    @Override
    public boolean getNotificationSetting(String userId){
        return Boolean.parseBoolean(jdbcTemplate.queryForObject(SqlConstants.SETTINGS_GET_NOTIFICATION_SETTING,
                new Object[]{userId}, String.class));
    }
}
