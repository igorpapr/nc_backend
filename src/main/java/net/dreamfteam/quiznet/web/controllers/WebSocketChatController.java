package net.dreamfteam.quiznet.web.controllers;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.ChatService;
import net.dreamfteam.quiznet.web.dto.DtoChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Slf4j
@Controller
public class WebSocketChatController {

	final private ChatService chatService;
	final private SimpMessagingTemplate messagingTemplate;

	@Autowired
	public WebSocketChatController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
		this.chatService = chatService;
		this.messagingTemplate = messagingTemplate;
	}

	@MessageMapping("/chat/{chatId}")
	public void sendMessage(@DestinationVariable String chatId, String chatMessage) {

		log.info(chatId);

		DtoChatMessage dtoChatMessage = new Gson().fromJson(chatMessage, DtoChatMessage.class);

		if (!chatService.checkIfChatExist(chatId)) {
			throw new ValidationException("Not such chat");
		}

		dtoChatMessage.setSentDate(new Date());

		chatService.saveMessage(chatId, dtoChatMessage);

		messagingTemplate.convertAndSend("topic/message/" + chatId, new Gson().toJson(dtoChatMessage));
	}

}
