package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.constants.Constants;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.data.entities.UserFriendInvitation;
import net.dreamfteam.quiznet.data.entities.UserView;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.AchievementService;
import net.dreamfteam.quiznet.service.UserService;
import net.dreamfteam.quiznet.web.dto.DtoUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(Constants.USER_URLS)
public class UserController {

    final private UserService userService;
    final private IAuthenticationFacade authenticationFacade;
    final private AchievementService achievementService;

    public UserController(UserService userService,
                          IAuthenticationFacade authenticationFacade, AchievementService achievementService) {
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
        this.achievementService = achievementService;
    }


    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/edit/aboutMe")
    public ResponseEntity<?> activate(@RequestParam("key") String aboutMe) {

        User currentUser = userService.getById(authenticationFacade.getUserId());

        currentUser.setAboutMe(aboutMe);
        userService.update(currentUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/edit/image")
    public ResponseEntity<?> editImage(@RequestParam("key") MultipartFile image){
        User currentUser = userService.getById(authenticationFacade.getUserId());

        try {
            currentUser.setImage(image.getBytes());
        } catch (IOException e) {
            throw new ValidationException("Broken image");
        }

        userService.update(currentUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN', 'USER')")
    @GetMapping("/{userName}")
    public ResponseEntity<DtoUser> getProfile(@PathVariable String userName) {

        User currentUser = userService.getById(authenticationFacade.getUserId());
        User user = userService.getByUsername(userName);

        if (user == null) {
            throw new ValidationException("Not found");
        }

        if (currentUser.getRole().ordinal() < user.getRole().ordinal()) {
            throw new ValidationException("You dont have such capabilities");
        } else if (currentUser.getRole().ordinal() == 0) {
            userService.getFriendsRelations(user, currentUser.getId());
        }

        DtoUser dtoUser = DtoUser.fromUser(user);

        return new ResponseEntity<>(dtoUser, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN', 'USER')")
    @GetMapping("/search")
    public ResponseEntity<List<DtoUser>> getProfileByStr(@PathParam("key") String key) {

        User currentUser = userService.getById(authenticationFacade.getUserId());

        List<User> users = userService.getBySubStr(key, currentUser.getRole());

        List<DtoUser> dtoUsers = DtoUser.fromUser(users);

        return new ResponseEntity<>(dtoUsers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/popular-creators")
    public  ResponseEntity<List<?>> getPopularCreators() {
        return new ResponseEntity<>(userService.getPopularCreators(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping("/privileged-users")
    public ResponseEntity<List<?>> getPrivileged() {
        return new ResponseEntity<>(userService.getPrivilegedUsers(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<DtoUser>> getAllProfiles() {

        User currentUser = userService.getById(authenticationFacade.getUserId());
        List<DtoUser> dtoUsers = DtoUser.fromUser(userService.getAllByRole(currentUser.getRole()));
        return new ResponseEntity<>(dtoUsers, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping("/{userId}/friends/page/{page}")
    public ResponseEntity<List<UserView>> getFriends(@PathVariable String userId, @PathVariable int page)
            throws ValidationException {
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
    @GetMapping("/friends/invitations/incoming/page/{page}")
    public ResponseEntity<List<UserFriendInvitation>> getInvitationsIncoming(@PathVariable int page)
            throws ValidationException {
        return new ResponseEntity<>(
                userService.getFriendInvitationsByUserId((page - 1) * Constants.AMOUNT_INVITATIONS_ON_PAGE,
                        Constants.AMOUNT_INVITATIONS_ON_PAGE, authenticationFacade.getUserId(), true), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/friends/invitations/incoming/totalsize")
    public ResponseEntity<?> getInvitationsIncomingTotalSize() {
        return new ResponseEntity<>(
                userService.getFriendInvitationsTotalSize(authenticationFacade.getUserId(), true), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/friends/invitations/outgoing/page/{page}")
    public ResponseEntity<List<UserFriendInvitation>> getInvitationsOutgoing(@PathVariable int page)
            throws ValidationException {
        return new ResponseEntity<>(
                userService.getFriendInvitationsByUserId((page - 1) * Constants.AMOUNT_INVITATIONS_ON_PAGE,
                        Constants.AMOUNT_INVITATIONS_ON_PAGE, authenticationFacade.getUserId(), false), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/friends/invitations/outgoing/totalsize")
    public ResponseEntity<?> getInvitationsOutgoingTotalSize() {
        return new ResponseEntity<>(
                userService.getFriendInvitationsTotalSize(authenticationFacade.getUserId(), false), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/friends/invite")
    public ResponseEntity<?> inviteToBecomeFriends(@RequestParam String targetId, boolean toInvite) throws ValidationException {
        userService.inviteToBecomeFriends(authenticationFacade.getUserId(), targetId, toInvite);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/friends/process")
    public ResponseEntity<?> proceedInvitation(@RequestParam String targetId, @RequestParam boolean toAccept)
            throws ValidationException {
        userService.proceedInvitation(targetId, authenticationFacade.getUserId(), toAccept);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{userId}/achievements")
    public ResponseEntity<?> getUserAchievements(@PathVariable String userId) throws ValidationException {
        System.out.println(userId);
        return new ResponseEntity<>(achievementService.getUserAchievements(userId), HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN', 'USER')")
    @GetMapping("/{userId}/achievements/size")
    public ResponseEntity<?> getUserAchievementsAmount(@PathVariable String userId) throws ValidationException {
        return new ResponseEntity<>(achievementService.getUserAchievementsAmount(userId), HttpStatus.OK);

    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/friends/remove")
    public ResponseEntity<?> proceedInvitation(@RequestParam String targetId) throws ValidationException {
        userService.removeFriend(targetId, authenticationFacade.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/achievements/last-week")
    public ResponseEntity<?> getUserAchievementsLastWeek() {
        return new ResponseEntity<>(
                achievementService.getUserAchievementsLastWeek(), HttpStatus.OK);
    }


}
