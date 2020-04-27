package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.Announcement;
import net.dreamfteam.quiznet.web.dto.DtoAnnouncement;
import net.dreamfteam.quiznet.web.dto.DtoEditAnnouncement;

public interface AnnouncementService {

    Announcement createAnnouncement(DtoAnnouncement dtoAnnouncement);

    Announcement getAnnouncement(String announcementId, String creatorId);

    Announcement editAnnouncement(DtoEditAnnouncement dtoAnnouncement);

    void deleteAnnouncementById(String announcementId);

}
