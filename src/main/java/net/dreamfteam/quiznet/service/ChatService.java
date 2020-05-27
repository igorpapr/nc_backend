package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.Chat;
import net.dreamfteam.quiznet.data.entities.UserView;
import net.dreamfteam.quiznet.web.dto.DtoChatMessage;
import net.dreamfteam.quiznet.web.dto.DtoChatUser;
import net.dreamfteam.quiznet.web.dto.DtoCreateGroupChat;

import java.util.List;

public interface ChatService {

    String createPersonalChat(String currentUserId, String otherUserId);

    String createGroupChat(DtoCreateGroupChat groupChat, String userId);

    //TODO check if is creator change title than can change
    void updateChatTitle(String chatId, String newTitle);

    //TODO check if is creator add to chat
    void addUserToGroupChat(String userId, String chatId);

    List<Chat> getAllUsersChat(String userId);

    List<DtoChatUser> getAllUsersInChat(String chatId);

    boolean checkIfChatExist(String chatId);

    void saveMessage(String chatId, DtoChatMessage chatMessage);

    Chat getChatById(String chatId, String currentUserId);

    List<DtoChatMessage> getMessagesInChat(String chatId, int page);

    List<UserView> getFriendByTerm(String term, String userId);

}
