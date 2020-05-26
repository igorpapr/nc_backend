package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.Chat;
import net.dreamfteam.quiznet.web.dto.DtoChatUser;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface ChatService {

    HttpStatus createPersonalChat(String currentUserId, String otherUserId);

    void createGroupChat(String title, String userId);

    //check if is creator change title
    void updateChatTitle(String chatId, String newTitle);

    //check if is creator add to chat
    void addUserToGroupChat(String userId, String chatId);

    List<Chat> getAllUsersChat(String userId);

    List<DtoChatUser> getAllUsersInChat(String chatId);

//    List<ChatMessage> getAllMessagesInChat(String chatId);
//
//    void saveMessage(String content, String chatId);

}
