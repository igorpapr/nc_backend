package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.data.entities.UserFriendInvitation;
import net.dreamfteam.quiznet.data.entities.UserView;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.AchievementService;
import net.dreamfteam.quiznet.service.ImageService;
import net.dreamfteam.quiznet.service.UserService;
import net.dreamfteam.quiznet.web.dto.DtoUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
    final private AchievementService achievementService;

    public UserController(UserService userService, ImageService imageService,
                          IAuthenticationFacade authenticationFacade, AchievementService achievementService) {
        this.userService = userService;
        this.imageService = imageService;
        this.authenticationFacade = authenticationFacade;
        this.achievementService = achievementService;
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
        } else if (currentUser.getRole().ordinal() == 0){
             userService.getFriendsRelations(user, currentUser.getId());
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

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping("/{userId}/friends/page/{page}")
    public ResponseEntity<List<UserView>> getFriends(@PathVariable String userId, @PathVariable int page)
            throws ValidationException{
        return new ResponseEntity<>(
                userService.getFriendsListByUserId((page - 1) * Constants.AMOUNT_FRIENDS_ON_PAGE,
                        Constants.AMOUNT_FRIENDS_ON_PAGE, userId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping("/{userId}/friendstotalsize")
    public ResponseEntity<?> getFriendsTotalSize(@PathVariable String userId) throws ValidationException {
        return new ResponseEntity<>(userService.getFriendsTotalSize(userId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/friends/invitations/page/{page}")
    public ResponseEntity<List<UserFriendInvitation>> getInvitations(@PathVariable int page)
        throws ValidationException {
        return new ResponseEntity<>(
                userService.getFriendInvitationsListByUserId((page - 1) * Constants.AMOUNT_INVITATIONS_ON_PAGE,
                        Constants.AMOUNT_INVITATIONS_ON_PAGE, authenticationFacade.getUserId()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/friends/invitationstotalsize")
    public ResponseEntity<?> getInvitationsTotalSize() {
        return new ResponseEntity<>(
                userService.getFriendInvitationsTotalSize(authenticationFacade.getUserId()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/friends/invite")
    public ResponseEntity<?> inviteToBecomeFriends(@RequestParam String targetId) throws ValidationException{
        userService.inviteToBecomeFriends(authenticationFacade.getUserId(), targetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/friends/proceed")
    public ResponseEntity<?> proceedInvitation(@RequestParam String targetId, @RequestParam boolean toAccept)
        throws ValidationException {
        userService.proceedInvitation(authenticationFacade.getUserId(), targetId, toAccept);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{userId}/achievements")
    public ResponseEntity<?> getUserAchievements(@PathVariable String userId) throws ValidationException{
        System.out.println(userId);
        return new ResponseEntity<>(achievementService.getUserAchievements(userId), HttpStatus.OK);

    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/friends/remove")
    public ResponseEntity<?> proceedInvitation(@RequestParam String targetId) throws ValidationException {
        userService.removeFriend(targetId, authenticationFacade.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
