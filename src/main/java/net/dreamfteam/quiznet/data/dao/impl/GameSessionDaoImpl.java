package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.configs.constants.SqlConstants;
import net.dreamfteam.quiznet.data.dao.GameDao;
import net.dreamfteam.quiznet.data.dao.GameSessionDao;
import net.dreamfteam.quiznet.data.entities.GameSession;
import net.dreamfteam.quiznet.data.rowmappers.GameSessionMapper;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.SseService;
import net.dreamfteam.quiznet.web.dto.DtoPlayerSession;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GameSessionDaoImpl implements GameSessionDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GameSessionDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public GameSession getSessionByAccessId(String accessId, String userId, String username) {


        try {
            if(!userId.startsWith("-")){
                return jdbcTemplate.queryForObject(SqlConstants.GAME_SESSIONS_GET_SESSION_BY_ACCESS_ID_FOR_USER,
                                                   new Object[]{userId, username, accessId}, new GameSessionMapper());
            }else{
                return jdbcTemplate.queryForObject(SqlConstants.GAME_SESSIONS_GET_SESSION_BY_ACCESS_ID_FOR_ANONYM,
                        new Object[]{username, accessId}, new GameSessionMapper());
            }

        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Override
    public GameSession getById(String sessionId) {
        try {
            return jdbcTemplate.queryForObject(SqlConstants.GAME_SESSIONS_GET_SESSION_BY_ID,
                    new Object[]{sessionId}, new GameSessionMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<DtoPlayerSession> getSessions(String gameId) {
        try {
            return jdbcTemplate
                    .query(SqlConstants.GAME_SESSIONS_GET_SESSIONS_BY_GAME_ID, new Object[]{gameId},
                            (rs, i) -> DtoPlayerSession.builder()
                                    .game_session_id(rs.getString("game_session_id"))
                                    .duration_time(rs.getInt("duration_time"))
                                    .image(rs.getBytes("image"))
                                    .is_creator(rs.getBoolean("is_creator"))
                                    .is_winner(rs.getBoolean("is_winner"))
                                    .score(rs.getInt("score"))
                                    .user_id(rs.getString("user_id"))
                                    .username(rs.getString("username"))
                                    .build());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Override
    public GameSession createSession(GameSession gameSession) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(SqlConstants.GAME_SESSIONS_CREATE_SESSION, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, gameSession.getUserId() == null ? gameSession.getUserId() :
                    UUID.fromString(gameSession.getUserId()));
            ps.setObject(2, UUID.fromString(gameSession.getGameId()));
            ps.setInt(3, gameSession.getScore());
            ps.setBoolean(4, gameSession.isWinner());
            ps.setBoolean(5, gameSession.isCreator());
            ps.setBoolean(6, gameSession.isSavedByUser());
            ps.setObject(7, gameSession.getDurationTime());
            ps.setString(8, gameSession.getUsername());
            return ps;
        }, keyHolder);

        gameSession.setId(Objects.requireNonNull(keyHolder.getKeys()).get("game_session_id").toString());

        return gameSession;
    }

    @Override
    @Transactional
    public void updateSession(GameSession gameSession) {
        jdbcTemplate.update(SqlConstants.GAME_SESSIONS_UPDATE_SESSION,
                gameSession.getScore(), gameSession.getDurationTime(), gameSession.getId());
    }

    @Override
    public boolean gameHasAvailableSlots(String accessId) {
        int playersJoined = Optional.ofNullable(jdbcTemplate.queryForObject(
                SqlConstants.GAME_SESSIONS_GET_USER_AMOUNT_IN_GAME_BY_ACCESS_CODE, new Object[]{accessId}, Integer.class))
                .orElse(0);

        int allSlots = Optional.ofNullable(jdbcTemplate.queryForObject(SqlConstants.GAME_SESSIONS_MAX_USER_AMOUNT, new Object[]{accessId}, Integer.class))
                .orElse(0);

        return playersJoined != allSlots;
    }

    @Override
    public String getGameId(String sessionId) {
        return jdbcTemplate.queryForObject(SqlConstants.GAME_SESSIONS_GET_GAME_BY_ID,
                new Object[]{sessionId}, String.class);
    }

    @Override
    @Transactional
    public void removePlayer(String sessionId) {
        jdbcTemplate.update(SqlConstants.GAME_SESSIONS_REMOVE_PLAYER, sessionId);
    }

    //For achievements: returns the number of all finished game sessions of user
    @Override
    public int getNumberOfSessionsOfUser(String userId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SqlConstants.GAME_SESSIONS_GET_NUMBER_OF_SESSIONS_OF_USER,
                    new Object[]{userId},
                    Integer.class))
                    .orElse(0);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    //For achievements: returns the number of unique quizzes played by the user
    @Override
    public int getNumberOfQuizzesPlayedByUser(String userId) {
        try {
            return Optional.ofNullable(jdbcTemplate
                    .queryForObject(SqlConstants.GAME_SESSIONS_GET_NUMBER_OF_QUIZZES_PLAYED_BY_USER,
                            new Object[]{userId},
                            Integer.class))
                    .orElse(0);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }


    //Checks if game is finished
    @Override
    public boolean isGameFinished(String gameId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SqlConstants.GAME_SESSIONS_IS_GAME_FINISHED,
                    new Object[]{gameId}, Boolean.class))
                    .orElse(false);
        } catch (DataAccessException e) {
            return false;
        }
    }

    //Sets the winners of the game
    @Override
    public int setWinnersForTheGame(String gameId) {
        try {
            return jdbcTemplate.update(SqlConstants.GAME_SESSIONS_SET_WINNERS_FOR_THE_GAME,
                    gameId, gameId);
        } catch (DataAccessException e) {
            return 0;
        }
    }

    //Returns true if player that joined to current game is first
    @Override
    public boolean isCreator(String gameId){
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                SqlConstants.GAME_SESSIONS_GET_USER_AMOUNT_IN_GAME_BY_ID,
                new Object[]{gameId},Integer.class)).orElse(0) == 0;
    }
}
