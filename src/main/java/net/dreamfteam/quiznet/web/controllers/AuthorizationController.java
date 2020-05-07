package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.ActivationService;
import net.dreamfteam.quiznet.service.SettingsService;
import net.dreamfteam.quiznet.service.UserService;
import net.dreamfteam.quiznet.web.dto.DtoUser;
import net.dreamfteam.quiznet.web.dto.DtoUserSignUp;
import net.dreamfteam.quiznet.web.dto.LoginRequest;
import net.dreamfteam.quiznet.web.dto.UserLoginSuccessResponse;
import net.dreamfteam.quiznet.web.validators.LoginRequestValidator;
import net.dreamfteam.quiznet.web.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.websocket.server.PathParam;

@RestController
@CrossOrigin
@RequestMapping(Constants.SIGN_UP_URLS)
public class AuthorizationController {

    @Value("${activation.redirect.url}")
    private String ACTIVATION_REDIRECT_URL;

    private SettingsService settingsService;
    final private UserService userService;
    final private ActivationService activationService;

    @Autowired
    public AuthorizationController(UserService userService, ActivationService activationService, SettingsService settingsService) {
        this.userService = userService;
        this.activationService = activationService;
        this.settingsService = settingsService;
    }

    @PostMapping("/log-in")
    public ResponseEntity<UserLoginSuccessResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {

        LoginRequestValidator.validate(loginRequest);

        User currentUser = userService.getByEmail(loginRequest.getUsername());

        if (currentUser == null) {
            currentUser = userService.getByUsername(loginRequest.getUsername());
        }

        if (currentUser == null) {
            throw new ValidationException("User not found with such username or email" + loginRequest.getUsername());
        }

        userService.checkCorrectPassword(currentUser, loginRequest.getPassword());

        UserLoginSuccessResponse successResponse = UserLoginSuccessResponse.fromUser(currentUser);

        successResponse.setToken(activationService.isUserVerified(currentUser));
        successResponse.setOnline(true);
        successResponse.setSuccess(true);

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<DtoUser> registerUser(@RequestBody DtoUserSignUp user) throws ValidationException {
        UserValidator.validate(user);
        User saved = userService.save(user.toUser());
        settingsService.initSettings(saved.getId());
        System.out.println(saved.getId());
        return new ResponseEntity<>(DtoUser.fromUser(saved), HttpStatus.CREATED);
    }

    @GetMapping("/activation")
    public RedirectView activate(@PathParam("key") String key) {

        return new RedirectView(ACTIVATION_REDIRECT_URL + activationService.verifyUser(key));
    }
}

