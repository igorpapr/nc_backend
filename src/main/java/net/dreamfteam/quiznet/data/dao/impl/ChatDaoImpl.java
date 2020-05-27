package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.ChatDao;
import net.dreamfteam.quiznet.data.entities.Chat;
import net.dreamfteam.quiznet.data.entities.UserView;
import net.dreamfteam.quiznet.data.rowmappers.ChatMapper;
import net.dreamfteam.quiznet.data.rowmappers.DtoChatUserMapper;
import net.dreamfteam.quiznet.data.rowmappers.UserViewMapper;
import net.dreamfteam.quiznet.web.dto.DtoChatMessage;
import net.dreamfteam.quiznet.web.dto.DtoChatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ChatDaoImpl implements ChatDao {

    final private JdbcTemplate jdbcTemplate;

    @Autowired
    public ChatDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String savePersonalChat(String currentUserId, String otherUserId) {

        UUID chatId = UUID.randomUUID();

        jdbcTemplate.update("INSERT INTO chats (chat_id, is_personal) " +
                "VALUES (?, ?)", chatId, true);

        jdbcTemplate.update("INSERT INTO users_chats (chat_id, user_id, datetime_joined, is_creator) " +
                "VALUES (?, ?, CURRENT_TIMESTAMP, true)", chatId, UUID.fromString(currentUserId));


        jdbcTemplate.update("INSERT INTO users_chats (chat_id, user_id, datetime_joined, is_creator) " +
                "VALUES (?, ?, CURRENT_TIMESTAMP, true)", chatId, UUID.fromString(otherUserId));

        return chatId.toString();

    }

    @Override
    public String saveGroupChat(String title, String userId) {
        UUID chatId = UUID.randomUUID();

        jdbcTemplate.update("INSERT INTO chats (chat_id, title, is_personal) " +
                "VALUES (?, ?, ?)", chatId, title, false);

        jdbcTemplate.update("INSERT INTO users_chats (chat_id, user_id, datetime_joined, is_creator) " +
                "VALUES (?, ?, CURRENT_TIMESTAMP, true)", chatId, UUID.fromString(userId));

        return chatId.toString();
    }

    @Override
    public void addUserToGroupChat(String userId, String chatId) {

        jdbcTemplate.update("INSERT INTO users_chats (chat_id, user_id, datetime_joined, is_creator) " +
                "VALUES (?, ?, CURRENT_TIMESTAMP, false)", UUID.fromString(chatId), UUID.fromString(userId));
    }

    @Override
    public void updateChatTitle(String chatId, String newTitle) {
        jdbcTemplate.update("UPDATE chats SET title = ? " +
                "WHERE chat_id = ? ", newTitle, UUID.fromString(chatId));
    }

    @Override
    public String checkIsPersonalChatCreated(String currentUserId, String otherUserId) {
        try {
            return jdbcTemplate.queryForObject("SELECT c.chat_id " +
                    "FROM chats c INNER JOIN users_chats uc ON c.chat_id = uc.chat_id " +
                    "WHERE c.is_personal = true " +
                    "AND uc.user_id = UUID(?)" +
                    "AND uc.chat_id IN (SELECT uc1.chat_id " +
                    "                     FROM users_chats uc1 " +
                    "                     WHERE uc1.chat_id = uc.chat_id " +
                    "                       AND uc1.user_id = UUID(?));", new Object[]{currentUserId, otherUserId}, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Chat> getAllUsersChat(String userId) {
        return jdbcTemplate.query("SELECT c.chat_id, uc.datetime_joined, uc.is_creator, c.is_personal, " +
                        "CASE c.is_personal " +
                        "        WHEN true THEN (SELECT u.username " +
                        "                        FROM users_chats uc1 INNER JOIN users u ON uc1.user_id = u.user_id " +
                        "                        WHERE uc1.chat_id = c.chat_id " +
                        "                        AND uc1.user_id <> uuid(?))" +
                        "                        WHEN false THEN c.title END AS title, " +
                        "CASE c.is_personal " +
                        "        WHEN true THEN (SELECT u1.image " +
                        "                        FROM users_chats uc2 INNER JOIN users u1 ON uc2.user_id = u1.user_id " +
                        "                        WHERE uc2.chat_id = c.chat_id " +
                        "                        AND uc2.user_id <> uuid(?)) " +
                        "                        ELSE null END AS image " +
                        "FROM chats c INNER JOIN users_chats uc ON c.chat_id = uc.chat_id " +
                        "WHERE uc.user_id = uuid(?);"
                , new ChatMapper(), UUID.fromString(userId), UUID.fromString(userId), UUID.fromString(userId));
    }

    @Override
    public List<DtoChatUser> getAllUsersInChat(String chatId) {
        return jdbcTemplate.query("SELECT username, users.user_id as user_id, image, datetime_joined from users " +
                "join users_chats uc on users.user_id = uc.user_id " +
                "WHERE chat_id = ?", new DtoChatUserMapper(), UUID.fromString(chatId));
    }

    @Override
    public List<UserView> getFriendByTerm(String term, String userId) {
        term = "%" + term + "%";
        try {
            return jdbcTemplate
                    .query("SELECT user_id, username, last_time_online, image AS image_content " +
                                    "FROM users WHERE user_id IN (SELECT f.friend_id AS id " +
                                    "FROM friends f " +
                                    "WHERE f.parent_id = uuid(?) " +
                                    "AND f.accepted_datetime IS NOT NULL " +
                                    "UNION " +
                                    "SELECT f1.parent_id AS id " +
                                    "FROM friends f1 " +
                                    "WHERE f1.friend_id = uuid(?) " +
                                    "AND f1.accepted_datetime IS NOT NULL) AND username LIKE ?" +
                                    "LIMIT 10;", new Object[]{userId, userId, term},
                            new UserViewMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Chat getChatById(String chatId, String currentUserId) {
        return jdbcTemplate.queryForObject("SELECT c.chat_id, uc.datetime_joined, uc.is_creator, c.is_personal, " +
                "       CASE c.is_personal" +
                "           WHEN true THEN (SELECT u.username\n" +
                "                           FROM users_chats uc1 INNER JOIN users u ON uc1.user_id = u.user_id\n" +
                "                           WHERE uc1.chat_id = c.chat_id\n" +
                "                             AND uc1.user_id <> UUID(?))\n" +
                "           WHEN false THEN c.title END AS title," +
                "       CASE c.is_personal " +
                "           WHEN true THEN (SELECT u1.image" +
                "                           FROM users_chats uc2 INNER JOIN users u1 ON uc2.user_id = u1.user_id " +
                "                           WHERE uc2.chat_id = c.chat_id " +
                "                             AND uc2.user_id <> UUID(?))" +
                "           ELSE null END AS image " +
                "FROM chats c INNER JOIN users_chats uc ON c.chat_id = uc.chat_id\n" +
                "WHERE c.chat_id = UUID(?) AND uc.user_id= UUID(?);", new ChatMapper(), UUID.fromString(currentUserId), UUID.fromString(currentUserId), UUID.fromString(chatId), UUID.fromString(currentUserId));
    }

    @Override
    public boolean checkIfChatExist(String chatId) {
        return jdbcTemplate.queryForObject("SELECT CASE WHEN EXISTS " +
                        "(SELECT chat_id FROM chats WHERE chat_id = UUID(?)) " +
                        "THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END ; ",
                new Object[]{chatId}, Boolean.class);
    }

    @Override
    public void saveMessage(String chatId, DtoChatMessage chatMessage) {
        jdbcTemplate.update("INSERT INTO messages (message_id, chat_id, user_id, content, datetime_sent) VALUES (?, ?, ?, ?, ?)",
                UUID.randomUUID(), UUID.fromString(chatId), UUID.fromString(chatMessage.getAuthorId()), chatMessage.getContent(), chatMessage.getSentDate());
    }


}
