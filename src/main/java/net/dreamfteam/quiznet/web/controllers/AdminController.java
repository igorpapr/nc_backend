package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.UserService;
import net.dreamfteam.quiznet.web.dto.DtoAdminActivation;
import net.dreamfteam.quiznet.web.dto.DtoAdminSignUp;
import net.dreamfteam.quiznet.web.dto.DtoEditAdminProfile;
import net.dreamfteam.quiznet.web.dto.DtoUser;
import net.dreamfteam.quiznet.web.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(Constants.ADMIN_URLS)
public class AdminController {

    private UserService userService;
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    public AdminController(UserService userService, IAuthenticationFacade authenticationFacade) {
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
    }

    @PostMapping("/edit/{field}")
    public ResponseEntity<?> activate(@PathVariable("field") String field, @RequestBody DtoEditAdminProfile editAdminProfile) {

        User currentUser = userService.getById(authenticationFacade.getUserId());
        User otherUser = userService.getById(editAdminProfile.getId());

        if (otherUser == null) {
            throw new ValidationException("Not found such user");
        }

        if (otherUser.getRole() == Role.ROLE_USER) {
            throw new ValidationException("You dont have such capabilities");
        }

        if (field.equals("role") && !StringUtils.isEmpty(editAdminProfile.getRole())) {
            if (currentUser.getRole() != Role.ROLE_SUPERADMIN) {
                throw new ValidationException("You dont have such capabilities");
            }

            otherUser.setRole(Role.valueOf(editAdminProfile.getRole()));
        }

        if (currentUser.getRole().ordinal() <= otherUser.getRole().ordinal()) {
            throw new ValidationException("You dont have such capabilities");
        }

        if (field.equals("image")) {
            otherUser.setImage(editAdminProfile.getImage());
            userService.update(otherUser);
        }

        if (field.equals("aboutMe")) {
            otherUser.setAboutMe(editAdminProfile.getAboutMe());
            userService.update(otherUser);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DtoUser> create(@RequestBody DtoAdminSignUp newAdmin) {

        User currentUser = userService.getById(authenticationFacade.getUserId());
        UserValidator.validate(newAdmin);
        User user = userService.getByEmail(newAdmin.getEmail());

        if (user != null) {
            throw new ValidationException("Such email has been taken");
        }

        if (userService.getByUsername(newAdmin.getUsername()) != null) {
            throw new ValidationException("Such username has been taken");
        }

        if (!(currentUser.getRole().ordinal() > Role.valueOf(newAdmin.getRole()).ordinal())) {
            throw new ValidationException("You dont have such capabilities");
        }

        User saved = userService.saveAdmin(newAdmin.toUser());
        return new ResponseEntity<>(DtoUser.fromUser(saved), HttpStatus.OK);
    }

    @PostMapping("/activation")
    public ResponseEntity<?> activation(@RequestBody DtoAdminActivation admin) {

        UserValidator.validate(admin);

        User currentUser = userService.getById(authenticationFacade.getUserId());
        User userToChange = userService.getById(admin.getId());

        if (currentUser.getRole().ordinal() > userToChange.getRole().ordinal()) {
            userToChange.setActivated(admin.isActivated());
            userService.update(userToChange);
        } else throw new ValidationException("You dont have such capabilities");

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
