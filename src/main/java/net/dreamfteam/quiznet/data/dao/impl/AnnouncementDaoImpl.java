package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.configs.constants.SqlConstants;
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
                    .prepareStatement(SqlConstants.ANNOUNCEMENTS_CREATE_ANNOUNCEMENT, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, java.util.UUID.fromString(ann.getCreatorId()));
            ps.setString(2, ann.getTitle());
            ps.setString(3, ann.getTextContent());
            ps.setTimestamp(4, new Timestamp(ann.getCreationDate().getTime()));
            ps.setTimestamp(5, new Timestamp(ann.getPublicationDate().getTime()));
            ps.setBoolean(6, true);
            ps.setBytes(7, ann.getImage());

            return ps;
        }, keyHolder);

        ann.setAnnouncementId(Objects.requireNonNull(keyHolder.getKeys()).get("announcement_id").toString());
        return ann;
    }


    @Override
    public Announcement getAnnouncement(String announcementId) {
        try {
            return jdbcTemplate.queryForObject(SqlConstants.ANNOUNCEMENTS_GET_ANNOUNCEMENT_BY_ID,
                    new Object[]{announcementId},
                    new AnnouncementMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Override
    public List<Announcement> getAllAnnouncements(long start, long amount) {
        try {
            return jdbcTemplate.query(SqlConstants.ANNOUNCEMENTS_GET_ALL_ANNOUNCEMENTS,
                    new Object[]{amount, start}, new AnnouncementMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Announcement editAnnouncement(Announcement ann, boolean newImage) {

        if(newImage){
            jdbcTemplate.update(SqlConstants.ANNOUNCEMENTS_EDIT_ANNOUNCEMENT_WITH_IMAGE,
                    ann.getCreatorId(),
                    ann.getTitle(), ann.getTextContent(), true , ann.getImage(), ann.getAnnouncementId());
        } else {
            jdbcTemplate.update(SqlConstants.ANNOUNCEMENTS_EDIT_ANNOUNCEMENT_WITHOUT_IMAGE,
                    ann.getCreatorId(),
                    ann.getTitle(), ann.getTextContent(), true, ann.getAnnouncementId());
        }

        return ann;
    }

    @Override
    public void deleteAnnouncementById(String announcementId) {
        jdbcTemplate.update(SqlConstants.ANNOUNCEMENTS_DELETE_ANNOUNCEMENT, announcementId);
    }

    @Override
    public long getAmount() {
        try {
            return jdbcTemplate.queryForObject(SqlConstants.ANNOUNCEMENTS_GET_ANNOUNCEMENTS_AMOUNT, Long.class);

        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return 0;
        }
    }

}
