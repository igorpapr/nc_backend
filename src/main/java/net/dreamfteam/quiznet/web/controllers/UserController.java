package net.dreamfteam.quiznet.web.controllers;


import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.UserService;
import net.dreamfteam.quiznet.web.dto.DtoEditProfile;
import net.dreamfteam.quiznet.web.dto.DtoUser;
import net.dreamfteam.quiznet.web.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(Constants.USER_URLS)
public class UserController {

    private UserService userService;
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    public UserController(UserService userService, IAuthenticationFacade authenticationFacade) {
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
    }

    @PostMapping("/edit/{field}")
    public ResponseEntity<?> activate(@PathVariable("field") String field, @RequestBody DtoEditProfile editProfile) {
        User currentUser = userService.getById(authenticationFacade.getUserId());

        if (field.equals("image")) {
            currentUser.setImage(editProfile.getImage());
            userService.update(currentUser);
        }

        if (field.equals("aboutMe")) {
            currentUser.setAboutMe(editProfile.getAboutMe());
            userService.update(currentUser);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<DtoUser> getProfile(@PathVariable String userId) {

        User user = userService.getById(userId);

        return new ResponseEntity<>(DtoUser.fromUser(user), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<DtoUser>> getAllProfiles() {

        User currentUser = userService.getById(authenticationFacade.getUserId());

        return new ResponseEntity<>(DtoUser.fromUser(userService.getAll()), HttpStatus.OK);
    }
}
