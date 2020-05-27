package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.Chat;
import net.dreamfteam.quiznet.data.entities.UserView;
import net.dreamfteam.quiznet.web.dto.DtoChatMessage;
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

    List<UserView> getFriendByTerm(String term, String userId);

    Chat getChatById(String chatId,String currentUserId);

    boolean checkIfChatExist(String chatId);

    void saveMessage(String chatId, DtoChatMessage chatMessage);

    List<DtoChatMessage> getMessagesInChat(String chatId, int page, int amountMessagesOnPage);
}
