package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.configs.security.AuthenticationFacade;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.SettingsService;
import net.dreamfteam.quiznet.web.dto.DtoSettings;
import net.dreamfteam.quiznet.web.validators.SettingsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(Constants.SETTING_URLS)
public class SettingsController {

    private SettingsService settingsService;
    private AuthenticationFacade authenticationFacade;

    @Autowired
    public SettingsController(SettingsService settingsService, AuthenticationFacade authenticationFacade) {
        this.settingsService = settingsService;
        this.authenticationFacade = authenticationFacade;

    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @PostMapping
    public ResponseEntity<?> setSettings(@RequestBody DtoSettings dtoSettings) throws ValidationException {
        SettingsValidator.validate(dtoSettings);
        return new ResponseEntity<>(settingsService.editSettings(dtoSettings), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping
    public ResponseEntity<?> getSettings() throws ValidationException {
        return new ResponseEntity<>(settingsService.getSettings(authenticationFacade.getUserId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @PostMapping("/init")
    public ResponseEntity<?> initSettings() throws ValidationException {
        settingsService.initSettings(authenticationFacade.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
