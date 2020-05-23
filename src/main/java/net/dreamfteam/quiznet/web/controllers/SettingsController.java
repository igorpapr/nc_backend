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

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(Constants.SETTING_URLS)
public class SettingsController {

    private final SettingsService settingsService;


    @Autowired
    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @PostMapping
    public ResponseEntity<?> setSettings(@RequestBody List<DtoSettings> dtoSettings) throws ValidationException {
        SettingsValidator.validate(dtoSettings);
        settingsService.editSettings(dtoSettings);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping
    public ResponseEntity<?> getSettings() throws ValidationException {
        return new ResponseEntity<>(settingsService.getSettings(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','SUPERADMIN')")
    @GetMapping("language")
    public ResponseEntity<?> getLanguage(){
        return new ResponseEntity<>(settingsService.getLanguage(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("notifications")
    public ResponseEntity<?> getNotificationsSetting(){
        return new ResponseEntity<>(settingsService.getNotificationSetting(), HttpStatus.OK);
    }
}
