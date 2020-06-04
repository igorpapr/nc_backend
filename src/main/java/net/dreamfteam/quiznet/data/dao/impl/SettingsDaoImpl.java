package net.dreamfteam.quiznet.data.dao.impl;

import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.configs.constants.SqlConstants;
import net.dreamfteam.quiznet.data.dao.SettingsDao;
import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.Setting;
import net.dreamfteam.quiznet.data.rowmappers.SettingMapper;
import net.dreamfteam.quiznet.web.dto.DtoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
        try{
            List<String> ids = getIds(role == Role.ROLE_USER);
            for (String id:ids) {
                jdbcTemplate.update(SqlConstants.SETTINGS_INIT_SETTINGS_DEFAULTS,userId,id);
            }
            jdbcTemplate.update(SqlConstants.SETTINGS_INIT_SETTINGS_LANGUAGE,userId,language);

            log.info("Initialized settings for user: "+userId);
        }catch (DataAccessException | NullPointerException e){
            log.error("Error while initialize settings for user: "+userId+"\n"+e.getMessage());
        }

    }

    private List<String> getIds(boolean isUser){
        try{
            List<String> result;
            if(isUser){
                result = jdbcTemplate.queryForList(SqlConstants.SETTINGS_GET_TITLES_DEFAULT, String.class);
            }else{
                result = jdbcTemplate.queryForList(SqlConstants.SETTINGS_GET_TITLES_PRIVILEGED, String.class);
            }
            log.info("Get ids of settings for user or privileged");
            return result;
        }catch (DataAccessException e){
            log.error("Error while getting ids of settings\n"+e.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public void editSettings(List<DtoSettings> settings, String userId) {
        try {
            for (DtoSettings setting : settings) {
                jdbcTemplate.update(SqlConstants.SETTINGS_EDIT_SETTINGS, setting.getValue(), userId, setting.getId());
            }

            log.info("Updated settings for user: "+userId);
        }catch (DataAccessException e){
            log.error("Error while editing settings for user: "+userId+"\n"+e.getMessage());
        }
    }

    @Override
    public List<Setting> getSettings(String userId) {
        try {
            String langValue = getLanguage(userId);

            List<Setting> result = jdbcTemplate.query(SqlConstants.SETTINGS_GET_SETTINGS,
                    new Object[]{langValue, langValue, userId}, new SettingMapper());

            log.info("Get settings for user: "+userId);
            return result;
        }catch (DataAccessException e){
            log.error("Error while getting settings for user: "+userId+"\n"+e.getMessage());
            return null;
        }
    }

    @Override
    public String getLanguage(String userId){
        try {
            String result = jdbcTemplate.queryForObject(SqlConstants.SETTINGS_GET_LANGUAGE,
                    new Object[]{userId}, String.class);

            log.info("Get language setting for user: "+userId);
            return result;
        }catch (DataAccessException e){
            log.error("Error while getting language setting for user: "+userId+"\n"+e.getMessage());
            return "";
        }
    }

    @Override
    public boolean getNotificationSetting(String userId){
        try {
            boolean result = Boolean.parseBoolean(jdbcTemplate.queryForObject(
                    SqlConstants.SETTINGS_GET_NOTIFICATION_SETTING,
                    new Object[]{userId}, String.class));

            log.info("Get notifications setting for user: "+userId);
            return result;
        } catch (DataAccessException e) {
            log.error("Error while getting notifications setting for user: " + userId + "\n" + e.getMessage());
            return false;
        }
    }
}
