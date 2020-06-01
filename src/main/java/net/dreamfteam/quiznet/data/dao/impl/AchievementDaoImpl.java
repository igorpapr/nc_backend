package net.dreamfteam.quiznet.data.dao.impl;

import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.configs.constants.Constants;
import net.dreamfteam.quiznet.configs.constants.SqlConstants;
import net.dreamfteam.quiznet.data.dao.AchievementDao;
import net.dreamfteam.quiznet.data.entities.UserAchievement;
import net.dreamfteam.quiznet.data.rowmappers.UserAchievementMapper;
import net.dreamfteam.quiznet.web.dto.DtoUserAchievement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class AchievementDaoImpl implements AchievementDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AchievementDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int assignAchievementRepeating(String userId, int achievementId) {
        try {
            return jdbcTemplate.update(SqlConstants.ACHIEVEMENT_ASSIGN_REPEATING,
                    userId, achievementId);
        } catch (NullPointerException | EmptyResultDataAccessException e) {
            log.error("Couldn't assign the repeatable achievement. AchievementId: "
                    + achievementId + "\n Exception: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public int assignAchievement(String userId, int achievementId) {
        try {
            return jdbcTemplate.update(SqlConstants.ACHIEVEMENT_ASSIGN_NONREPEATING, userId, achievementId);
        } catch (NullPointerException | EmptyResultDataAccessException e) {
            log.error("Couldn't assign the non-repeatable achievement. AchievementId: "
                    + achievementId + "\n Exception: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public List<UserAchievement> getUserAchievements(String userId) {
        try {
            return jdbcTemplate
                    .query(SqlConstants.ACHIEVEMENT_GET_USER_ACHIEVEMENTS,
                            new Object[]{userId, Constants.SETTING_LANG_ID, userId}, new UserAchievementMapper());
        } catch (NullPointerException | EmptyResultDataAccessException e) {
            log.error("Couldn't get user achievements. Exception: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<UserAchievement> getUserAchievementsLastWeek(String userId) {
        try{
            return jdbcTemplate.query(
                    SqlConstants.ACHIEVEMENT_GET_USER_ACHIEVEMENTS_LAST_WEEK,
                    new Object[]{userId, Constants.SETTING_LANG_ID, userId},
                    new UserAchievementMapper());

        }catch (EmptyResultDataAccessException | NullPointerException e){
            log.error("Couldn't get a list of user achievements during past week.\n Exception: " + e.getMessage());
            return null;
        }
    }

    @Override
    public DtoUserAchievement getUserAchievementByIds(String userId, int achievementId) {
        try {
            return jdbcTemplate
                    .queryForObject(SqlConstants.ACHIEVEMENT_GET_USER_ACHIEVEMENTS_BY_IDS,
                            new Object[]{userId, achievementId}, (rs, i) -> DtoUserAchievement.builder()
                                    .title(rs.getString("title"))
                                    .titleUk(rs.getString("title_uk"))
                                    .timesGained(rs.getInt("times_gained"))
                                    .username(rs.getString("username"))
                                    .build());
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            log.error("Couldn't find a user achievement info by given userId: " + userId
                    + ", and achievementId: " + achievementId + ".\n Exception: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Integer getUserAchievementsAmount(String userId){
        try{
            return jdbcTemplate
                    .queryForObject(SqlConstants.ACHIEVEMENT_GET_USER_ACHIEVEMENTS_AMOUNT,
                            new Object[]{userId}, Integer.class);

        }catch (Exception e){
            log.error("An exception occurred while counting the amount of user achievements: " + e.getMessage());
            return 0;
        }
    }

}
