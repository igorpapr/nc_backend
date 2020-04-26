package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.service.RecoveringService;
import net.dreamfteam.quiznet.web.dto.DtoChangePassword;
import net.dreamfteam.quiznet.web.dto.DtoMail;
import net.dreamfteam.quiznet.web.validators.RecoverDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.websocket.server.PathParam;

@RestController
@CrossOrigin
@RequestMapping(Constants.SECUR_RECOVER_URLS)
public class RecoveringController {

    @Value("${recover.redirect.url}")
    private String RECOVER_REDIRECT_URL;

    private RecoveringService recoveringService;

    @Autowired
    public RecoveringController(RecoveringService recoveringService) {
        this.recoveringService = recoveringService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> registerUser(@RequestBody DtoMail user) {

        RecoverDtoValidator.validate(user);

        recoveringService.sendRecoveringUrl(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/confirm")
    public RedirectView activate(@PathParam("key") String key) {

        return new RedirectView(RECOVER_REDIRECT_URL + recoveringService.confirmRecovery(key));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody DtoChangePassword passwordDto) {

        RecoverDtoValidator.validate(passwordDto);
        recoveringService.changePassword(passwordDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
