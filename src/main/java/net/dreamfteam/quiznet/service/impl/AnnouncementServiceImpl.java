package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.data.dao.AnnouncementDao;
import net.dreamfteam.quiznet.data.entities.Announcement;
import net.dreamfteam.quiznet.service.AnnouncementService;
import net.dreamfteam.quiznet.web.dto.DtoAnnouncement;
import net.dreamfteam.quiznet.web.dto.DtoEditAnnouncement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    AnnouncementDao announcementDao;

    @Autowired
    public AnnouncementServiceImpl(AnnouncementDao announcementDao) {
        this.announcementDao = announcementDao;
    }

    @Override
    public Announcement createAnnouncement(DtoAnnouncement ann) {
        Announcement announcement = Announcement.builder()
                .creatorId(ann.getCreatorId())
                .title(ann.getTitle()).
                        image(ann.getImage())
                .textContent(ann.getTextContent())
                .creationDate(Calendar.getInstance().getTime())
                .publicationDate(Calendar.getInstance().getTime())
                .isPublished(true).build();

        return announcementDao.createAnnouncement(announcement);
    }

    @Override
    public Announcement getAnnouncement(String announcementId, String creatorId) {
        return announcementDao.getAnnouncement(announcementId, creatorId);
    }

    @Override
    public Announcement editAnnouncement(DtoEditAnnouncement ann) {
        Announcement announcement = Announcement.builder().announcementId(ann.getAnnouncementId())
                .creatorId(ann.getCreatorId())
                .title(ann.getTitle()).
                        image(ann.getImage())
                .textContent(ann.getTextContent())
                .creationDate(Calendar.getInstance().getTime())
                .publicationDate(Calendar.getInstance().getTime())
                .isPublished(true).build();

        return announcementDao.editAnnouncement(announcement);
    }

    @Override
    public void deleteAnnouncementById(String id) {
        announcementDao.deleteAnnouncementById(id);
    }
}
