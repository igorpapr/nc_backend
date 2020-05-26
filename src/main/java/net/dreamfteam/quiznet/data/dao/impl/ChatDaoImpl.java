package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.ChatDao;
import net.dreamfteam.quiznet.data.entities.Chat;
import net.dreamfteam.quiznet.data.rowmappers.ChatMapper;
import net.dreamfteam.quiznet.data.rowmappers.DtoChatUserMapper;
import net.dreamfteam.quiznet.web.dto.DtoChatUser;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void savePersonalChat(String currentUserId, String otherUserId) {

        UUID chatId = UUID.randomUUID();

        jdbcTemplate.update("INSERT INTO chats (chat_id, is_personal) " +
                "VALUES (?, ?)", chatId, true);

        jdbcTemplate.update("INSERT INTO users_chats (chat_id, user_id, datetime_joined, is_creator) " +
                "VALUES (?, ?, CURRENT_TIMESTAMP, true)", chatId, UUID.fromString(currentUserId));


        jdbcTemplate.update("INSERT INTO users_chats (chat_id, user_id, datetime_joined, is_creator) " +
                "VALUES (?, ?, CURRENT_TIMESTAMP, true)", chatId, UUID.fromString(otherUserId));


    }

    @Override
    public void saveGroupChat(String title, String userId) {
        UUID chatId = UUID.randomUUID();

        jdbcTemplate.update("INSERT INTO chats (chat_id, title, is_personal) " +
                "VALUES (?, ?, ?)", chatId, title, false);

        jdbcTemplate.update("INSERT INTO users_chats (chat_id, user_id, datetime_joined, is_creator) " +
                "VALUES (?, ?, CURRENT_TIMESTAMP, true)", chatId, UUID.fromString(userId));

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
    public boolean checkIsPersonalChatCreated(String currentUserId, String otherUserId) {
        return jdbcTemplate.queryForObject("SELECT count(*)=1 " +
                "FROM chats c INNER JOIN users_chats uc ON c.chat_id = uc.chat_id " +
                "WHERE c.is_personal = true " +
                "AND uc.user_id = UUID(?)" +
                "AND uc.chat_id IN (SELECT uc1.chat_id " +
                "                     FROM users_chats uc1 " +
                "                     WHERE uc1.chat_id = uc.chat_id " +
                "                       AND uc1.user_id = UUID(?));", new Object[]{currentUserId, otherUserId}, Boolean.class);
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
        return jdbcTemplate.query("SELECT username, users.user_id as user_id, image, last_time_online, datetime_joined from users " +
                "join users_chats uc on users.user_id = uc.user_id " +
                "WHERE chat_id = ?", new DtoChatUserMapper(), UUID.fromString(chatId));
    }


}
