package net.dreamfteam.quiznet.data.dao.impl;

import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.configs.constants.SqlConstants;
import net.dreamfteam.quiznet.data.dao.GameDao;
import net.dreamfteam.quiznet.data.entities.Game;
import net.dreamfteam.quiznet.data.entities.Question;
import net.dreamfteam.quiznet.data.entities.QuizCreatorFullStatistics;
import net.dreamfteam.quiznet.data.entities.UserCategoryAchievementInfo;
import net.dreamfteam.quiznet.data.rowmappers.GameMapper;
import net.dreamfteam.quiznet.web.dto.DtoGameCount;
import net.dreamfteam.quiznet.web.dto.DtoGameWinner;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Repository
public class GameDaoImpl implements GameDao {

    private final JdbcTemplate jdbcTemplate;
    private final Hashids hashids;

    @Autowired
    public GameDaoImpl(JdbcTemplate jdbcTemplate, Hashids hashids) {
        this.jdbcTemplate = jdbcTemplate;
        this.hashids = hashids;
    }

    @Override
    @Transactional
    public Game createGame(Game game) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(SqlConstants.GAMES_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setTimestamp(1, Timestamp.valueOf(java.time.LocalDateTime.now()));
                ps.setInt(2, game.getMaxUsersCount());
                ps.setInt(3, game.getNumberOfQuestions());
                ps.setInt(4, game.getRoundDuration());
                ps.setBoolean(5, game.isAdditionalPoints());
                ps.setInt(6, game.getBreakTime());
                ps.setObject(7, UUID.fromString(game.getQuizId()));
                return ps;
            }, keyHolder);

            String id = Objects.requireNonNull(keyHolder.getKeys()).get("game_id").toString();
            String accessId = generateAccessId(id);


            jdbcTemplate.update(SqlConstants.GAMES_EDITING_ACCESS_CODE,
                    accessId, UUID.fromString(id));

            game.setAccessId(accessId);
            game.setId(id);
            game.setStartDatetime((Date) Objects.requireNonNull(keyHolder.getKeys()).get("datetime_start"));

            log.info("Created game with access code: " + accessId);
            return game;

        }catch (DataAccessException e){
            log.error("Error while creating game\n" + e.getMessage());
            return null;
        }
    }

    @Override
    public void updateGame(Game game) {
        try {
            jdbcTemplate.update(SqlConstants.GAMES_UPDATE_GAME, game.getStartDatetime(), game.getMaxUsersCount(),
                    game.getNumberOfQuestions(), game.getRoundDuration(), game.isAdditionalPoints(),
                    game.getBreakTime(), game.getQuizId(), game.getId());
            log.info("Update game with id: " + game.getId());
        } catch (DataAccessException e) {
            log.error("Error while updating game with id: " + game.getId() + "\n" + e.getMessage());
        }
    }

    @Override
    public void deleteGame(String gameId) {
        try {
            jdbcTemplate.update(SqlConstants.GAMES_DELETE_GAME, gameId);
            log.info("Delete game with id: " + gameId);
        } catch (DataAccessException e) {
            log.error("Error while deleting game with id: " + gameId + "\n" + e.getMessage());
        }
    }


    @Override
    public Game getGame(String id) {
        try {
            Game result = jdbcTemplate.queryForObject(SqlConstants.GAMES_GET_GAME_BY_ID,
                    new Object[]{id}, new GameMapper());
            log.info("Get game with id: " + id);
            return result;
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            log.error("Error while getting game with id: " + id + "\n" + e.getMessage());
            return null;
        }
    }

    @Override
    public Game getGameByAccessId(String accessId) {
        try {
            Game result = jdbcTemplate.queryForObject(SqlConstants.GAMES_GET_GAME_BY_ACCESS_ID,
                    new Object[]{accessId}, new GameMapper());
            log.info("Get game with access code: " + accessId);
            return result;
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            log.error("Error while getting game with access code: " + accessId + "\n" + e.getMessage());
            return null;
        }
    }

    @Override
    public void startGame(String gameId) {
        try {
            jdbcTemplate.update(SqlConstants.GAMES_START_GAME, gameId);
            log.info("Started game with id: " + gameId);
        } catch (DataAccessException e) {
            log.error("Error while starting the game with id: " + gameId + "\n" + e.getMessage());
        }
    }


    public Question getQuestion(String gameId) {
        try {
            return jdbcTemplate.queryForObject(SqlConstants.GAMES_GET_QUESTION,

                    new Object[]{gameId, gameId}, (rs, i) -> Question.builder()
                            .id(rs.getString("question_id"))
                            .quizId(rs.getString("quiz_id"))
                            .title(rs.getString("title"))
                            .content(rs.getString("content"))
                            .image(rs.getString("image"))
                            .points(rs.getInt("points"))
                            .typeId(rs.getInt("type_id"))
                            .imageContent(rs.getBytes("imgcontent"))
                            .build());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    //Get info about the number of games played by user of some category by given game id
    @Override
    public UserCategoryAchievementInfo getUserGamesInCategoryInfo(String userId, String gameId) {
        try {
            return jdbcTemplate.queryForObject(
                    SqlConstants.GAMES_GET_USER_GAMES_IN_CATEGORY_INFO, new Object[]{gameId, userId},
                    (rs, i) -> UserCategoryAchievementInfo.builder()
                            .amountPlayed(rs.getInt("amount"))
                            .categoryId(rs.getString("category_id"))
                            .categoryTitle(rs.getString("title"))
                            .build());
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return null;
        }
    }

    //For achievements: returns the number of all games with quizzes that created the creator of given gameId
    @Override
    public QuizCreatorFullStatistics getAmountOfPlayedGamesCreatedByCreatorOfGame(String gameId) {
        try {
            return jdbcTemplate
                    .queryForObject(SqlConstants.GAMES_GET_AMOUNT_OF_PLAYED_GAMES_CREATED_BY_CREATOR_OF_GAME,
                            new Object[]{gameId},
                            (rs, i) -> QuizCreatorFullStatistics.builder()
                                    .creatorId(rs.getString("creator"))
                                    .amountGamesPlayedAllQuizzes(rs.getInt("amount"))
                                    .build());
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<DtoGameWinner> getWinnersOfTheGame(String gameId) {
        try {
            List<DtoGameWinner> result = jdbcTemplate.query(SqlConstants.GAMES_GET_WINNERS_OF_THE_GAME,
                    new Object[]{gameId},
                    (rs, i) -> DtoGameWinner.builder()
                            .userId(rs.getString("user_id"))
                            .quizTitle(rs.getString("title"))
                            .build());

            log.info("Get winners of game with id: " + gameId);
            return result;
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            log.error("Error while getting game winners for game with id: "+gameId+"\n"+e.getMessage());
            return null;
        }
    }

    @Override
    public List<DtoGameCount> getGamesAmountForDay() {
        return jdbcTemplate.query(SqlConstants.GAMES_GET_GAMES_AMOUNT_FOR_DAY,
                (rs, i) -> DtoGameCount.builder()
                        .date(rs.getDate("dt_start"))
                        .gamesAmount(rs.getInt("amount"))
                        .build());
    }

    @Override
    public int gameTime(String gameId) {

        try {
            int result = Optional.ofNullable(jdbcTemplate.queryForObject(SqlConstants.GAMES_MAX_GAME_TIME,
                    new Object[]{gameId}, Integer.class)).orElse(0);
            log.info("Game will be played for: " + result + " secs");
            return result;
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            log.error("Error while getting game time for game with id: "+gameId+"\n"+e.getMessage());
            return 0;
        }

    }

    private String generateAccessId(String gameId) {
        return hashids.encodeHex(gameId.substring(0, gameId.indexOf("-")));
    }
}
