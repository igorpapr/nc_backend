package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.Announcement;

import java.util.List;

public interface AnnouncementDao {

    Announcement createAnnouncement(Announcement announcement);

    Announcement getAnnouncement(String announcementId);

    List<Announcement> getAllAnnouncements(long start, long amount);

    Announcement editAnnouncement(Announcement announcement);

    void deleteAnnouncementById(String announcementId);

    long getAmount();
}
