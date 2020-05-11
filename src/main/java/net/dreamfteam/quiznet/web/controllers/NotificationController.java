package net.dreamfteam.quiznet.web.controllers;

import com.google.gson.Gson;
import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.NotificationService;
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

    private final Gson gson;
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(Gson gson, NotificationService notificationService) {
        this.gson = gson;
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<?> insertNotification(@RequestParam("obj") String notification,
                                                @RequestParam(value = "img", required = false) MultipartFile image)
            throws ValidationException {

        DtoNotification dtoNotification = gson.fromJson(notification, DtoNotification.class);

        NotificationValidator.validate(dtoNotification);

        notificationService.insert(dtoNotification,image);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("seen/{userId}")
    public ResponseEntity<?> seen(@PathVariable String userId)
            throws ValidationException {

        notificationService.updateSeen(userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<?> get(@PathVariable String userId){
        return new ResponseEntity<>(notificationService.getUnseenByUserId(userId),HttpStatus.OK);
    }


}
