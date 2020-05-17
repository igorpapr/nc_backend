package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(Constants.NOTIFICATION_URLS)
public class NotificationController {

    private final NotificationService notificationService;
    private final IAuthenticationFacade authenticationFacade;


    @Autowired
    public NotificationController(NotificationService notificationService, IAuthenticationFacade authenticationFacade) {
        this.notificationService = notificationService;
        this.authenticationFacade = authenticationFacade;
    }

    @PostMapping("seen")
    public ResponseEntity<?> seen()
            throws ValidationException {

        notificationService.updateSeen(authenticationFacade.getUserId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getUnseen() {
        return new ResponseEntity<>(notificationService.getUnseenByUserId(authenticationFacade.getUserId()),
                HttpStatus.OK);
    }

    @GetMapping("notification/{notifId}")
    public ResponseEntity<?> get(@PathVariable String notifId) {
        return new ResponseEntity<>(notificationService.getById(notifId), HttpStatus.OK);
    }
}
