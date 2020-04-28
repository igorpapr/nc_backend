package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.AnnouncementService;
import net.dreamfteam.quiznet.web.dto.DtoAnnouncement;
import net.dreamfteam.quiznet.web.dto.DtoEditAnnouncement;
import net.dreamfteam.quiznet.web.validators.AnnouncementValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.isNull;

@RestController
@CrossOrigin
@RequestMapping(Constants.ANNOUNCEMENT_URLS)
public class AnnouncementController {
    private AnnouncementService announcementService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;

    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createAnnouncement(@RequestBody DtoAnnouncement dtoAnnouncement) throws ValidationException {
        AnnouncementValidator.validate(dtoAnnouncement);
        return new ResponseEntity<>(announcementService.createAnnouncement(dtoAnnouncement), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN')")
    @PostMapping("/edit")
    public ResponseEntity<?> editAnnouncement(@RequestBody DtoEditAnnouncement dtoAnnouncement) throws ValidationException {
        AnnouncementValidator.validateForEdit(dtoAnnouncement);
        return new ResponseEntity<>(announcementService.editAnnouncement(dtoAnnouncement), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN')")
    @DeleteMapping("/delete/{announcementId}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable String announcementId) throws ValidationException {
        if (isNull(announcementService.getAnnouncement(announcementId))) {
            return ResponseEntity.notFound().build();
        }
        announcementService.deleteAnnouncementById(announcementId);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/getall")
    public ResponseEntity<?> getAnnouncements(@RequestParam long start, @RequestParam long amount) throws ValidationException {
        return new ResponseEntity<>(announcementService.getAllAnnouncements(start, amount), HttpStatus.OK);
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<?> getAnnouncementById(@PathVariable String id) throws ValidationException {
        return new ResponseEntity<>(announcementService.getAnnouncement(id), HttpStatus.OK);
    }


}
