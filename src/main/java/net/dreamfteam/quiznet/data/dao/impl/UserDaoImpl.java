package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.UserDao;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.data.entities.UserFriendInvitation;
import net.dreamfteam.quiznet.data.entities.UserView;
import net.dreamfteam.quiznet.data.rowmappers.UserFriendInvitationMapper;
import net.dreamfteam.quiznet.data.rowmappers.UserMapper;
import net.dreamfteam.quiznet.data.rowmappers.UserViewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public class UserDaoImpl implements UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                SELECT_QUERY,
                new UserMapper()

        );
    }

    @Override
    public User getByActivationUrl(String activationUrl) {
        try {
            return jdbcTemplate.queryForObject(SELECT_QUERY +
                            "WHERE activation_url = ?",
                    new Object[]{activationUrl},
                    new UserMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public User getByRecoverUrl(String recoverUrl) {
        try {
            return jdbcTemplate.queryForObject(SELECT_QUERY +
                            "WHERE recovery_url = ?",
                    new Object[]{recoverUrl},
                    new UserMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public List<User> getAllByRoleUser() {
        return jdbcTemplate.query(SELECT_QUERY +
                        "WHERE roles.role_id = 1 and is_activated = true order by last_time_online desc",
                new UserMapper()
        );
    }

    @Override
    public List<User> getBySubStr(String str) {
        return jdbcTemplate.query(SELECT_QUERY +
                        "WHERE LOWER(username) LIKE LOWER(?) order by role , last_time_online desc",
                new UserMapper(),
                str + "%");
    }

    @Override
    public List<User> getBySubStrAndRoleUser(String str) {
        return jdbcTemplate.query(SELECT_QUERY +
                        "WHERE LOWER(username) LIKE LOWER(?)" +
                        "AND roles.role_id = 1 and is_activated = true ORDER BY  last_time_online desc",
                new UserMapper(),
                str + "%");
    }


    @Override
    public User getByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(SELECT_QUERY +
                            "WHERE email = ?",
                    new Object[]{email},
                    new UserMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public User getByUsername(String username) {

        try {
            return jdbcTemplate.queryForObject(SELECT_QUERY +
                            "WHERE username=?",
                    new Object[]{username},
                    new UserMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public User getById(String id) {

        try {
            return jdbcTemplate.queryForObject(SELECT_QUERY +
                            " WHERE user_id = UUID(?)",
                    new Object[]{id},
                    new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User save(User user) {
        jdbcTemplate.update(SAVE_QUERY,
                UUID.randomUUID(), user.getUsername(), user.getEmail(), user.getPassword(), user.isActivated(),
                user.isVerified(), user.getActivationUrl(), user.getCreationDate(), user.getCreationDate(),
                user.getRole().ordinal() + 1
        );

        return getByEmail(user.getEmail());

    }

    @Override
    public void deleteById(String id) {
        jdbcTemplate.update(DELETE_QUERY + " WHERE user_id = UUID( ?)", id);
    }

    @Override
    public int deleteIfLinkExpired() {
        return jdbcTemplate
                .update(DELETE_QUERY +
                        " WHERE is_verified = 'false' and current_timestamp - date_acc_creation >= '1 DAY'");
    }


    @Override
    public void update(User user) {
        jdbcTemplate.update(UPDATE_QUERY +
                        "WHERE user_id = UUID(?)",
                user.getUsername(), user.getEmail(), user.getPassword(), user.isActivated(), user.isVerified(),
                user.getLastTimeOnline(), user.getImage(), user.getAboutMe(), user.getRecoveryUrl(),
                user.getRecoverySentTime(), user.getRole().ordinal() + 1, user.getId());

    }

    @Override
    public List<UserView> getFriendsByUserId(int startIndex, int amount, String userId) {
        try {
            return jdbcTemplate
                    .query("SELECT user_id, username, last_time_online, image AS image_content " +
                            "FROM users WHERE user_id IN   (SELECT f.friend_id AS id " +
                            "FROM friends f " +
                            "WHERE f.parent_id = uuid(?) " +
                            "AND f.accepted_datetime IS NOT NULL " +
                            "UNION " +
                            "SELECT f1.parent_id AS id " +
                            "FROM friends f1 " +
                            "WHERE f1.friend_id = uuid(?) " +
                            "AND f1.accepted_datetime IS NOT NULL) " +
                            "LIMIT ? OFFSET ?;", new Object[]{userId, userId, amount, startIndex}, new UserViewMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int getFriendsTotalSize(String userId) {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) AS total_size " +
                            "FROM friends " +
                            "WHERE (parent_id = uuid(?) " +
                            "OR friend_id = uuid(?))" +
                            "AND accepted_datetime IS NOT NULL;",
                    new Object[]{userId, userId},
                    Integer.class);
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return 0;
        }
    }

    @Override
    public List<UserFriendInvitation> getFriendInvitationsIncomingByUserId(int startIndex, int amount, String userId) {
        try {
            return jdbcTemplate.query("SELECT user_id, username, invite_datetime, image AS image_content " +
                    "FROM users INNER JOIN friends f ON users.user_id = f.parent_id " +
                    "WHERE f.friend_id = uuid(?) " +
                    "AND f.accepted_datetime IS NULL " +
                    "LIMIT ? OFFSET ?;", new Object[]{userId, amount, startIndex}, new UserFriendInvitationMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int getFriendInvitationsIncomingTotalSize(String userId) {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) AS total_size " +
                            "FROM friends " +
                            "WHERE friend_id = uuid(?) " +
                            "AND accepted_datetime IS NULL;",
                    new Object[]{userId},
                    Integer.class);
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return 0;
        }
    }

    @Override
    public List<UserFriendInvitation> getFriendInvitationsOutgoingByUserId(int startIndex, int amount, String userId) {
        try {
            return jdbcTemplate.query("SELECT user_id, username, invite_datetime, image AS image_content " +
                    "FROM users INNER JOIN friends f ON users.user_id = f.friend_id " +
                    "WHERE f.parent_id = uuid(?) " +
                    "AND f.accepted_datetime IS NULL " +
                    "LIMIT ? OFFSET ?;", new Object[]{userId, amount, startIndex}, new UserFriendInvitationMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int getFriendInvitationsOutgoingTotalSize(String userId) {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) AS total_size " +
                            "FROM friends " +
                            "WHERE parent_id = uuid(?) " +
                            "AND accepted_datetime IS NULL;",
                    new Object[]{userId},
                    Integer.class);
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return 0;
        }
    }

    @Override
    public boolean processOutgoingFriendInvitation(String parentId, String targetId, boolean toInvite) {
        if (toInvite) {
            jdbcTemplate.update("INSERT INTO friends (parent_id, friend_id, invite_datetime) " +
                            "VALUES (UUID(?), UUID(?), CURRENT_TIMESTAMP);",
                    parentId, targetId
            );
            return true;
        } else {
            jdbcTemplate.update("delete from friends where friend_id in ( UUID(?), UUID(?)) and parent_id in ( UUID(?), UUID(?));",
                    parentId, targetId, parentId, targetId);
            return false;
        }

    }

    @Override
    public int acceptInvitation(String parentId, String targetId) {
        try{
            return jdbcTemplate.update("UPDATE friends " +
                    "SET accepted_datetime = CURRENT_TIMESTAMP " +
                    "WHERE parent_id = uuid(?) AND friend_id = uuid(?);", parentId, targetId);
        }catch (DataAccessException e){
            System.err.println("Error occurred while accepting the friend invitation: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public void rejectInvitation(String parentId, String targetId) {
        jdbcTemplate.update("DELETE FROM friends " +
                "WHERE parent_id = uuid(?) AND friend_id = uuid(?)", parentId, targetId);
    }

    @Override
    public User getFriendsRelations(User targetUser, String thisUserId) {
        try {
            return jdbcTemplate.queryForObject("select f.parent_id = uuid(?) as outgoing ," +
                            " f.friend_id = uuid(?) as incoming , f.accepted_datetime IS NOT NULL as friend from friends f WHERE  (f.parent_id in ( UUID(?), UUID(?)) and f.friend_id in ( UUID(?), UUID(?)))",
                    new Object[]{thisUserId, thisUserId, thisUserId, targetUser.getId(), thisUserId, targetUser.getId()},
                    (ps, i) -> {
                        targetUser.setFriend(ps.getBoolean("friend"));
                        targetUser.setIncomingRequest(ps.getBoolean("incoming"));
                        targetUser.setOutgoingRequest(ps.getBoolean("outgoing"));
                        return targetUser;

                    });
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return targetUser;
        }
    }

    @Override
    public void removeFriend(String targetId, String thisId) {
        jdbcTemplate.update("update friends set accepted_datetime = null, parent_id = UUID(?), friend_id=UUID(?)" +
                "where parent_id in ( UUID(?), UUID(?)) and friend_id in ( UUID(?), UUID(?))", targetId, thisId, targetId, thisId, targetId, thisId);
    }

    @Override
    public List<User> getPopularCreators() {
        return jdbcTemplate.query(SELECT_QUERY + "inner join " +
                        "(select creator_id, count(quiz_id) as count from quizzes " +
                        "where validated = true and published = true group by creator_id) as cic " +
                        "on creator_id = user_id where roles.role_id = 1 and is_activated = true order by count desc limit 20;\n",
                new UserMapper());
    }

    @Override
    public List<User> getPrivilegedUsers() {
        return jdbcTemplate.query(SELECT_QUERY + "where roles.role_id > 1 order by last_time_online desc;\n",
                new UserMapper());
    }

}
