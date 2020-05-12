package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.QuizDao;
import net.dreamfteam.quiznet.data.entities.*;
import net.dreamfteam.quiznet.data.rowmappers.QuizMapper;
import net.dreamfteam.quiznet.web.dto.DtoQuiz;
import net.dreamfteam.quiznet.web.dto.DtoQuizFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.List;
import java.util.Map;

@Repository
public class QuizDaoImpl implements QuizDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public QuizDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Quiz saveQuiz(Quiz quiz) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("INSERT INTO quizzes (title, description, creator_id, activated, validated, quiz_lang, ver_creation_datetime, rating, published) VALUES (?,?,?,?,?,?,current_timestamp,?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, quiz.getTitle());
                ps.setString(2, quiz.getDescription());
                ps.setObject(3, java.util.UUID.fromString(quiz.getCreatorId()));
                ps.setBoolean(4, quiz.isActivated());
                ps.setBoolean(5, quiz.isValidated());
                ps.setString(6, quiz.getLanguage());
                ps.setInt(7, 0);
                ps.setBoolean(8, quiz.isPublished());
                return ps;
            }
        }, keyHolder);
        quiz.setId(keyHolder.getKeys().get("quiz_id").toString());
        for (int i = 0; i < quiz.getTagIdList().size(); i++) {
            jdbcTemplate.update("INSERT INTO quizzes_tags (quiz_id, tag_id) VALUES (UUID(?),UUID(?))", quiz.getId(), quiz.getTagIdList().get(i));
        }
        for (int i = 0; i < quiz.getCategoryIdList().size(); i++) {
            jdbcTemplate.update("INSERT INTO categs_quizzes (quiz_id, category_id) VALUES (UUID(?),UUID(?))", quiz.getId(), quiz.getCategoryIdList().get(i));
        }
        quiz.setTagNameList(loadTagNameList(quiz.getId()));
        quiz.setCategoryNameList(loadCategoryNameList(quiz.getId()));
        quiz.setAuthor(jdbcTemplate.queryForObject("SELECT username FROM users WHERE user_id = UUID(?)", new Object[]{quiz.getCreatorId()}, String.class));
        return quiz;
    }

    @Override
    public Quiz updateQuiz(Quiz quiz, String oldQuizId) {
        if(jdbcTemplate.queryForObject("SELECT published FROM quizzes WHERE quiz_id = UUID(?)", new Object[]{oldQuizId}, Boolean.class) == true) {
            quiz = saveQuiz(quiz);
            jdbcTemplate.update("INSERT INTO quizzes_edit (prev_ver_id, new_ver_id, edit_datetime) VALUES (UUID(?), UUID(?), current_timestamp)", oldQuizId, quiz.getId());
            List<Question> qlist = getQuestionList(oldQuizId);
            for (int i = 0; i < qlist.size(); i++) {
                qlist.get(i).setQuizId(quiz.getId());
                saveQuestion(qlist.get(i));
            }
            System.out.println("Updated in db. New quiz id: " + quiz.getId() + "Old quiz id: " + oldQuizId);
        } else {
            jdbcTemplate.update("UPDATE quizzes SET title = ?, description = ?, quiz_lang = ? WHERE quiz_id = UUID(?)",
                    quiz.getTitle(), quiz.getDescription(), quiz.getLanguage(), oldQuizId);

            for (int i = 0; i < quiz.getTagIdList().size(); i++) {
                jdbcTemplate.update("INSERT INTO quizzes_tags (quiz_id, tag_id) VALUES (UUID(?),UUID(?)) ON CONFLICT DO NOTHING", oldQuizId, quiz.getTagIdList().get(i));
            }
            for (int i = 0; i < quiz.getCategoryIdList().size(); i++) {
                jdbcTemplate.update("INSERT INTO categs_quizzes (quiz_id, category_id) VALUES (UUID(?),UUID(?)) ON CONFLICT DO NOTHING", oldQuizId, quiz.getCategoryIdList().get(i));
            }
            quiz.setTagNameList(loadTagNameList(oldQuizId));
            quiz.setCategoryNameList(loadCategoryNameList(oldQuizId));
            quiz.setAuthor(jdbcTemplate.queryForObject("SELECT username FROM users WHERE user_id = UUID(?)", new Object[]{quiz.getCreatorId()}, String.class));
            quiz.setId(oldQuizId);
        }
        System.out.println("Updated in db. Quiz id: " + oldQuizId);
        return quiz;
    }

    @Override
    public Quiz getQuiz(String quizId, String userId) {
        try {
            Quiz quiz = jdbcTemplate.queryForObject("SELECT * FROM quizzes WHERE quiz_id = UUID(?)", new Object[]{quizId}, new QuizMapper());
            if(userId != null) {
                if (jdbcTemplate.queryForObject("SELECT count(*) FROM favourite_quizzes WHERE user_id = UUID(?) AND quiz_id = UUID(?)", new Object[]{userId, quiz.getId()}, Long.class) >= 1) {
                    quiz.setFavourite(true);
                }
            }
            quiz.setTagNameList(loadTagNameList(quiz.getId()));
            quiz.setCategoryNameList(loadCategoryNameList(quiz.getId()));
            quiz.setTagIdList(loadTagIdList(quiz.getId()));
            quiz.setCategoryIdList(loadCategoryIdList(quiz.getId()));
            quiz.setAuthor(jdbcTemplate.queryForObject("SELECT username FROM users WHERE user_id = UUID(?)", new Object[]{quiz.getCreatorId()}, String.class));
            return quiz;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Quiz getQuiz(String quizId) {
        try {
            Quiz quiz = jdbcTemplate.queryForObject("SELECT * FROM quizzes WHERE quiz_id = UUID(?)", new Object[]{quizId}, new QuizMapper());
            quiz.setTagNameList(loadTagNameList(quiz.getId()));
            quiz.setCategoryNameList(loadCategoryNameList(quiz.getId()));
            quiz.setTagIdList(loadTagIdList(quiz.getId()));
            quiz.setCategoryIdList(loadCategoryIdList(quiz.getId()));
            quiz.setAuthor(jdbcTemplate.queryForObject("SELECT username FROM users WHERE user_id = UUID(?)", new Object[] { quiz.getCreatorId() }, String.class));
            return quiz;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void markAsFavourite(DtoQuiz dtoQuiz) {
        if(jdbcTemplate.queryForObject("SELECT count(*) FROM favourite_quizzes WHERE user_id = UUID(?) AND quiz_id = UUID(?)", new Object[]{dtoQuiz.getUserId(), dtoQuiz.getQuizId()}, Long.class) >= 1) {
            jdbcTemplate.update("DELETE FROM favourite_quizzes WHERE user_id = UUID(?) AND quiz_id = UUID(?)", dtoQuiz.getUserId(), dtoQuiz.getQuizId());
            System.out.println("Quiz removed from favourites");
        } else {
            jdbcTemplate.update("INSERT INTO favourite_quizzes (user_id, quiz_id) VALUES (UUID(?),UUID(?))", dtoQuiz.getUserId(), dtoQuiz.getQuizId());
            System.out.println("Quiz marked as favourite for user " + dtoQuiz.getUserId());
        }
    }

    @Override
    public void markAsPublished(DtoQuiz dtoQuiz) {
        jdbcTemplate.update("UPDATE quizzes SET published = true WHERE quiz_id = UUID(?)", dtoQuiz.getQuizId());
        System.out.println("Quiz marked as published");
    }

    @Override
    @Transactional
    public void deleteQuizById(String id) {
        jdbcTemplate.update("DELETE FROM quizzes WHERE quiz_id = UUID(?)", id);
        for (Question q : getQuestionList(id)) {
            deleteQuestion(q);
        }
        System.out.println("Quiz deleted");
    }

    @Override
    public void deactivateQuiz(DtoQuiz dtoQuiz) {
        jdbcTemplate.update("UPDATE quizzes SET activated = false WHERE quiz_id = UUID(?)", dtoQuiz.getQuizId());
        System.out.println("Quiz deactivated");
    }


    @Override
    public Quiz getUserQuizByTitle(String title, String userId) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM quizzes " +
                    "WHERE title = ? AND creator_id = UUID(?)", new Object[]{title, userId}, new QuizMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional
    @Override
    public void validateQuiz(DtoQuiz dtoQuiz) {

        jdbcTemplate.update("UPDATE quizzes SET validated = true, published = ?, activated = ?, admin_commentary = ?, validator_id = uuid(?), validation_date = current_timestamp WHERE quiz_id = UUID(?)", dtoQuiz.isValidated(), dtoQuiz.isValidated(), dtoQuiz.getAdminCommentary(), dtoQuiz.getValidator_id(), dtoQuiz.getQuizId());

        if (dtoQuiz.isValidated()) {
            try {
                String oldQuizId = jdbcTemplate.queryForObject("SELECT prev_ver_id FROM quizzes_edit WHERE new_ver_id = UUID(?)", new Object[]{dtoQuiz.getQuizId()}, String.class);
                deleteQuizById(oldQuizId);
                System.out.println("Validate quiz in db. Quiz id: " + dtoQuiz.getQuizId() + ". Delete old version. Quiz id: " + oldQuizId);
            } catch (EmptyResultDataAccessException e) {
                System.out.println("Validate quiz in db. Quiz id: " + dtoQuiz.getQuizId());
            }
        }
    }

    @Override
    public List<QuizFiltered> findQuizzesByFilter(DtoQuizFilter quizFilter, int startIndex, int amount) {
        String sql = "SELECT q.quiz_id, q.title, q.description, q.ver_creation_datetime, " +
                 "q.creator_id, q.activated, q.validated, q.quiz_lang, q.rating, q.image, u.username " +
                "FROM quizzes q INNER JOIN users u ON q.creator_id = u.user_id " +
                "WHERE activated = true AND validated = true; ";
        if (quizFilter.getQuizName() != null) {

            sql = sql + "title ILIKE '%" + quizFilter.getQuizName() + "%' AND ";
        }
        if (quizFilter.getUserName() != null) {
            sql = sql + "creator_id = (SELECT user_id FROM users WHERE username LIKE '" + quizFilter.getUserName() + "') AND ";
        }
        if (quizFilter.getMoreThanRating() > 0) {
            sql = sql + "rating >= " + quizFilter.getMoreThanRating() + " AND ";
        }
        if (quizFilter.getLessThanRating() > 0) {
            sql = sql + "rating <= " + quizFilter.getLessThanRating() + " AND ";
        }
        if (quizFilter.getTags() != null && quizFilter.getTags().size() > 0) {
            for (int i = 0; i < quizFilter.getTags().size(); i++) {
                sql = sql + "quiz_id IN (SELECT quiz_id FROM quizzes_tags WHERE tag_id = '" + quizFilter.getTags().get(i) + "') AND ";
            }
        }
        if (quizFilter.getCategories() != null && quizFilter.getCategories().size() > 0) {
            for (int i = 0; i < quizFilter.getCategories().size(); i++) {
                sql = sql + "quiz_id IN (SELECT quiz_id FROM categs_quizzes WHERE category_id = '" + quizFilter.getCategories().get(i) + "') AND ";
            }
        }

        sql = sql.substring(0, sql.length()-4);
        if(quizFilter.getOrderByRating() != null && quizFilter.getOrderByRating() == true) {
            sql = sql + "ORDER BY rating DESC LIMIT ? OFFSET ?";
        } else {sql = sql + " ORDER BY ver_creation_datetime DESC LIMIT ? OFFSET ?";}
        System.out.println("FILTERED");
        try {
            List<QuizFiltered> quizList = jdbcTemplate.query(sql, new Object[]{amount, startIndex}, (rs, i) -> QuizFiltered.builder()
                    .id(rs.getString("quiz_id"))
                    .title(rs.getString("title"))
                    .description(rs.getString("description"))
                    .creationDate(rs.getDate("ver_creation_datetime"))
                    .creatorId(rs.getString("creator_id"))
                    .language(rs.getString("quiz_lang"))
                    .rating(rs.getFloat("rating"))
                    .imageContent(rs.getBytes("image"))
                    .author(rs.getString("username")).build()
            );

            return quizList;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<QuizValid> getValidQuizzes(int startIndex, int amount, String adminId) {
        try {
            return jdbcTemplate.query("SELECT quiz_id, title, description, q.image AS image_content, " +
                    "ver_creation_datetime, creator_id, username, quiz_lang, admin_commentary " +
                    "FROM quizzes q INNER JOIN users u ON q.creator_id = u.user_id " +
                    "WHERE validated = true AND validator_id = UUID(?) LIMIT ? OFFSET ?;",
                    new Object[]{adminId, amount, startIndex},
                    (rs, i) -> QuizValid.builder()
                            .id(rs.getString("quiz_id"))
                            .title(rs.getString("title"))
                            .description(rs.getString("description"))
                            .imageContent(rs.getBytes("image_content"))
                            .creationDate(rs.getDate("ver_creation_datetime"))
                            .creatorId(rs.getString("creator_id"))
                            .username(rs.getString("username"))
                            .language(rs.getString("quiz_lang"))
                            .adminComment(rs.getString("admin_commentary"))
                            .build());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int getInvalidQuizzesTotalSize() {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) AS total_size FROM quizzes WHERE validated = false AND published = true", Integer.class);
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return 0;
        }
    }

    @Override
    public int getValidQuizzesTotalSize(String adminId) {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) AS total_size FROM quizzes WHERE validated = true AND validator_id = UUID(?)", new Object[]{adminId}, Integer.class);
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return 0;
        }
    }

    @Override
    @Transactional
    public Quiz setValidator(String quizId, String adminId) {
        jdbcTemplate.update("UPDATE quizzes SET validator_id = uuid(?) WHERE quiz_id = UUID(?)", adminId, quizId);
        return getQuiz(quizId);
    }

    @Override
    public void addQuizImage(String imageId, String quizId) {
        jdbcTemplate.update("UPDATE quizzes SET image_ref = UUID(?) WHERE quiz_id = UUID(?)", imageId, quizId);
        System.out.println("Quiz image saved in db");
    }

    @Override
    public void addQuestionImage(String imageId, String questionId) {
        jdbcTemplate.update("UPDATE questions SET image = UUID(?) WHERE question_id = UUID(?)", imageId, questionId);
        System.out.println("Question image saved in db");
    }

    @Override
    public String saveQuestion(Question question) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("INSERT INTO questions (quiz_id, title, content, points, type_id) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setObject(1, java.util.UUID.fromString(question.getQuizId()));
                ps.setString(2, question.getTitle());
                ps.setString(3, question.getContent());
                ps.setInt(4, question.getPoints());
                ps.setInt(5, question.getTypeId());
                return ps;
            }
        }, keyHolder);
        System.out.println("Question added in DB. Its ID in database is: " + keyHolder.getKeys().get("question_id"));
        return keyHolder.getKeys().get("question_id").toString();
    }

    @Override
    public void saveFirstTypeAns(Question question) {
        for (int i = 0; i < question.getRightOptions().size(); i++) {
            jdbcTemplate.update("INSERT INTO options (content, is_correct, question_id) VALUES (?,?,UUID(?))", question.getRightOptions().get(i), true, question.getId());
        }
        for (int i = 0; i < question.getOtherOptions().size(); i++) {
            jdbcTemplate.update("INSERT INTO options (content, is_correct, question_id) VALUES (?,?,UUID(?))", question.getOtherOptions().get(i), false, question.getId());
        }
        System.out.println("First type answers saved in db for question: " + question.toString());
    }

    @Override
    public void saveSecondThirdTypeAns(Question question) {
        jdbcTemplate.update("INSERT INTO one_val_options (value, question_id) VALUES (?,UUID(?))", question.getRightOptions().get(0), question.getId());
        System.out.println("Second/Third type answers saved in db for question: " + question.toString());
    }

    @Override
    public void saveFourthTypeAns(Question question) {
        for (int i = 0; i < question.getRightOptions().size(); i++) {
            jdbcTemplate.update("INSERT INTO seq_options (seq_pos, content, question_id) VALUES (?,?,UUID(?))", i + 1, question.getRightOptions().get(i), question.getId());
        }
        System.out.println("Fourth type answers saved in db for question: " + question.toString());
    }

    @Override
    public void deleteQuestion(Question question) {
        jdbcTemplate.update("DELETE FROM questions WHERE question_id = UUID(?)", question.getId());
    }

    @Override
    public List<Question> getQuestionList(String quizId) {
        try {

            List<Question> listQ = jdbcTemplate.query("SELECT q.question_id, q.quiz_id, q.title, q.content, " +
                            "q.image, q.points, q.type_id as imgcontent " +
                            "FROM questions q WHERE q.quiz_id = UUID(?)",

                    new Object[]{quizId},  (rs, i) -> Question.builder()
                            .id(rs.getString("question_id"))
                            .quizId(rs.getString("quiz_id"))
                            .title(rs.getString("title"))
                            .content(rs.getString("content"))
                            .image(rs.getString("image"))
                            .points(rs.getInt("points"))
                            .typeId(rs.getInt("type_id"))
                            .imageContent(rs.getBytes("image"))
                            .build());

            for (int i = 0; i < listQ.size(); i++) {
                listQ.set(i, loadAnswersForQuestion(listQ.get(i), i));
            }
            return listQ;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Map<String, String>> getTagList() {
        try {
            List listT = jdbcTemplate.queryForList("SELECT tag_id, description FROM tags", new Object[]{});
            return listT;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<List<Object>> getCategoryList() {
        try {
            List listT = jdbcTemplate.queryForList("SELECT category_id, title, description, cat_image_ref FROM categories", new Object[]{});
            return listT;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }



    @Override
    public List<Quiz> getUserQuizList(String userId) {
        try {
            return jdbcTemplate.query("SELECT * FROM quizzes WHERE creator_id = UUID(?) ", new Object[]{userId},  (rs, i) ->Quiz.builder()
                    .id(rs.getString("quiz_id"))
                    .title(rs.getString("title"))
                    .description(rs.getString("description"))
                    .imageContent(rs.getBytes("image"))
                    .creationDate(rs.getDate("ver_creation_datetime"))
                    .creatorId(rs.getString("creator_id"))
                    .activated(rs.getBoolean("activated"))
                    .validated(rs.getBoolean("validated"))
                    .published(rs.getBoolean("published"))
                    .language(rs.getString("quiz_lang"))
                    .adminComment(rs.getString("admin_commentary"))
                    .rating(rs.getFloat("rating"))
                    .isFavourite(false)
                    .build());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<QuizView> getQuizzes(int startIndex, int amount) {
        try {
            return jdbcTemplate.query("SELECT quiz_id, title, q.image AS image_content" +
                    " FROM quizzes q " +
                    "WHERE validated = true AND activated = true " +
                    "AND published = true " +
                    "ORDER BY rating DESC LIMIT ? OFFSET ? ;",

                    new Object[]{amount, startIndex}, (rs, i) ->
                            QuizView.builder()
                                    .quiz_id(rs.getString("quiz_id"))
                                    .title(rs.getString("title"))
                                    .image_content(rs.getBytes("image_content")).build());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<QuizValid> getInvalidQuizzes(int startIndex, int amount, String adminId) {
        try {
            return jdbcTemplate.query("SELECT quiz_id, title, description, q.image AS image_content, " +
                    "ver_creation_datetime, creator_id, username, quiz_lang, admin_commentary " +
                    "FROM quizzes q INNER JOIN users u ON q.creator_id = u.user_id " +
                    "WHERE validated = false AND (validator_id IS NULL OR validator_id = uuid(?)) " +
                    "AND published = true LIMIT ? OFFSET ?;",

                    new Object[]{adminId, amount, startIndex}, (rs, i) ->
                            QuizValid.builder()
                                    .id(rs.getString("quiz_id"))
                                    .title(rs.getString("title"))
                                    .description(rs.getString("description"))
                                    .imageContent(rs.getBytes("image_content"))
                                    .creationDate(rs.getDate("ver_creation_datetime"))
                                    .creatorId(rs.getString("creator_id"))
                                    .username(rs.getString("username"))
                                    .language(rs.getString("quiz_lang"))
                                    .adminComment(rs.getString("admin_commentary"))
                                    .build());

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int getQuizzesTotalSize() {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) AS total_size FROM quizzes WHERE validated = true AND activated = true AND published = true", Integer.class);
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return 0;
        }
    }

    @Override
    public int getQuestionsAmountInQuiz(String quizId) {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) AS total_size FROM questions WHERE quiz_id = uuid(?)", new Object[]{quizId}, Integer.class);

        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return 0;
        }
    }

    @Override
    public List<Question> getQuestionsInPage(int startIndex, int amount, String quizId) {
        try {
            List<Question> listQ = jdbcTemplate.query("SELECT q.question_id, q.quiz_id, q.title, q.content, " +
                            "q.points, q.type_id, q.img as imgcontent " +
                            "FROM questions q " +
                            "WHERE q.quiz_id = UUID(?) LIMIT ? OFFSET ?",

                    new Object[]{quizId,amount,startIndex},  (rs, i) -> Question.builder()
                            .id(rs.getString("question_id"))
                            .quizId(rs.getString("quiz_id"))
                            .title(rs.getString("title"))
                            .content(rs.getString("content"))
                            .points(rs.getInt("points"))
                            .typeId(rs.getInt("type_id"))
                            .imageContent(rs.getBytes("imgcontent"))
                            .build());
            for (int i = 0; i < listQ.size(); i++) {
                listQ.set(i, loadAnswersForQuestion(listQ.get(i), i));
            }
            return listQ;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }




     public Question loadAnswersForQuestion(Question question, int i) {
        switch (question.getTypeId()) {
            case (1):
                question.setRightOptions(jdbcTemplate.queryForList("SELECT content FROM options WHERE question_id = UUID(?) AND is_correct = true", new Object[]{question.getId()}, String.class));

                question.setOtherOptions(jdbcTemplate.queryForList("SELECT content FROM options WHERE question_id = UUID(?) AND is_correct = false", new Object[]{question.getId()}, String.class));
                return question;
            case (2):
            case (3):
                question.setRightOptions(jdbcTemplate.queryForList("SELECT value FROM one_val_options WHERE question_id = UUID(?)", new Object[]{question.getId()}, String.class));
                return question;
            case (4):
                question.setRightOptions(jdbcTemplate.queryForList("SELECT content FROM seq_options WHERE question_id = UUID(?) ORDER BY seq_pos;", new Object[]{question.getId()}, String.class));
                return question;
            default:
                return null;
        }
    }

    private List<String> loadTagNameList(String quizId) {
        return jdbcTemplate.query("SELECT t.description FROM tags t " + "INNER JOIN quizzes_tags qt ON t.tag_id = qt.tag_id WHERE quiz_id = UUID(?)", new Object[]{quizId}, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        });
    }

    private List<String> loadCategoryNameList(String quizId) {
        return jdbcTemplate.query("SELECT c.title FROM categories c " + "INNER JOIN categs_quizzes cq ON c.category_id = cq.category_id WHERE quiz_id = UUID(?)", new Object[]{quizId}, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        });
    }

    private List<String> loadTagIdList(String quizId) {
        return jdbcTemplate.query("SELECT tag_id FROM quizzes_tags WHERE quiz_id = UUID(?)", new Object[]{quizId}, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        });
    }

    private List<String> loadCategoryIdList(String quizId) {
        return jdbcTemplate.query("SELECT category_id FROM categs_quizzes WHERE quiz_id = UUID(?)", new Object[]{quizId}, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        });
    }
}
