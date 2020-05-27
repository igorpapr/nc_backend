package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.Chat;
import net.dreamfteam.quiznet.data.entities.UserView;
import net.dreamfteam.quiznet.web.dto.DtoChatUser;

import java.util.List;

public interface ChatDao {

    void savePersonalChat(String currentUserId, String otherUserId);

    void saveGroupChat(String title, String userId);

    void addUserToGroupChat(String userId, String chatId);

    void updateChatTitle(String chatId, String newTitle);

    boolean checkIsPersonalChatCreated(String currentUserId, String otherUserId);

    List<Chat> getAllUsersChat(String userId);

    List<DtoChatUser> getAllUsersInChat(String chatId);

    List<UserView> getFriendByTerm(String term, String userId);

}
