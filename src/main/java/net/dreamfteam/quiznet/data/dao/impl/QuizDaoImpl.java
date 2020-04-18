package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.QuizDao;
import net.dreamfteam.quiznet.data.entities.Question;
import net.dreamfteam.quiznet.data.entities.Quiz;
import net.dreamfteam.quiznet.data.rowmappers.QuestionMapper;
import net.dreamfteam.quiznet.data.rowmappers.QuizMapper;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.web.dto.DtoQuiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class QuizDaoImpl implements QuizDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public QuizDaoImpl(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    @Override
    public Quiz saveQuiz(Quiz quiz) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement ps = con.prepareStatement(
                                "INSERT INTO quizzes (title, description, image_ref, " +
                                        "creator_id, activated, validated, language, ver_creation_datetime) VALUES (?,?,?,?,?,?,?,current_timestamp)",
                                Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, quiz.getTitle());
                        ps.setString(2, quiz.getDescription());
                        ps.setString(3, quiz.getImageRef());
                        ps.setLong(4, quiz.getCreatorId());
                        ps.setBoolean(5, quiz.isActivated());
                        ps.setBoolean(6, quiz.isValidated());
                        ps.setString(7, quiz.getLanguage());
                        return ps;
                    }
                }, keyHolder);
        quiz.setId((Long) keyHolder.getKeys().get("quiz_id"));
        for(int i = 0; i < quiz.getTagList().size(); i++) {
            jdbcTemplate.update("INSERT INTO quizzes_tags (quiz_id, tag_id) VALUES (?,?)",
                    quiz.getId(), quiz.getTagList().get(i));
        }
        for(int i = 0; i < quiz.getCategoryList().size(); i++) {
            jdbcTemplate.update("INSERT INTO categs_quizzes (quiz_id, category_id) VALUES (?,?)",
                    quiz.getId(), quiz.getCategoryList().get(i));
        }
        return quiz;
    }

    @Override
    public Quiz updateQuiz(DtoQuiz dtoQuiz) {
        jdbcTemplate.update(
                "UPDATE quizzes SET title = ?, description = ?, image_ref = ?, language = ? WHERE quiz_id = ?",
                dtoQuiz.getNewTitle(), dtoQuiz.getNewDescription(), dtoQuiz.getNewImageRef(), dtoQuiz.getNewLanguage(), dtoQuiz.getQuizId());
        jdbcTemplate.update("DELETE FROM quizzes_tags WHERE quiz_id = ?", dtoQuiz.getQuizId());
        jdbcTemplate.update("DELETE FROM categs_quizzes WHERE quiz_id = ?", dtoQuiz.getQuizId());
        for(int i = 0; i < dtoQuiz.getNewTagList().size(); i++) {
            jdbcTemplate.update("INSERT INTO quizzes_tags (quiz_id, tag_id) VALUES (?,?)",
                    dtoQuiz.getQuizId(), dtoQuiz.getNewTagList().get(i));
        }
        for(int i = 0; i < dtoQuiz.getNewCategoryList().size(); i++) {
            jdbcTemplate.update("INSERT INTO categs_quizzes (quiz_id, category_id) VALUES (?,?)",
                    dtoQuiz.getQuizId(), dtoQuiz.getNewCategoryList().get(i));
        }
        System.out.println("Updated in db. Quiz id: " + dtoQuiz.getQuizId());
        return getUserQuizByTitle(dtoQuiz.getTitle(), dtoQuiz.getCreatorId());
    }

    @Override
    public Quiz getQuiz(DtoQuiz dtoQuiz) {
        try {
            Quiz quiz =  jdbcTemplate.queryForObject(
                    "SELECT * FROM quizzes WHERE quiz_id = ?",
                    new Object[]{dtoQuiz.getQuizId()},
                    new QuizMapper());
            if(jdbcTemplate.queryForObject(
                    "SELECT count(*) FROM favourite_quizzes WHERE user_id = ? AND quiz_id = ?",
                    new Object[] {dtoQuiz.getUserId(), quiz.getId()}, Long.class) >= 1) {
                quiz.setFavourite(true);
            }
            return quiz;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void markAsFavourite(DtoQuiz dtoQuiz) {
        jdbcTemplate.update(
                "INSERT INTO favourite_quizzes (user_id, quiz_id) VALUES (?,?)",
                dtoQuiz.getUserId(), dtoQuiz.getQuizId());
        System.out.println("Quiz marked as favourite for user " + dtoQuiz.getUserId());
    }

    @Override
    public void deleteQuizById(Long id) {
        //TODO: DELETE QUIZ OPERATION
    }

    @Override
    public Quiz getUserQuizByTitle(String title, long userId) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM quizzes WHERE title = ? AND creator_id = ?",
                    new Object[]{title, userId},
                    new QuizMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public long saveQuestion(Question question) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement ps = con.prepareStatement(
                                "INSERT INTO questions (quiz_id, title, content, image, points, type_id) VALUES (?,?,?,?,?,?)",
                                Statement.RETURN_GENERATED_KEYS);
                        ps.setLong(1, question.getQuizId());
                        ps.setString(2, question.getTitle());
                        ps.setString(3, question.getContent());
                        ps.setString(4, question.getImage());
                        ps.setInt(5, question.getPoints());
                        ps.setInt(6, question.getTypeId());
                        return ps;
                    }
                }, keyHolder);
        System.out.println("Question added in DB. Its ID in database is: " + keyHolder.getKeys().get("question_id"));
        return (long) keyHolder.getKeys().get("question_id");
    }

    @Override
    public void saveFirstTypeAns(Question question) {
        //upd
        for(int i = 0; i < question.getRightOptions().size(); i++) {
            jdbcTemplate.update(
                    "INSERT INTO options (content, is_correct, question_id) VALUES (?,?,?)",
                    question.getRightOptions().get(i), true, question.getId());
        }
        for(int i = 0; i < question.getOtherOptions().size(); i++) {
            jdbcTemplate.update(
                    "INSERT INTO options (content, is_correct, question_id) VALUES (?,?,?)",
                    question.getRightOptions().get(i), false, question.getId());
        }
        System.out.println("First type answers saved in db for question: " + question.toString());
    }

    @Override
    public void saveSecondThirdTypeAns(Question question) {
        jdbcTemplate.update(
                "INSERT INTO one_val_options (value, question_id) VALUES (?,?)",
                question.getRightOptions().get(0), question.getId());
        System.out.println("Second/Third type answers saved in db for question: " + question.toString());
    }

    @Override
    public void saveFourthTypeAns(Question question) {
        for (int i = 0; i < question.getRightOptions().size(); i++) {
            jdbcTemplate.update(
                    "INSERT INTO seq_options (position, content, question_id) VALUES (?,?,?)",
                    i+1, question.getRightOptions().get(i), question.getId());
        }
        System.out.println("Fourth type answers saved in db for question: " + question.toString());
    }

    @Override
    public void deleteQuestion(Question question) {
        jdbcTemplate.update("DELETE FROM questions WHERE question_id = ?", question.getId());
    }

    @Override
    public List<Question> getQuestionList(Question question) {
        try {
            List<Question> listQ = jdbcTemplate.query(
                    "SELECT * FROM questions WHERE quiz_id = ?",
                    new Object[]{question.getQuizId()},
                    new QuestionMapper());
            for (int i = 0; i < listQ.size(); i++) {
                listQ.set(i,loadAnswersForQuestion(listQ.get(i), i));
            }
            return listQ;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private Question loadAnswersForQuestion(Question question, int i) {
        switch(question.getTypeId()) {
            case (1):
                question.setRightOptions(jdbcTemplate.queryForList(
                        "SELECT content FROM options WHERE question_id = ? AND is_correct = true",
                        new Object[]{question.getId()}, String.class));

                question.setOtherOptions(jdbcTemplate.queryForList(
                        "SELECT content FROM options WHERE question_id = ? AND is_correct = false",
                        new Object[]{question.getId()}, String.class));
                return question;
            case (2):
            case (3):
                question.setRightOptions(jdbcTemplate.queryForList(
                        "SELECT value FROM one_val_options WHERE question_id = ?",
                        new Object[]{question.getId()}, String.class));
                return question;
            case (4):
                question.setRightOptions(jdbcTemplate.queryForList(
                        "SELECT content FROM seq_options WHERE question_id = ? ORDER BY position;",
                        new Object[]{question.getId()}, String.class));
                return question;
            default: return null;
        }
    }
}
