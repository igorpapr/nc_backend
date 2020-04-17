package net.dreamfteam.quiznet.web.controllers;


import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.ActivationService;
import net.dreamfteam.quiznet.service.UserService;
import net.dreamfteam.quiznet.web.dto.LoginRequest;
import net.dreamfteam.quiznet.web.dto.UserLoginSuccessResponse;
import net.dreamfteam.quiznet.web.validators.LoginRequestValidator;
import net.dreamfteam.quiznet.web.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.websocket.server.PathParam;


@RestController
@CrossOrigin
@RequestMapping(Constants.SIGN_UP_URLS)
public class AuthorizationController {

    private UserService userService;
    private ActivationService activationService;

    @Autowired
    public AuthorizationController(UserService userService, ActivationService activationService) {
        this.userService = userService;
        this.activationService = activationService;
    }

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@RequestBody LoginRequest loginRequest) {

        LoginRequestValidator.validate(loginRequest);

        User currentUser = userService.getByUsername(loginRequest.getUsername());

        UserLoginSuccessResponse userLoginSuccessResponse = UserLoginSuccessResponse.builder()
                .success(true)
                .token(activationService.isUserActivated(loginRequest))
                .username(currentUser.getUsername())
                .email(currentUser.getEmail())
                .creationDate(currentUser.getCreationDate()).build();


        return ResponseEntity.ok(userLoginSuccessResponse);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) throws ValidationException {

        UserValidator.validate(user);
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @GetMapping("/activate")
    public RedirectView activate(@PathParam("activationUrl") String activationUrl) {

        activationService.activateUser(activationUrl);

        return new RedirectView("https://quiz-app-nc.herokuapp.com/log-in");
    }
}

