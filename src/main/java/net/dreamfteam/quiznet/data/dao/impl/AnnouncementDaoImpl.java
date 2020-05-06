package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.AnnouncementDao;
import net.dreamfteam.quiznet.data.entities.Announcement;
import net.dreamfteam.quiznet.data.rowmappers.AnnouncementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Repository
public class AnnouncementDaoImpl implements AnnouncementDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AnnouncementDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Announcement createAnnouncement(Announcement ann) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO announcements " +
                            "(creator_id, title, text_content, image, datetime_creation," +
                            " datetime_publication, is_published) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, java.util.UUID.fromString(ann.getCreatorId()));
            ps.setString(2, ann.getTitle());
            ps.setString(3, ann.getTextContent());
            ps.setBytes(4, ann.getImage());
            ps.setTimestamp(5, new Timestamp(ann.getCreationDate().getTime()));
            ps.setTimestamp(6, new Timestamp(ann.getPublicationDate().getTime()));
            ps.setBoolean(7, true);
            return ps;
        }, keyHolder);

        ann.setAnnouncementId(Objects.requireNonNull(keyHolder.getKeys()).get("announcement_id").toString());
        return ann;
    }

    @Override
    public Announcement getAnnouncement(String announcementId) {
        try {
            return jdbcTemplate.queryForObject("SELECT announcement_id,  creator_id, title, text_content, announcements.image, datetime_creation, is_published, datetime_publication from announcements join users on announcements.creator_id = users.user_id where announcement_id = UUID(?)",
                    new Object[]{announcementId},
                    new AnnouncementMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Override
    public List<Announcement> getAllAnnouncements(long start, long amount) {
        try {
            return jdbcTemplate.query("SELECT announcement_id, username, title, text_content, announcements.image, datetime_creation, is_published, datetime_publication from announcements join users on announcements.creator_id = users.user_id where datetime_publication < current_timestamp order by datetime_publication desc limit ? offset ? rows;",
                    new Object[]{amount, start}, new AnnouncementMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Announcement editAnnouncement(Announcement ann) {
        jdbcTemplate.update("UPDATE announcements SET creator_id = UUID(?), title = ?,  text_content = ?, image = ?, "
                + "is_published = ?  WHERE  announcement_id = UUID(?)", ann.getCreatorId(), ann.getTitle(), ann.getTextContent(), ann.getImage(), true,  ann.getAnnouncementId());
        return ann;
    }

    @Override
    public void deleteAnnouncementById(String announcementId) {
        jdbcTemplate.update("DELETE FROM announcementS WHERE announcement_id = UUID(?) ", announcementId);
    }

    @Override
    public long getAmount() {
        try {
            return jdbcTemplate.queryForObject("SELECT count(*) from announcements", Long.class);

        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return 0;
        }
    }
}
