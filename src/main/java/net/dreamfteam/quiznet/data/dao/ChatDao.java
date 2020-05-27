package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.Chat;
import net.dreamfteam.quiznet.web.dto.DtoChatUser;

import java.util.List;

public interface ChatDao {

    String savePersonalChat(String currentUserId, String otherUserId);

    String saveGroupChat(String title, String userId);

    void addUserToGroupChat(String userId, String chatId);

    void updateChatTitle(String chatId, String newTitle);

    String checkIsPersonalChatCreated(String currentUserId, String otherUserId);

    List<Chat> getAllUsersChat(String userId);

    List<DtoChatUser> getAllUsersInChat(String chatId);

    Chat getChatById(String chatId,String currentUserId);
}
