package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.GameDao;
import net.dreamfteam.quiznet.data.entities.Game;
import net.dreamfteam.quiznet.data.entities.Question;
import net.dreamfteam.quiznet.data.entities.QuizCreatorFullStatistics;
import net.dreamfteam.quiznet.data.entities.UserCategoryAchievementInfo;
import net.dreamfteam.quiznet.data.rowmappers.GameMapper;
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
import java.util.List;
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


    private String generateAccessId(String gameId) {
        return hashids.encodeHex(gameId.substring(0, gameId.indexOf("-")));
    }

    public Question getQuestion(String gameId) {
        try {
            Question question = jdbcTemplate.queryForObject("select q.question_id, q.quiz_id, q.title, q.content, q.image, q.points, q.type_id, i.image as imgcontent FROM questions q LEFT JOIN images i ON q.image = i.image_id where quiz_id = ( select games.quiz_id from  games where game_id = (select game_id from users_games where game_session_id =uuid(?))) offset (select count(*) from answers where users_game_id=uuid(?)) rows limit 1;",
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
            return question;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    //Get info about the number of games played by user of some category by given game id
    @Override
    public UserCategoryAchievementInfo getUserGamesInCategoryInfo(String userId, String gameId) {
        try {
            UserCategoryAchievementInfo info = jdbcTemplate.queryForObject(
                    "SELECT COUNT(DISTINCT g1.game_id) AS amount, cq1.category_id, c.title AS title " +
                        "FROM users_games ug INNER JOIN games g1 ON ug.game_id = g1.game_id " +
                        "    INNER JOIN categs_quizzes cq1 ON g1.quiz_id = cq1.quiz_id " +
                            "INNER JOIN categories c ON cq1.category_id = c.category_id " +
                        "WHERE cq1.category_id = " +
                        "                  (SELECT category_id " +
                        "                  FROM games g INNER JOIN categs_quizzes cq ON g.quiz_id = cq.quiz_id " +
                        "                  WHERE game_id = uuid(?)) " +
                        "      AND ug.user_id = uuid(?) " +
                        "GROUP BY cq1.category_id, c.title;", new Object[]{gameId, userId},
                    (rs, i) -> UserCategoryAchievementInfo.builder()
                            .amountPlayed(rs.getInt("amount"))
                            .categoryId(rs.getString("category_id"))
                            .categoryTitle(rs.getString("title"))
                            .build());
            return info;
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return null;
        }
    }

    //For achievements: returns the number of all games with quizzes that created the creator of given gameId
    @Override
    public QuizCreatorFullStatistics getAmountOfPlayedGamesCreatedByCreatorOfGame(String gameId) {
        try {
            QuizCreatorFullStatistics info = jdbcTemplate
                    .queryForObject("SELECT COUNT(*) AS amount, q.creator_id AS creator " +
                            "FROM games g INNER JOIN quizzes q ON q.quiz_id = g.quiz_id " +
                            "WHERE g.quiz_id IN (SELECT q1.quiz_id " +
                                                "FROM quizzes q1 " +
                                                "WHERE q1.creator_id = (SELECT creator_id " +
                                                                        "FROM quizzes qq INNER JOIN games gg " +
                                                                        "ON qq.quiz_id = gg.quiz_id " +
                                                                        "WHERE gg.game_id = uuid(?))) " +
                                    "GROUP BY q.creator_id;",
                    new Object[]{gameId},
                            (rs, i) -> QuizCreatorFullStatistics.builder()
                                    .creatorId(rs.getString("creator"))
                                    .amountGamesPlayedAllQuizzes(rs.getInt("amount"))
                                    .build());
            return info;
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return null;
        }
    }

    @Override
    public boolean isGameFinished(String gameId) {
        try {
            return jdbcTemplate.queryForObject("SELECT CASE " +
                            "WHEN COUNT(*) = COUNT(CASE WHEN finished THEN 1 END) " +
                            "THEN TRUE " +
                            "ELSE FALSE END " +
                            "FROM users_games WHERE game_id = UUID(?);",
                    new Object[]{gameId}, Boolean.class);
        }catch (DataAccessException e){
            return false;
        }
    }

    @Override
    public int setWinnersForTheGame(String gameId) {
        try{
            return jdbcTemplate.update("UPDATE users_games SET " +
                            "is_winner = true " +
                            "WHERE game_session_id IN (" +
                            "SELECT game_session_id FROM users_games" +
                            " WHERE game_id = UUID(?)" +
                            "AND score = (" +
                            "SELECT MAX(score) FROM users_games WHERE game_id = UUID(?)))",
                    gameId, gameId);
        }catch (DataAccessException e){
            System.err.println("Error occurred while setting winners for the game (" + gameId + "): " + e.getMessage());
            return 0;
        }
    }

    @Override
    public List<DtoGameWinner> getWinnersOfTheGame(String gameId) {
        try{
            return jdbcTemplate.query("SELECT ug.user_id, q.title " +
                    "FROM users_games ug INNER JOIN games g ON ug.game_id = g.game_id " +
                            "INNER JOIN quizzes q ON g.quiz_id = q.quiz_id " +
                    "WHERE ug.game_id = uuid(?) AND " +
                    "ug.is_winner = true",
                    new Object[]{gameId},
                    (rs, i) -> DtoGameWinner.builder()
                                .userId(rs.getString("user_id"))
                                .quizTitle(rs.getString("title"))
                                .build());
        }catch (EmptyResultDataAccessException | NullPointerException e){
            return null;
        }
    }
}
