package net.dreamfteam.quiznet.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.data.dao.ChatDao;
import net.dreamfteam.quiznet.data.entities.Chat;
import net.dreamfteam.quiznet.data.entities.UserView;
import net.dreamfteam.quiznet.service.ChatService;
import net.dreamfteam.quiznet.web.dto.DtoChatMessage;
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
    public String createPersonalChat(String currentUserId, String otherUserId) {

        String idIfCreated = chatDao.checkIsPersonalChatCreated(currentUserId, otherUserId);

        if (idIfCreated == null) {
            return chatDao.savePersonalChat(currentUserId, otherUserId);

        } else
            return idIfCreated;

    }

    @Override
    public String createGroupChat(DtoCreateGroupChat groupChat, String userId) {
        String chatId = chatDao.saveGroupChat(groupChat.getTitle(), userId);
        groupChat.getParticipants().forEach(participant -> chatDao.addUserToGroupChat(participant, chatId));
        return chatId;
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
    public boolean checkIfChatExist(String chatId) {
        return chatDao.checkIfChatExist(chatId);
    }

    @Override
    public void saveMessage(String chatId, DtoChatMessage chatMessage) {
        chatDao.saveMessage(chatId, chatMessage);
    }

    //TODO CHECK IF CHAT EXIST CHECK NULL
    @Override
    public Chat getChatById(String chatId, String currentUserId) {
        return chatDao.getChatById(chatId, currentUserId);
    }

    @Override
    public List<UserView> getFriendByTerm(String term, String userId){
        return chatDao.getFriendByTerm(term,userId);
    }
}
