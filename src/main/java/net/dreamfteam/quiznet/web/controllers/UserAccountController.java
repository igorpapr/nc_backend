package net.dreamfteam.quiznet.web.controllers;


import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.service.UserAccountService;
import net.dreamfteam.quiznet.web.dto.DtoUpdatePassword;
import net.dreamfteam.quiznet.web.validators.RecoverDtoValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("api/account")
public class UserAccountController {

    final private UserAccountService userAccountService;

    final private IAuthenticationFacade authenticationFacade;

    public UserAccountController(UserAccountService userAccountService, IAuthenticationFacade authenticationFacade) {
        this.userAccountService = userAccountService;
        this.authenticationFacade = authenticationFacade;
    }

    //проверить изначальный пароль
    @PatchMapping("changePassword")
    public ResponseEntity<?> changePassword(@RequestBody DtoUpdatePassword dtoUpdatePassword) {

        RecoverDtoValidator.validate(dtoUpdatePassword.getNewPassword());

        userAccountService.changePassword(authenticationFacade.getUserId(), dtoUpdatePassword.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
