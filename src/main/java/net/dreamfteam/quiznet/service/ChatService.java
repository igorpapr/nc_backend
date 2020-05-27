package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.Chat;
import net.dreamfteam.quiznet.web.dto.DtoChatUser;
import net.dreamfteam.quiznet.web.dto.DtoChatWithParticipants;
import net.dreamfteam.quiznet.web.dto.DtoCreateGroupChat;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface ChatService {

    DtoChatWithParticipants createPersonalChat(String currentUserId, String otherUserId);

    DtoChatWithParticipants createGroupChat(DtoCreateGroupChat groupChat, String userId);

    //TODO check if is creator change title than can change
    void updateChatTitle(String chatId, String newTitle);

    //TODO check if is creator add to chat
    void addUserToGroupChat(String userId, String chatId);

    List<Chat> getAllUsersChat(String userId);

    List<DtoChatUser> getAllUsersInChat(String chatId);

//    List<ChatMessage> getAllMessagesInChat(String chatId);
//
//    void saveMessage(String content, String chatId);

}
