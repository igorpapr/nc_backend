package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.Announcement;

public interface AnnouncementDao {

    Announcement createAnnouncement(Announcement announcement);

    Announcement getAnnouncement(String announcementId, String creatorId);

    Announcement editAnnouncement(Announcement announcement);

    void deleteAnnouncementById(String announcementId);
}
