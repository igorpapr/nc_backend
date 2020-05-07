package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.GameDao;
import net.dreamfteam.quiznet.data.entities.Answer;
import net.dreamfteam.quiznet.data.entities.Game;
import net.dreamfteam.quiznet.data.entities.Question;
import net.dreamfteam.quiznet.data.rowmappers.GameMapper;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
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

    @Override
    public Answer saveAnswer(Answer answer) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "insert into answers (time_of_answer, users_game_id ) values (?, uuid(?)) returning answer_id;", Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, new Timestamp(answer.getTimeOfAnswer().getTime()));
            ps.setString(2, answer.getSessionId());
            return ps;
        }, keyHolder);

        answer.setAnswerId(Objects.requireNonNull(keyHolder.getKeys()).get("answer_id").toString());

        switch (answer.getTypeId()) {
            case 2:
            case 3:
                saveAnswerAndScoreSecondThird(answer);
                break;
            case 1:
                saveFirstTypeAnswer(answer);
                break;
            case 4:
                saveFourthAnswer(answer);
        }

        return answer;
    }

    // might be used later for randomized questions
    private Integer getAnsweredQuestionsAmount() {
        return jdbcTemplate.queryForObject("select count(*) from questions left join games on games.quiz_id = questions.quiz_id where game_id = UUID(?) and question_id not in (select question_id from answers);", Integer.class);
    }

    private void saveFourthAnswer(Answer answer) {
        int idx = 1;
        for (String s :
                answer.getAnswer()) {
            jdbcTemplate.update(
                    "insert into ans_seq_options (answer_id,  seq_pos, option_id) values (uuid(?), ?, " +
                            "(select seq_options.seq_option_id from seq_options where question_id = uuid(?) and content = ?));",
                    answer.getAnswerId(), idx++, answer.getQuestionId(), s);

        }

        jdbcTemplate.update("update answers set gained_points = ( select points *(select count(*) from seq_options s" +
                " join ans_seq_options an on s.seq_option_id = an.option_id where an.seq_pos = s.seq_pos and answer_id = uuid(?) )/ ? from questions where question_id = uuid(?))  where answer_id =uuid(?)", answer.getAnswerId(), answer.getAnswer().size(), answer.getQuestionId(), answer.getAnswerId());

    }

    private void saveAnswerAndScoreSecondThird(Answer answer) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "insert into ans_one_options (answer_id, value , is_correct) values (uuid(?), ?, (select one_val_options.value = ? from one_val_options where one_val_options.question_id = uuid(?) ));", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, answer.getAnswerId());
            ps.setString(2, answer.getAnswer().get(0));
            ps.setString(3, answer.getAnswer().get(0));
            ps.setString(4, answer.getQuestionId());
            return ps;
        }, keyHolder);

        System.out.println(keyHolder.getKeyList());
        if (Objects.requireNonNull(keyHolder.getKeys()).get("is_correct").toString().equals("true")) {
            jdbcTemplate.update(
                    "update answers SET gained_points = (select points from questions where question_id = uuid(?)) where answer_id = uuid(?)",
                    answer.getQuestionId(), answer.getAnswerId());
        }

    }

    private void saveFirstTypeAnswer(Answer answer) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "insert into ans_options (answer_id, option_id) values (uuid(?), (select option_id from options where options.content = ? and  options.question_id = uuid(?)));", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, answer.getAnswerId());
            ps.setString(2, answer.getAnswer().get(0));
            ps.setString(3, answer.getQuestionId());
            ps.setString(3, answer.getQuestionId());
            return ps;
        }, keyHolder);

        String optionid = Objects.requireNonNull(keyHolder.getKeys()).get("option_id").toString();

        jdbcTemplate.update("update answers SET gained_points =" +
                        "    CASE WHEN (select is_correct  from options  where option_id = uuid(?))" +
                        "THEN (select points from questions where question_id = (select question_id from options  where option_id = uuid(?))) else (0) end where answer_id = uuid(?)",
                optionid, optionid, answer.getAnswerId());

    }




}
