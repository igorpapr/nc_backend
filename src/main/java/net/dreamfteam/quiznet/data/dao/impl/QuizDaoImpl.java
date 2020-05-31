package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.configs.constants.SqlConstants;
import net.dreamfteam.quiznet.data.dao.QuizDao;
import net.dreamfteam.quiznet.data.entities.*;
import net.dreamfteam.quiznet.data.rowmappers.QuizFilteredMapper;
import net.dreamfteam.quiznet.data.rowmappers.QuizMapper;
import net.dreamfteam.quiznet.data.rowmappers.QuizValidMapper;
import net.dreamfteam.quiznet.data.rowmappers.RatingMapper;
import net.dreamfteam.quiznet.web.dto.*;
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
    public Quiz saveQuiz(Quiz quiz, String language) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(SqlConstants.QUIZ_SAVE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, quiz.getTitle());
                ps.setString(2, quiz.getDescription());
                ps.setObject(3, java.util.UUID.fromString(quiz.getCreatorId()));
                ps.setBoolean(4, quiz.isActivated());
                ps.setBoolean(5, quiz.isValidated());
                ps.setString(6, quiz.getLanguage());
                ps.setBoolean(7, quiz.isPublished());
                ps.setBytes(8, quiz.getImageContent());
            return ps;
        }, keyHolder);
        quiz.setId(keyHolder.getKeys()
                .get("quiz_id")
                .toString());
        for (int i = 0; i < quiz.getTagIdList()
                .size(); i++) {
            jdbcTemplate.update(SqlConstants.QUIZ_SAVE_QUIZ_TAGS, quiz.getId(), quiz.getTagIdList().get(i));
        }
        for (int i = 0; i < quiz.getCategoryIdList()
                .size(); i++) {
            jdbcTemplate.update(SqlConstants.QUIZ_SAVE_QUIZ_CATEGS, quiz.getId(), quiz.getCategoryIdList().get(i));
        }
        quiz.setTagNameList(loadTagNameList(quiz.getId()));
        quiz.setCategoryNameList(loadCategoryNameList(quiz.getId(),language));
        quiz.setAuthor(jdbcTemplate.queryForObject(SqlConstants.QUIZ_SAVE_GET_AUTHOR,
                new Object[]{quiz.getCreatorId()}, String.class));
        return quiz;
    }

    @Override
    public Quiz updateQuiz(Quiz quiz, String oldQuizId, String language) {
        if (jdbcTemplate.queryForObject(SqlConstants.QUIZ_UPDATE_IS_PUBLISHED, new Object[]{oldQuizId}, Boolean.class) == true) {
            quiz = saveQuiz(quiz,language);
            jdbcTemplate.update(SqlConstants.QUIZ_UPDATE_QUZZES_EDIT, oldQuizId, quiz.getId());

            List<Question> qlist = getQuestionList(oldQuizId);
            for (int i = 0; i < qlist.size(); i++) {
                qlist.get(i)
                        .setQuizId(quiz.getId());
                saveQuestion(qlist.get(i));
            }
        } else {
            jdbcTemplate.update(SqlConstants.QUIZ_UPDATE,
                    quiz.getTitle(), quiz.getDescription(), quiz.getLanguage(), quiz.getImageContent(),
                    oldQuizId);

            for (int i = 0; i < quiz.getTagIdList().size(); i++) {
                jdbcTemplate.update(
                        SqlConstants.QUIZ_UPDATE_TAGS, oldQuizId, quiz.getTagIdList().get(i));
            }
            for (int i = 0; i < quiz.getCategoryIdList().size(); i++) {
                jdbcTemplate.update(SqlConstants.QUIZ_UPDATE_CATEGS, oldQuizId, quiz.getCategoryIdList().get(i));
            }
            quiz.setTagNameList(loadTagNameList(oldQuizId));
            quiz.setCategoryNameList(loadCategoryNameList(oldQuizId,language));
            quiz.setAuthor(jdbcTemplate.queryForObject(SqlConstants.QUIZ_SAVE_GET_AUTHOR, new Object[]{quiz.getCreatorId()}, String.class));
            quiz.setId(oldQuizId);
        }
        return quiz;
    }

    @Override
    public Quiz getQuiz(String quizId, String userId, String language) {
        try {
            Quiz quiz =
                    jdbcTemplate.queryForObject(SqlConstants.QUIZ_GET, new Object[]{quizId},
                            new QuizMapper());
            if (userId != null) {
                if (jdbcTemplate.queryForObject(
                        SqlConstants.QUIZ_GET_IS_FAVOURITE,
                        new Object[]{userId, quiz.getId()}, Long.class) >= 1) {
                    quiz.setFavourite(true);
                }
            }
            quiz.setTagNameList(loadTagNameList(quiz.getId()));
            quiz.setCategoryNameList(loadCategoryNameList(quiz.getId(),language));
            quiz.setTagIdList(loadTagIdList(quiz.getId()));
            quiz.setCategoryIdList(loadCategoryIdList(quiz.getId()));
            quiz.setAuthor(jdbcTemplate.queryForObject(SqlConstants.QUIZ_SAVE_GET_AUTHOR,
                    new Object[]{quiz.getCreatorId()}, String.class));
            return quiz;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Quiz getQuiz(String quizId, String language) {
        try {
            Quiz quiz =
                    jdbcTemplate.queryForObject(SqlConstants.QUIZ_GET, new Object[]{quizId},
                            new QuizMapper());
            quiz.setTagNameList(loadTagNameList(quiz.getId()));
            quiz.setCategoryNameList(loadCategoryNameList(quiz.getId(),language));
            quiz.setTagIdList(loadTagIdList(quiz.getId()));
            quiz.setCategoryIdList(loadCategoryIdList(quiz.getId()));
            quiz.setAuthor(jdbcTemplate.queryForObject(SqlConstants.QUIZ_SAVE_GET_AUTHOR,
                    new Object[]{quiz.getCreatorId()}, String.class));
            return quiz;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void markAsFavourite(DtoQuiz dtoQuiz) {
        if (jdbcTemplate.queryForObject(SqlConstants.QUIZ_GET_IS_FAVOURITE, new Object[]{dtoQuiz.getUserId(), dtoQuiz.getQuizId()}, Long.class) >= 1) {
            jdbcTemplate.update(SqlConstants.QUIZ_FAVOURITES_DELETE, dtoQuiz.getUserId(), dtoQuiz.getQuizId());
        } else {
            jdbcTemplate.update(SqlConstants.QUIZ_FAVOURITES_INSERT,
                    dtoQuiz.getUserId(), dtoQuiz.getQuizId());
        }
    }

    @Override
    public void markAsPublished(DtoQuiz dtoQuiz) {
        jdbcTemplate.update(SqlConstants.QUIZ_MARK_AS_PUBLISHED, dtoQuiz.getQuizId());
    }

    @Override
    @Transactional
    public void deleteQuizById(String id) {
        jdbcTemplate.update(SqlConstants.QUIZ_DELETE_BY_ID, id);
        for (Question q : getQuestionList(id)) {
            deleteQuestion(q);
        }
    }

    @Override
    public void deactivateQuiz(String id) {
        jdbcTemplate.update(SqlConstants.QUIZ_DEACTIVATE, id);
    }


    @Override
    public Quiz getUserQuizByTitle(String title, String userId) {
        try {
            return jdbcTemplate.queryForObject(SqlConstants.QUIZ_GET_USER_QUIZ_BY_TITLE,
                    new Object[]{title, userId}, new QuizMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional
    @Override
    public int validateQuiz(DtoQuiz dtoQuiz) {

        int rows = jdbcTemplate.update(
                SqlConstants.QUIZ_VALIDATE, dtoQuiz.isValidated(), dtoQuiz.isValidated(), dtoQuiz.getAdminCommentary(),
                dtoQuiz.getValidator_id(), dtoQuiz.getQuizId());

        if (dtoQuiz.isValidated()) {
            try {
                String oldQuizId = jdbcTemplate.queryForObject(SqlConstants.QUIZ_VALIDATE_GET_OLD_QUIZ_ID, new Object[]{dtoQuiz.getQuizId()}, String.class);
                deleteQuizById(oldQuizId);
                System.out.println("Validate quiz in db. Quiz id: " + dtoQuiz.getQuizId() + ". Delete old version. Quiz id: " + oldQuizId);
            } catch (EmptyResultDataAccessException e) {
                System.out.println("Validate quiz in db. Quiz id: " + dtoQuiz.getQuizId());
            }
        }
        return rows;
    }

    @Override
    public List<QuizFiltered> findQuizzesByFilter(DtoQuizFilter quizFilter, int startIndex, int amount) {
        String sql = SqlConstants.QUIZ_FILTER_INITIAL;
        sql = filterSqlGeneration(quizFilter, sql);

        if (quizFilter.getOrderByRating() != null && quizFilter.getOrderByRating() == true) {
            sql = sql + "ORDER BY rating DESC LIMIT ? OFFSET ?";
        } else {
            sql = sql + " ORDER BY ver_creation_datetime DESC LIMIT ? OFFSET ?";
        }
        try {
            List<QuizFiltered> quizList =
                    jdbcTemplate.query(sql, new Object[]{amount, startIndex}, new QuizFilteredMapper());

            return quizList;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private String filterSqlGeneration(DtoQuizFilter quizFilter, String sql) {
        if (quizFilter.getQuizName() != null) {
            sql = sql + "title ILIKE '%" + quizFilter.getQuizName() + "%' AND ";
        }
        if (quizFilter.getUserName() != null) {
            sql = sql + "creator_id = (SELECT user_id FROM users WHERE username LIKE '" + quizFilter.getUserName() +
                  "') AND ";
        }
        if (quizFilter.getMoreThanRating() > 0) {
            sql = sql + "quiz_rating(q.quiz_id) >= " + quizFilter.getMoreThanRating() + " AND ";
        }
        if (quizFilter.getLessThanRating() > 0) {
            sql = sql + "quiz_rating(q.quiz_id) <= " + quizFilter.getLessThanRating() + " AND ";
        }
        if (quizFilter.getQuizLang() != null) {
            sql = sql + "quiz_lang LIKE '" + quizFilter.getQuizLang() + "' AND ";
        }
        if (quizFilter.getTags() != null && quizFilter.getTags().size() > 0) {
            for (int i = 0; i < quizFilter.getTags().size(); i++) {
                sql = sql + "quiz_id IN (SELECT quiz_id FROM quizzes_tags WHERE tag_id = '" + quizFilter.getTags().get(i) +
                      "') AND ";
            }
        }
        if (quizFilter.getCategories() != null && quizFilter.getCategories().size() > 0) {
            for (int i = 0; i < quizFilter.getCategories().size(); i++) {
                sql = sql + "quiz_id IN (SELECT quiz_id FROM categs_quizzes WHERE category_id = '" +
                      quizFilter.getCategories().get(i) + "') AND ";
            }
        }
        sql = sql.substring(0, sql.length() - 4);
        return sql;
    }

    @Override
    public int findQuizzesFilterSize(DtoQuizFilter quizFilter) {
        String sql = SqlConstants.QUIZ_FIND_QUIZZES_BY_FILTER_SIZE;
        sql = filterSqlGeneration(quizFilter, sql);
        System.out.println(sql);
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }


    public List<DtoQuizValid> getValidQuizzes(int startIndex, int amount, String adminId) {
        try {
            return jdbcTemplate.query(SqlConstants.QUIZ_GET_VALID_QUIZZES, new Object[]{adminId, amount, startIndex}, new QuizValidMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int getInvalidQuizzesTotalSize() {
        try {
            return jdbcTemplate.queryForObject(SqlConstants.QUIZ_INVALID_QUIZZES_TOTAL_SIZE, Integer.class);
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return 0;
        }
    }

    @Override
    public int getValidQuizzesTotalSize(String adminId) {
        try {
            return jdbcTemplate.queryForObject(SqlConstants.QUIZ_VALID_QUIZZES_TOTAL_SIZE, new Object[]{adminId}, Integer.class);
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return 0;
        }
    }

    @Override
    @Transactional
    public Quiz setValidator(String quizId, String adminId, String language) {
        jdbcTemplate.update(SqlConstants.QUIZ_SET_VALIDATOR, adminId, quizId);
        return getQuiz(quizId,language);
    }

    @Override
    public void removeQuestionImage(String questionId) {
        jdbcTemplate.update(SqlConstants.QUIZ_REMOVE_QUESTION_IMAGE, questionId);
    }

    @Override
    public String saveQuestion(Question question) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    SqlConstants.QUIZ_SAVE_QUESTION,
                    Statement.RETURN_GENERATED_KEYS);

            ps.setObject(1, java.util.UUID.fromString(question.getQuizId()));
            ps.setString(2, question.getTitle());
            ps.setString(3, question.getContent());
            ps.setInt(4, question.getPoints());
            ps.setInt(5, question.getTypeId());
            ps.setBytes(6, question.getImageContent());
            return ps;
        }, keyHolder);
        return keyHolder.getKeys()
                .get("question_id")
                .toString();
    }

    @Override
    public void saveFirstTypeAns(Question question) {
        for (int i = 0; i < question.getRightOptions()
                .size(); i++) {
            jdbcTemplate.update(SqlConstants.QUIZ_FIRST_TYPE_ANS, question.getRightOptions().get(i), true, question.getId());
        }
        for (int i = 0; i < question.getOtherOptions()
                .size(); i++) {
            jdbcTemplate.update(SqlConstants.QUIZ_FIRST_TYPE_ANS, question.getOtherOptions().get(i), false, question.getId());
        }
    }

    @Override
    public void saveSecondThirdTypeAns(Question question) {
        jdbcTemplate.update(SqlConstants.QUIZ_SECOND_THIRD_TYPE_ANS, question.getRightOptions().get(0), question.getId());
    }

    @Override
    public void saveFourthTypeAns(Question question) {
        for (int i = 0; i < question.getRightOptions().size(); i++) {
            jdbcTemplate.update(SqlConstants.QUIZ_FOURTH_TYPE_ANS, i + 1, question.getRightOptions().get(i), question.getId());
        }
    }

    @Override
    public void deleteQuestion(Question question) {
        jdbcTemplate.update(SqlConstants.QUIZ_DELETE_QUESTION, question.getId());
    }

    @Override
    public List<Question> getQuestionList(String quizId) {
        try {

            List<Question> listQ = jdbcTemplate.query(SqlConstants.QUIZ_GET_QUESTION_LIST,
                    new Object[]{quizId}, (rs, i) -> Question.builder()
                            .id(rs.getString("question_id"))
                            .quizId(rs.getString("quiz_id"))
                            .title(rs.getString("title"))
                            .content(rs.getString("content"))
                            .points(rs.getInt("points"))
                            .typeId(rs.getInt("type_id"))
                            .imageContent(rs.getBytes("img"))
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
            List listT = jdbcTemplate.queryForList(SqlConstants.QUIZ_GET_TAG_LIST, new Object[]{});
            return listT;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<List<Object>> getCategoryList(String language) {
        try {
            List listT =
                    jdbcTemplate.queryForList(SqlConstants.QUIZ_GET_CATEGORY_LIST, language);
            return listT;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Override
    public List<Quiz> getUserQuizList(String userId, String thisUserId) {
        try {
            return jdbcTemplate.query(SqlConstants.QUIZ_GET_USER_QUIZ_LIST,
                    new Object[]{thisUserId, userId}, (rs, i) -> Quiz.builder()
                            .id(rs.getString("quiz_id"))
                            .title(rs.getString("title"))
                            .description(rs.getString(
                                    "description"))
                            .imageContent(
                                    rs.getBytes("image"))
                            .creationDate(rs.getDate(
                                    "ver_creation_datetime"))
                            .activated(rs.getBoolean(
                                    "activated"))
                            .validated(rs.getBoolean(
                                    "validated"))
                            .published(rs.getBoolean(
                                    "published"))
                            .language(rs.getString(
                                    "quiz_lang"))
                            .rating(rs.getFloat("rating"))
                            .adminComment(rs.getString(
                                    "admin_commentary"))
                            .isFavourite(
                                    rs.getInt("liked") > 0)
                            .build());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Quiz> getUserFavouriteList(String userId) {
        return jdbcTemplate.query(SqlConstants.QUIZ_USER_FAVOURITES_LIST,
                new Object[]{userId}, (rs, i) -> Quiz.builder()
                        .id(rs.getString("quiz_id"))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .imageContent(rs.getBytes("image"))
                        .creationDate(rs.getDate("ver_creation_datetime"))
                        .activated(rs.getBoolean("activated"))
                        .validated(rs.getBoolean("validated"))
                        .published(rs.getBoolean("published"))
                        .language(rs.getString("quiz_lang"))
                        .rating(rs.getFloat("rating"))
                        .isFavourite(true)
                        .build());
    }


    @Override
    public List<QuizView> getQuizzes(int startIndex, int amount) {
        try {
            return jdbcTemplate.query(SqlConstants.QUIZ_GET_QUIZZES,

                    new Object[]{amount, startIndex}, (rs, i) -> QuizView.builder()
                            .quiz_id(rs.getString("quiz_id"))
                            .title(rs.getString("title"))
                            .image_content(rs.getBytes("image_content"))
                            .build());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<QuizFiltered> getSuggestionsQuizListByCategoriesAndTags(String userId, int amount) {
        try {
            String sql = SqlConstants.QUIZ_GET_SUGGESTION_QUIZ_LIST_BY_CATEGS_AND_TAGS;
            return jdbcTemplate.query(sql, new Object[]{userId, userId, userId, amount}, new QuizFilteredMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<DtoQuizValid> getInvalidQuizzes(int startIndex, int amount, String adminId) {
        try {
            return jdbcTemplate.query(SqlConstants.QUIZ_GET_INVALID_QUIZZES,

                    new Object[]{adminId, amount, startIndex}, (rs, i) -> DtoQuizValid.builder()
                            .id(rs.getString(
                                    "quiz_id"))
                            .title(rs.getString(
                                    "title"))
                            .description(
                                    rs.getString(
                                            "description"))
                            .imageContent(
                                    rs.getBytes(
                                            "image_content"))
                            .creationDate(
                                    rs.getDate(
                                            "ver_creation_datetime"))
                            .creatorId(
                                    rs.getString(
                                            "creator_id"))
                            .username(
                                    rs.getString(
                                            "username"))
                            .language(
                                    rs.getString(
                                            "quiz_lang"))
                            .adminComment(
                                    rs.getString(
                                            "admin_commentary"))
                            .build());

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int getQuizzesTotalSize() {
        try {
            return jdbcTemplate.queryForObject(SqlConstants.QUIZ_QUIZZES_TOTAL_SIZE, Integer.class);
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return 0;
        }
    }

    @Override
    public int getQuestionsAmountInQuiz(String quizId) {
        try {
            return jdbcTemplate.queryForObject(SqlConstants.QUIZ_QUESTIONS_AMOUNT_IN_QUIZ, new Object[]{quizId}, Integer.class);

        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return 0;
        }
    }

    @Override
    public List<Question> getQuestionsInPage(int startIndex, int amount, String quizId) {
        try {
            List<Question> listQ = jdbcTemplate.query(SqlConstants.QUIZ_GET_QUESTIONS_IN_PAGE,
                    new Object[]{quizId, amount, startIndex},
                    (rs, i) -> Question.builder()
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

    //Get amount of quizzes, user had created and they were successfully validated
    @Override
    public int getAmountSuccessCreated(String userId) {
        try {
            return jdbcTemplate.queryForObject(SqlConstants.QUIZ_AMOUNT_SUCCESS_CREATED, new Object[]{userId}, Integer.class);
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return 0;
        }
    }

    @Override
    public List<DtoQuizLastPlayed> getLastPlayedQuizzes(String userId) {
        return jdbcTemplate.query(SqlConstants.QUIZ_LAST_PLAYED_QUIZZES,
                new Object[]{userId},
                (resultSet, i) -> DtoQuizLastPlayed.builder()
                        .quizId(resultSet.getString("quiz_id"))
                        .gameId(resultSet.getString("game_id"))
                        .durationTime(resultSet.getInt("duration_time"))
                        .isWinner(resultSet.getBoolean("is_winner"))
                        .score(resultSet.getInt("score"))
                        .datetimeStart(resultSet.getTimestamp("datetime_start"))
                        .title(resultSet.getString("title"))
                        .build());
    }

    @Override
    public List<DtoPopularQuiz> getMostPopularQuizzesForLastWeek(int amount) {
        return jdbcTemplate.query(SqlConstants.QUIZ_MOST_POPULAR_QUIZZES_LAST_WEEK, new Object[]{amount},
                (resultSet, i) -> DtoPopularQuiz.builder()
                        .quizId(resultSet.getString("quiz_id"))
                        .title(resultSet.getString("title"))
                        .gamesAmount(resultSet.getInt("games_amount"))
                        .build());
    }

    @Override
    public List<Rating> getUserQuizRating(String quizId, String userId) {
        return jdbcTemplate.query(SqlConstants.QUIZ_USER_QUIZ_RATING, new Object[]{quizId, userId}, new RatingMapper());
    }

    @Override
    public void rateQuiz(String sessionId, int ratingPoints, String userId) {
        jdbcTemplate.update(SqlConstants.QUIZ_RATE_QUIZ, sessionId, userId, ratingPoints, ratingPoints);
    }

    @Override
    public int countValidatedQuizzesByAdmin() {
        Integer result = jdbcTemplate.queryForObject(SqlConstants.QUIZ_COUNT_VALIDATED_BY_ADMIN,
                Integer.class);
        if (result == null) {
            return 0;
        } else return result;
    }

    @Override
    public int countValidatedQuizzesByModerator() {
        Integer result = jdbcTemplate.queryForObject(SqlConstants.QUIZ_COUNT_VALIDATED_BY_MODERATOR,
                Integer.class);
        if (result == null) {
            return 0;
        } else return result;
    }

    @Override
    public int getQuizzesNumber() {
        return jdbcTemplate.queryForObject(SqlConstants.QUIZ_GET_NUMBER, new Object[]{}, Integer.class);
    }

    @Override
    public int getActivatedNumber() {
        return jdbcTemplate.queryForObject(SqlConstants.QUIZ_ACTIVATED_NUMBER, new Object[]{}, Integer.class);
    }

    @Override
    public int getPublishedNumber() {
        return jdbcTemplate.queryForObject(SqlConstants.QUIZ_PUBLISHED_NUMBER, new Object[]{}, Integer.class);
    }

    @Override
    public int getRejectedNumber() {
        return jdbcTemplate.queryForObject(SqlConstants.QUIZ_REJECTED_NUMBER, new Object[]{}, Integer.class);
    }

    @Override
    public int getUnvalidatedNumber() {
        return jdbcTemplate.queryForObject(SqlConstants.QUIZ_UNVALIDATED_NUMBER, new Object[]{}, Integer.class);
    }

    public Question loadAnswersForQuestion(Question question, int i) {
        switch (question.getTypeId()) {
            case (1):
                question.setRightOptions(jdbcTemplate.queryForList(SqlConstants.QUIZ_GET_RIGHT_ANSWERS, new Object[]{question.getId()}, String.class));
                question.setOtherOptions(jdbcTemplate.queryForList(SqlConstants.QUIZ_GET_OTHER_ANSWERS, new Object[]{question.getId()}, String.class));
                return question;
            case (2):
            case (3):
                question.setRightOptions(jdbcTemplate.queryForList(SqlConstants.QUIZ_GET_ONE_VAL_ANSWER, new Object[]{question.getId()}, String.class));
                return question;
            case (4):
                question.setRightOptions(jdbcTemplate.queryForList(SqlConstants.QUIZ_GET_SEQUENCE_ANSWER, new Object[]{question.getId()}, String.class));
                return question;
            default:
                return null;
        }
    }

    @Override
    public List<DtoQuizRates> getUserQuizzesRating(String userId) {
        return jdbcTemplate.query(SqlConstants.QUIZ_GET_USER_QUIZZES_RATING, new Object[]{userId},
                (resultSet, i) -> DtoQuizRates.builder()
                        .id(resultSet.getString("quiz_id"))
                        .title(resultSet.getString("title"))
                        .imageContent(resultSet.getBytes("image"))
                        .rating(resultSet.getDouble("rating"))
                        .build());
    }

    private List<String> loadTagNameList(String quizId) {
        return jdbcTemplate.query(SqlConstants.QUIZ_GET_TAG_NAME_LIST, new Object[]{quizId},
                (rs, rowNum) -> rs.getString(1));
    }

    private List<String> loadCategoryNameList(String quizId, String language) {
        return jdbcTemplate.query(SqlConstants.QUIZ_GET_CATEGORY_NAME_LIST, new Object[]{language,quizId},
                (rs, rowNum) -> rs.getString(1));
    }

    private List<String> loadTagIdList(String quizId) {
        return jdbcTemplate.query(SqlConstants.QUIZ_GET_TAG_ID_LIST, new Object[]{quizId},
                (rs, rowNum) -> rs.getString(1));
    }

    private List<String> loadCategoryIdList(String quizId) {
        return jdbcTemplate.query(SqlConstants.QUIZ_GET_CATEGORY_ID_LIST, new Object[]{quizId},
                (rs, rowNum) -> rs.getString(1));
    }


    @Override
    public Integer getUserQuizListAmount(String userId) {
        return jdbcTemplate.queryForObject(SqlConstants.QUIZ_USER_QUIZ_LIST_AMOUNT, new Object[]{userId}, Integer.class);

    }

    @Override
    public Integer getUserFavQuizListAmount(String userId) {
        return jdbcTemplate.queryForObject(SqlConstants.QUIZ_USER_FAV_QUIZ_LIST_AMOUNT,
                new Object[]{userId}, Integer.class);

    }


}
