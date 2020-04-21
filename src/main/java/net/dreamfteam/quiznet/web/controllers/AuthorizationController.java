package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.ActivationService;
import net.dreamfteam.quiznet.service.UserService;
import net.dreamfteam.quiznet.web.dto.DtoUser;
import net.dreamfteam.quiznet.web.dto.DtoUserSignUp;
import net.dreamfteam.quiznet.web.dto.LoginRequest;
import net.dreamfteam.quiznet.web.dto.UserLoginSuccessResponse;
import net.dreamfteam.quiznet.web.validators.LoginRequestValidator;
import net.dreamfteam.quiznet.web.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/log-in")
    public ResponseEntity<UserLoginSuccessResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {

        LoginRequestValidator.validate(loginRequest);

        User currentUser = userService.getByUsername(loginRequest.getUsername());

        UserLoginSuccessResponse successResponse = UserLoginSuccessResponse.fromUser(currentUser);

        successResponse.setSuccess(true);
        successResponse.setToken(activationService.isUserActivated(currentUser.getUsername()));
        successResponse.setOnline(true);

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<DtoUser> registerUser(@RequestBody DtoUserSignUp user) throws ValidationException {

        UserValidator.validate(user);
        User saved = userService.save(user.toUser());

        return new ResponseEntity<>(DtoUser.fromUser(saved), HttpStatus.CREATED);
    }

    @GetMapping("/activation")
    public RedirectView activate(@PathParam("key") String activationUrl) {

        return new RedirectView("https://quiz-app-nc.herokuapp.com/log-in?message=" + activationService.activateUser(activationUrl));

    }
}

