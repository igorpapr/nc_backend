package net.dreamfteam.quiznet.web.controllers;

import com.google.gson.Gson;
import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.NotificationService;
import net.dreamfteam.quiznet.service.SseService;
import net.dreamfteam.quiznet.web.dto.DtoNotification;
import net.dreamfteam.quiznet.web.validators.NotificationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping(Constants.NOTIFICATION_URLS)
public class NotificationController {

    private final NotificationService notificationService;


    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("seen/{userId}")
    public ResponseEntity<?> seen(@PathVariable String userId)
            throws ValidationException {

        notificationService.updateSeen(userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<?> getUnseen(@PathVariable String userId) {
        return new ResponseEntity<>(notificationService.getUnseenByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("notification/{notifId}")
    public ResponseEntity<?> get(@PathVariable String notifId) {
        return new ResponseEntity<>(notificationService.getById(notifId), HttpStatus.OK);
    }
}
