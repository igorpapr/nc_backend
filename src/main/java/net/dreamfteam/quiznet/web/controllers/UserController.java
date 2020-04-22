package net.dreamfteam.quiznet.web.controllers;


import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.UserService;
import net.dreamfteam.quiznet.web.dto.DtoEditProfile;
import net.dreamfteam.quiznet.web.dto.DtoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
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

    @GetMapping("/{userName}")
    public ResponseEntity<DtoUser> getProfile(@PathVariable String userName) {

        User currentUser = userService.getById(authenticationFacade.getUserId());
        User user = userService.getByUsername(userName);

        if (user == null) {
            throw new ValidationException("Not found");
        }

        if (currentUser.getRole().ordinal() < user.getRole().ordinal()) {
            throw new ValidationException("You dont have such capabilities");
        }

        return new ResponseEntity<>(DtoUser.fromUser(user), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<DtoUser>> getProfileByStr(@PathParam("key") String key) {

        User currentUser = userService.getById(authenticationFacade.getUserId());

        List<User> users = userService.getBySubStr(key, currentUser.getRole(), currentUser.getId());

        return new ResponseEntity<>(DtoUser.fromUser(users), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<DtoUser>> getAllProfiles() {

        User currentUser = userService.getById(authenticationFacade.getUserId());
        List<DtoUser> dtoUsers = DtoUser.fromUser(userService.getAllByRole(currentUser.getRole(), currentUser.getId()));
        return new ResponseEntity<>(dtoUsers, HttpStatus.OK);
    }
}
