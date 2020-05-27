package net.dreamfteam.quiznet.web.controllers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.data.entities.Chat;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.ChatService;
import net.dreamfteam.quiznet.web.dto.DtoChatMessage;
import net.dreamfteam.quiznet.web.dto.DtoChatUser;
import net.dreamfteam.quiznet.web.dto.DtoCreateGroupChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping(Constants.CHAT_URLS)
public class ChatController {

    final private ChatService chatService;
    final private IAuthenticationFacade authenticationFacade;
    final private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatController(ChatService chatService, IAuthenticationFacade authenticationFacade, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.authenticationFacade = authenticationFacade;
        this.messagingTemplate = messagingTemplate;
    }

    //TODO check if userId != currentUser
    @PostMapping("/personal")
    public ResponseEntity<?> createPersonalChat(@RequestParam String userId) {

        String chatId = chatService.createPersonalChat(authenticationFacade.getUserId(), userId);
        Map<String, String> personalChat = new HashMap<>();
        personalChat.put("chatId", chatId);
        return new ResponseEntity<>(personalChat, HttpStatus.OK);
    }

    //TODO check correct ids in participants
    @PostMapping("/group")
    public ResponseEntity<?> createGroupChat(@RequestBody DtoCreateGroupChat dtoCreateGroupChat) {

        String chatId = chatService.createGroupChat(dtoCreateGroupChat, authenticationFacade.getUserId());
        Map<String, String> groupChat = new HashMap<>();
        groupChat.put("chatId", chatId);
        return new ResponseEntity<>(groupChat, HttpStatus.CREATED);
    }

    @PostMapping("/group/{chatId}")
    public ResponseEntity<?> addUserToGroupChat(@PathVariable String chatId, @RequestParam String userId) {

        chatService.addUserToGroupChat(userId, chatId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PatchMapping("/group/{chatId}")
    public ResponseEntity<?> changeChatTitle(@PathVariable String chatId, @RequestParam String newTitle) {
        chatService.updateChatTitle(chatId, newTitle);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Chat>> getAllUsersChats() {

        return new ResponseEntity<>(chatService.getAllUsersChat(authenticationFacade.getUserId()), HttpStatus.OK);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> getChat(@PathVariable String chatId) {
        return new ResponseEntity<>(chatService.getChatById(chatId, authenticationFacade.getUserId()), HttpStatus.OK);
    }

    @MessageMapping("/chat/{chatId}")
    public void sendMessage(@PathVariable String chatId, String chatMessage) {

        log.info(chatId);
        log.info(authenticationFacade.getUserId());

        DtoChatMessage dtoChatMessage = new Gson().fromJson(chatMessage, DtoChatMessage.class);

        if (!chatService.checkIfChatExist(chatId)) {
            throw new ValidationException("Not such chat");
        }

        dtoChatMessage.setSentDate(new Date());

        chatService.saveMessage(chatId, dtoChatMessage);

        messagingTemplate.convertAndSend("topic/message/" + chatId, new Gson().toJson(dtoChatMessage));
    }

    @GetMapping("/{chatId}/participants")
    public ResponseEntity<List<DtoChatUser>> getChatParticipants(@PathVariable String chatId) {
        return new ResponseEntity<>(chatService.getAllUsersInChat(chatId), HttpStatus.OK);
    }

    @GetMapping("/friends/{term}")
    public ResponseEntity<?> getFriends(@PathVariable String term) {
        return new ResponseEntity<>(chatService.getFriendByTerm(term, authenticationFacade.getUserId()), HttpStatus.OK);
    }
}
