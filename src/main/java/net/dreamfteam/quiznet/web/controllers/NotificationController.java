package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.NotificationService;
import net.dreamfteam.quiznet.service.SseService;
import net.dreamfteam.quiznet.web.dto.DtoNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@CrossOrigin
@RequestMapping(Constants.NOTIFICATION_URLS)
public class NotificationController {

    private final NotificationService notificationService;
    private final SseService sseService;
    private final IAuthenticationFacade authenticationFacade;


    @Autowired
    public NotificationController(NotificationService notificationService, SseService sseService,
                                  IAuthenticationFacade authenticationFacade) {
        this.notificationService = notificationService;
        this.sseService = sseService;
        this.authenticationFacade = authenticationFacade;
    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @PostMapping("seen")
    public ResponseEntity<?> seen() throws ValidationException {
        System.out.println(authenticationFacade.getUserId());
        notificationService.updateSeen(authenticationFacade.getUserId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping("{userId}")
    public ResponseEntity<?> getUnseen(@PathVariable String userId) {
        return new ResponseEntity<>(notificationService.getUnseenByUserId(userId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping("notification/{notifId}")
    public ResponseEntity<?> get(@PathVariable String notifId) {
        return new ResponseEntity<>(notificationService.getById(notifId), HttpStatus.OK);
    }

    //DELETE
    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping("send")
    public ResponseEntity<?> send() {
        sseService.send(authenticationFacade.getUserId(), "sent");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //DELETE
    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping("new")
    public ResponseEntity<?> newNot() {
        notificationService.insert(DtoNotification.builder()
                                                  .userId(authenticationFacade.getUserId())
                                                  .content(new Date().toString())
                                                  .build());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
