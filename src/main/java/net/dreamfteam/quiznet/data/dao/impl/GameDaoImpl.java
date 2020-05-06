package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.GameDao;
import net.dreamfteam.quiznet.data.dao.GameSessionDao;
import net.dreamfteam.quiznet.data.entities.Game;
import net.dreamfteam.quiznet.data.entities.GameSession;
import net.dreamfteam.quiznet.data.rowmappers.GameMapper;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.Objects;

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
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO games " +
                    "(datetime_start, max_num_of_users, number_of_questions," +
                    "round_duration, time_additional_points, break_time," +
                    " quiz_id)" +
                    " VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.setInt(2, game.getMaxUsersCount());
            ps.setInt(3, game.getNumberOfQuestions());
            ps.setInt(4, game.getRoundDuration());
            ps.setBoolean(5, game.isAdditionalPoints());
            ps.setInt(6, game.getBreakTime());
            ps.setObject(7, java.util.UUID.fromString(game.getQuizId()));
            return ps;
        }, keyHolder);

        String id = Objects.requireNonNull(keyHolder.getKeys()).get("game_id").toString();
        String accessId = generateAccessId(id);


        jdbcTemplate.update("UPDATE games SET access_code = ? WHERE game_id = ?",
                accessId, java.util.UUID.fromString(id));

        game.setAccessId(accessId);
        game.setId(id);
        game.setStartDatetime((java.util.Date) Objects.requireNonNull(keyHolder.getKeys()).get("datetime_start"));

        return game;
    }

    @Override
    public void updateGame(Game game) {
        jdbcTemplate.update("UPDATE games SET " +
                "datetime_start = ?, max_num_of_users = ?, number_of_questions = ?," +
                "round_duration = ?, time_additional_points = ?, break_time = ?," +
                " quiz_id = UUID(?) " +
                "WHERE game_id = UUID(?)", game.getStartDatetime(), game.getMaxUsersCount(),
                game.getNumberOfQuestions(), game.getRoundDuration(), game.isAdditionalPoints(),
                game.getBreakTime(), game.getQuizId(), game.getId());
    }

    @Override
    public void removeGame(String id) {

    }

    @Override
    public Game getGame(String id) {
        return jdbcTemplate.queryForObject("SELECT * FROM games WHERE game_id = UUID(?)",
                new Object[]{id}, new GameMapper());
    }

    @Override
    public Game getGameByAccessId(String accessId) {
        return jdbcTemplate.queryForObject("SELECT * FROM games WHERE access_code = ?",
                new Object[]{accessId}, new GameMapper());
    }

    @Override
    public void startGame(String gameId) {
        jdbcTemplate.update("UPDATE games SET access_code = '' WHERE game_id = UUID(?)", gameId);
    }

    @Override
    public int getGameDuration(String gameId) {
        return calculateDuration(getGame(gameId));
    }

    @Override
    public int calculateDuration(Game game) {
        return game.getNumberOfQuestions() * (game.getRoundDuration() + game.getBreakTime());
    }


    private String generateAccessId(String gameId) {
        return hashids.encodeHex(gameId.substring(0, gameId.indexOf("-")));
    }
}
