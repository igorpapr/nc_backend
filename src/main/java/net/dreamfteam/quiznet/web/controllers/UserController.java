package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.ImageService;
import net.dreamfteam.quiznet.service.UserService;
import net.dreamfteam.quiznet.web.dto.DtoUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(Constants.USER_URLS)
public class UserController {

    final private UserService userService;
    final private ImageService imageService;
    final private IAuthenticationFacade authenticationFacade;

    public UserController(UserService userService, ImageService imageService, IAuthenticationFacade authenticationFacade) {
        this.userService = userService;
        this.imageService = imageService;
        this.authenticationFacade = authenticationFacade;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/edit/aboutMe")
    public ResponseEntity<?> activate(@RequestParam("key") String aboutMe) {

        User currentUser = userService.getById(authenticationFacade.getUserId());

        currentUser.setAboutMe(aboutMe);
        userService.update(currentUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/edit/image")
    public ResponseEntity<?> activate(@RequestParam("key") MultipartFile image) {

        User currentUser = userService.getById(authenticationFacade.getUserId());

        currentUser.setImage(imageService.saveImage(image));

        userService.update(currentUser);

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

        DtoUser dtoUser = DtoUser.fromUser(user);
        dtoUser.setImageContent(imageService.loadImage(dtoUser.getImage()));

        return new ResponseEntity<>(dtoUser, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<DtoUser>> getProfileByStr(@PathParam("key") String key) {

        User currentUser = userService.getById(authenticationFacade.getUserId());

        List<User> users = userService.getBySubStr(key, currentUser.getRole());

        List<DtoUser> dtoUsers = DtoUser.fromUser(users);

        dtoUsers
                .forEach(dtoUser -> dtoUser.setImageContent(imageService.loadImage(dtoUser.getImage())));

        return new ResponseEntity<>(dtoUsers, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<DtoUser>> getAllProfiles() {

        User currentUser = userService.getById(authenticationFacade.getUserId());
        List<DtoUser> dtoUsers = DtoUser.fromUser(userService.getAllByRole(currentUser.getRole()));
        dtoUsers
                .forEach(dtoUser -> dtoUser.setImageContent(imageService.loadImage(dtoUser.getImage())));

        return new ResponseEntity<>(dtoUsers, HttpStatus.OK);
    }
}
