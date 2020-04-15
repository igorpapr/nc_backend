package net.dreamfteam.quiznet.web.controllers;


import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.ActivationService;
import net.dreamfteam.quiznet.service.UserService;
import net.dreamfteam.quiznet.web.dto.JWTLoginSuccessResponse;
import net.dreamfteam.quiznet.web.dto.LoginRequest;
import net.dreamfteam.quiznet.web.validators.LoginRequestValidator;
import net.dreamfteam.quiznet.web.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
        return ResponseEntity.ok(new JWTLoginSuccessResponse(true, activationService.isUserActivated(loginRequest)));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) throws ValidationException {

        UserValidator.validate(user);
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @GetMapping("/activate/{hashedId}")
    public String activate(@PathVariable String hashedId) {
        activationService.activateUser(hashedId);
        return "<img style='width:100%' 'height:100%' 'text-align: center' src='https://cdn1.savepice.ru/uploads/2019/11/21/bcadc0172fce5e6a398bb4edcdf8bf7a-full.jpg'>";
    }


}

