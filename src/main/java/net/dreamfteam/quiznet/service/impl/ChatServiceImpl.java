package net.dreamfteam.quiznet.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.data.dao.ChatDao;
import net.dreamfteam.quiznet.data.entities.Chat;
import net.dreamfteam.quiznet.data.entities.UserView;
import net.dreamfteam.quiznet.service.ChatService;
import net.dreamfteam.quiznet.web.dto.DtoChatUser;
import net.dreamfteam.quiznet.web.dto.DtoChatWithParticipants;
import net.dreamfteam.quiznet.web.dto.DtoCreateGroupChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    final private ChatDao chatDao;

    @Autowired
    public ChatServiceImpl(ChatDao chatDao) {
        this.chatDao = chatDao;
    }

    @Override
    public DtoChatWithParticipants createPersonalChat(String currentUserId, String otherUserId) {
        String idIfCreated = chatDao.checkIsPersonalChatCreated(currentUserId, otherUserId);
        DtoChatWithParticipants returnedChat;

        if (idIfCreated == null) {
            String id = chatDao.savePersonalChat(currentUserId, otherUserId);
            returnedChat = DtoChatWithParticipants.toDtoChatWithParticipants(chatDao.getChatById(id, currentUserId));
            returnedChat.setParticipants(chatDao.getAllUsersInChat(id));
            return returnedChat;
        } else {
            returnedChat = DtoChatWithParticipants.toDtoChatWithParticipants(chatDao.getChatById(idIfCreated, currentUserId));
            returnedChat.setParticipants(chatDao.getAllUsersInChat(idIfCreated));
            return returnedChat;
        }
    }

    @Override
    public DtoChatWithParticipants createGroupChat(DtoCreateGroupChat groupChat, String userId) {
        String chatId = chatDao.saveGroupChat(groupChat.getTitle(), userId);
        groupChat.getParticipants().forEach(participant -> chatDao.addUserToGroupChat(participant, chatId));

        Chat chatById = chatDao.getChatById(chatId, userId);
        DtoChatWithParticipants returnedChat = DtoChatWithParticipants.toDtoChatWithParticipants(chatById);
        chatDao.getAllUsersInChat(chatId);
        returnedChat.setParticipants(chatDao.getAllUsersInChat(chatId));
        return returnedChat;
    }

    @Override
    public void updateChatTitle(String chatId, String newTitle) {
        chatDao.updateChatTitle(chatId, newTitle);
    }

    @Override
    public void addUserToGroupChat(String userId, String chatId) {
        chatDao.addUserToGroupChat(userId, chatId);
    }

    @Override
    public List<Chat> getAllUsersChat(String userId) {
        return chatDao.getAllUsersChat(userId);
    }

    @Override
    public List<DtoChatUser> getAllUsersInChat(String chatId) {
        return chatDao.getAllUsersInChat(chatId);
    }

    @Override
    public List<UserView> getFriendByTerm(String term, String userId){
        return chatDao.getFriendByTerm(term,userId);
    }
}
