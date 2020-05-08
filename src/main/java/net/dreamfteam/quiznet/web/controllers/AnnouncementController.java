package net.dreamfteam.quiznet.web.controllers;

import com.google.gson.Gson;
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
import org.springframework.web.multipart.MultipartFile;

import static java.util.Objects.isNull;

@RestController
@CrossOrigin
@RequestMapping(Constants.ANNOUNCEMENT_URLS)
public class AnnouncementController {

    private final AnnouncementService announcementService;
    private final Gson gson;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService, Gson gson) {
        this.announcementService = announcementService;
        this.gson = gson;
    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createAnnouncement(@RequestParam("obj") String announcement,
                                                @RequestParam(value = "img", required = false)  MultipartFile image)
            throws ValidationException {

        DtoAnnouncement dtoAnnouncement = gson.fromJson(announcement, DtoAnnouncement.class);

        AnnouncementValidator.validate(dtoAnnouncement);

        return new ResponseEntity<>(announcementService.createAnnouncement(dtoAnnouncement,image),HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN')")
    @PostMapping("/edit")
    public ResponseEntity<?> editAnnouncement(@RequestBody DtoEditAnnouncement dtoAnnouncement) throws ValidationException {
        AnnouncementValidator.validateForEdit(dtoAnnouncement);
        return new ResponseEntity<>(announcementService.editAnnouncement(dtoAnnouncement), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN','SUPERADMIN')")
    @PostMapping("/edit/image")
    public ResponseEntity<?> activate(@RequestParam("key") MultipartFile image, @RequestParam("id") String announcementId) {
        announcementService.uploadPicture(image, announcementId);
        return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<?> getAnnouncements(@RequestParam long start, @RequestParam long amount, @RequestParam(required = false) String userId) throws ValidationException {
        return new ResponseEntity<>(announcementService.getAllAnnouncements(start, amount, userId), HttpStatus.OK);
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<?> getAnnouncementById(@PathVariable String id) throws ValidationException {
        return new ResponseEntity<>(announcementService.getAnnouncement(id), HttpStatus.OK);
    }

    @GetMapping("/getamount")
    public ResponseEntity<?> getAmount() throws ValidationException {
        return new ResponseEntity<>(announcementService.getAmount(), HttpStatus.OK);
    }


}
