package net.dreamfteam.quiznet.web.controllers;

import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.data.entities.Chat;
import net.dreamfteam.quiznet.service.ChatService;
import net.dreamfteam.quiznet.web.dto.DtoChatUser;
import net.dreamfteam.quiznet.web.dto.DtoChatWithParticipants;
import net.dreamfteam.quiznet.web.dto.DtoCreateGroupChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping(Constants.CHAT_URLS)
public class ChatController {

    final private ChatService chatService;
    final private IAuthenticationFacade authenticationFacade;

    @Autowired
    public ChatController(ChatService chatService, IAuthenticationFacade authenticationFacade) {
        this.chatService = chatService;
        this.authenticationFacade = authenticationFacade;
    }

    @PostMapping("/personal")
    public ResponseEntity<?> createPersonalChat(@RequestParam String userId) {

        DtoChatWithParticipants personalChat = chatService.createPersonalChat(authenticationFacade.getUserId(), userId);

        return new ResponseEntity<>(personalChat, HttpStatus.OK);
    }

    //TODO check correct ids in participants
    @PostMapping("/group")
    public ResponseEntity<?> createGroupChat(@RequestBody DtoCreateGroupChat groupChat) {

        DtoChatWithParticipants chat = chatService.createGroupChat(groupChat, authenticationFacade.getUserId());
        return new ResponseEntity<>(chat, HttpStatus.CREATED);
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

    @GetMapping("/{chatId}/users")
    public ResponseEntity<List<DtoChatUser>> getAllUsersInChat(@PathVariable String chatId) {
        return new ResponseEntity<>(chatService.getAllUsersInChat(chatId), HttpStatus.OK);
    }

    @GetMapping("/friends/{term}")
    public ResponseEntity<?> getFriends(@PathVariable String term){
        return new ResponseEntity<>(chatService.getFriendByTerm(term,authenticationFacade.getUserId()),HttpStatus.OK);
    }
}
