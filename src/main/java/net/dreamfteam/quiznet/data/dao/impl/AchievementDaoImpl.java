package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.data.dao.AchievementDao;
import net.dreamfteam.quiznet.data.entities.UserAchievement;
import net.dreamfteam.quiznet.data.entities.UserCategoryAchievementInfo;
import net.dreamfteam.quiznet.data.rowmappers.UserAchievementMapper;
import net.dreamfteam.quiznet.web.dto.DtoUserAchievement;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AchievementDaoImpl implements AchievementDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AchievementDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int assignAchievementRepeating(String userId, int achievementId) {
        try {
            return jdbcTemplate.update("INSERT INTO users_achievements AS t" +
                    "(user_id, achievement_id, datetime_gained) " +
                    "VALUES (uuid(?),?,CURRENT_TIMESTAMP) " +
                    "ON CONFLICT (user_id, achievement_id) DO UPDATE " +
                    "SET times_gained = t.times_gained + 1;", userId, achievementId);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    @Override
    public int assignAchievement(String userId, int achievementId) {
        try {
            return jdbcTemplate.update("INSERT INTO users_achievements AS t" +
                    "(user_id, achievement_id, datetime_gained) " +
                    "VALUES (uuid(?),?,CURRENT_TIMESTAMP) " +
                    "ON CONFLICT (user_id, achievement_id) DO NOTHING;", userId, achievementId);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    @Override
    public List<UserAchievement> getUserAchievements(String userId) {
        try {
            return jdbcTemplate
                    .query("SELECT a.achievement_id, " +
                                "CASE tmp.value WHEN 'uk' THEN a.title_uk ELSE a.title END AS title, " +
                                "CASE tmp.value WHEN 'uk' THEN a.description_uk ELSE a.description END AS description, " +
                                "a.image_content, a.category_id, " +
                                "c.title AS category_title, ua.datetime_gained, ua.times_gained " +
                                "FROM achievements a INNER JOIN users_achievements ua " +
                                "ON a.achievement_id = ua.achievement_id " +
                                "INNER JOIN (SELECT value, us.user_id " + //selecting language settings
                                            "FROM user_settings us " +
                                            "WHERE us.user_id = uuid(?) AND setting_id = uuid(?)) " +
                                    "AS tmp ON ua.user_id = tmp.user_id " +
                                "LEFT JOIN categories c ON a.category_id = c.category_id " +
                                "WHERE ua.user_id = uuid(?);",
                            new Object[]{userId, Constants.SETTING_LANG_ID, userId}, new UserAchievementMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<UserAchievement> getUserAchievementsLastWeek(String userId) {
        return jdbcTemplate.query(
                "SELECT a.achievement_id, " +
                        "CASE tmp.value WHEN 'uk' THEN a.title_uk ELSE a.title END AS title, " +
                        "CASE tmp.value WHEN 'uk' THEN a.description_uk ELSE a.description END AS description, " +
                        "a.image_content, a.category_id, " +
                "c.title AS category_title, ua.datetime_gained, ua.times_gained " +
                "FROM achievements a INNER JOIN users_achievements ua " +
                "ON a.achievement_id = ua.achievement_id " +
                "INNER JOIN (SELECT value, us.user_id " + //selecting language settings
                            "FROM user_settings us " +
                            "WHERE us.user_id = uuid(?) AND setting_id = uuid(?)) " +
                    "AS tmp ON ua.user_id = tmp.user_id " +
                "LEFT JOIN categories c ON a.category_id = c.category_id " +
                "WHERE ua.user_id = UUID(?) " +
                "ORDER BY datetime_gained", new Object[]{userId, Constants.SETTING_LANG_ID, userId},
                new UserAchievementMapper());
    }

    @Override
    public DtoUserAchievement getUserAchievementByIds(String userId, int achievementId) {
        try {
            return jdbcTemplate
                    .queryForObject("SELECT a.title, a.title_uk, ua.times_gained " +
                                    "FROM achievements a INNER JOIN users_achievements ua " +
                                    "ON a.achievement_id = ua.achievement_id " +
                                    "WHERE ua.user_id = uuid(?) AND ua.achievement_id = ?;",
                            new Object[]{userId, achievementId}, (rs, i) -> DtoUserAchievement.builder()
                                    .title(rs.getString("title"))
                                    .titleUk(rs.getString("title_uk"))
                                    .timesGained(rs.getInt("times_gained"))
                                    .build());
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            System.err.println("Couldn't find user achievement info by userId: " + userId
                    + ", and achievementId: " + achievementId);
            return null;
        }
    }

    @Override
    public Integer getUserAchievementsAmount(String userId){
        return jdbcTemplate
                .queryForObject("SELECT count(*) FROM users_achievements " +
                                "WHERE user_id = uuid(?);",
                        new Object[]{userId}, Integer.class);
    };

}
