package net.dreamfteam.quiznet.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.data.dao.ChatDao;
import net.dreamfteam.quiznet.data.entities.Chat;
import net.dreamfteam.quiznet.data.entities.UserView;
import net.dreamfteam.quiznet.service.ChatService;
import net.dreamfteam.quiznet.web.dto.DtoChatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public HttpStatus createPersonalChat(String currentUserId, String otherUserId) {
        boolean isCreated = chatDao.checkIsPersonalChatCreated(currentUserId, otherUserId);

        if (!isCreated) {
            chatDao.savePersonalChat(currentUserId, otherUserId);
            return HttpStatus.CREATED;
        } else return HttpStatus.OK;
    }

    @Override
    public void createGroupChat(String title, String userId) {
        chatDao.saveGroupChat(title, userId);
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
