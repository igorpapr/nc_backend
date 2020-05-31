package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.configs.constants.SqlConstants;
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

    final private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(SqlConstants.SELECT_USER_QUERY,
                new UserMapper()

        );
    }

    @Override
    public User getByActivationUrl(String activationUrl) {
        try {
            return jdbcTemplate.queryForObject(SqlConstants.USERS_GET_BY_ACTIVATION_URL,
                    new Object[]{activationUrl},
                    new UserMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public User getByRecoverUrl(String recoverUrl) {
        try {
            return jdbcTemplate.queryForObject(SqlConstants.USERS_GET_BY_RECOVERY_URL,
                    new Object[]{recoverUrl},
                    new UserMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public List<User> getAllByRoleUser() {
        return jdbcTemplate.query(SqlConstants.USERS_GET_ALL_BY_ROLE_USER,
                new UserMapper()
        );
    }

    @Override
    public List<User> getBySubStr(String str) {
        return jdbcTemplate.query(SqlConstants.USERS_GET_BY_USERNAME_SUBSTR,
                new UserMapper(),
                str + "%");
    }

    @Override
    public List<User> getBySubStrAndRoleUser(String str) {
        return jdbcTemplate.query(SqlConstants.USERS_GET_BY_USERNAME_SUBSTR_AND_ROLE_USER,
                new UserMapper(),
                str + "%");
    }


    @Override
    public User getByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(SqlConstants.USERS_GET_BY_EMAIL,
                    new Object[]{email},
                    new UserMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public User getByUsername(String username) {

        try {
            return jdbcTemplate.queryForObject(SqlConstants.USERS_GET_BY_USERNAME,
                    new Object[]{username},
                    new UserMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public User getById(String id) {

        try {
            return jdbcTemplate.queryForObject(SqlConstants.USERS_GET_BY_ID,
                    new Object[]{id},
                    new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User save(User user) {
        jdbcTemplate.update(SqlConstants.USERS_SAVE_QUERY,
                UUID.randomUUID(), user.getUsername(), user.getEmail(), user.getPassword(),
                user.isActivated(), user.isVerified(), user.getActivationUrl(),
                user.getCreationDate(), user.getCreationDate(), user.getRole().ordinal() + 1
        );

        return getByEmail(user.getEmail());

    }

    @Override
    public void deleteById(String id) {
        jdbcTemplate.update(SqlConstants.USERS_DELETE_BY_ID, id);
    }

    @Override
    public int deleteIfLinkExpired() {
        return jdbcTemplate
                .update(SqlConstants.USERS_DELETE_IF_LINK_EXPIRED);
    }


    @Override
    public void update(User user) {
        jdbcTemplate.update(SqlConstants.USERS_UPDATE_QUERY_BY_ID,
                user.getUsername(), user.getEmail(), user.getPassword(), user.isActivated(), user.isVerified(),
                user.getLastTimeOnline(), user.getImage(), user.getAboutMe(), user.getRecoveryUrl(),
                user.getRecoverySentTime(), user.getRole().ordinal() + 1, user.getId());

    }

    @Override
    public List<UserView> getFriendsByUserId(int startIndex, int amount, String userId) {
        try {
            return jdbcTemplate
                    .query(SqlConstants.USERS_GET_ALL_FRIENDS_BY_USER_ID,
                            new Object[]{userId, userId, amount, startIndex},
                            new UserViewMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int getFriendsTotalSize(String userId) {
        try {
            return jdbcTemplate.queryForObject(SqlConstants.USERS_GET_FRIENDS_TOTAL_SIZE,
                    new Object[]{userId, userId},
                    Integer.class);
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return 0;
        }
    }

    @Override
    public List<UserFriendInvitation> getFriendInvitationsIncomingByUserId(int startIndex, int amount, String userId) {
        try {
            return jdbcTemplate.query(SqlConstants.USERS_GET_FRIEND_INCOMING_INVITATIONS_BY_USER_ID,
                    new Object[]{userId, amount, startIndex},
                    new UserFriendInvitationMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int getFriendInvitationsIncomingTotalSize(String userId) {
        try {
            return jdbcTemplate.queryForObject(SqlConstants.USERS_GET_FRIEND_INCOMING_INVITATIONS_TOTAL_SIZE,
                    new Object[]{userId},
                    Integer.class);
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return 0;
        }
    }

    @Override
    public List<UserFriendInvitation> getFriendInvitationsOutgoingByUserId(int startIndex, int amount, String userId) {
        try {
            return jdbcTemplate.query(SqlConstants.USERS_GET_FRIEND_OUTGOING_INVITATIONS_BY_USER_ID,
                    new Object[]{userId, amount, startIndex},
                    new UserFriendInvitationMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int getFriendInvitationsOutgoingTotalSize(String userId) {
        try {
            return jdbcTemplate.queryForObject(SqlConstants.USERS_GET_FRIEND_OUTGOING_INVITATIONS_TOTAL_SIZE,
                    new Object[]{userId},
                    Integer.class);
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return 0;
        }
    }

    @Override
    public boolean processOutgoingFriendInvitation(String parentId, String targetId, boolean toInvite) {
        if (toInvite) {
            jdbcTemplate.update(SqlConstants.USERS_SAVE_OUTGOING_FRIEND_INVITATION,
                    parentId, targetId
            );
            return true;
        } else {
            jdbcTemplate.update(SqlConstants.USERS_DELETE_OUTGOING_FRIEND_INVITATION,
                    parentId, targetId, parentId, targetId);
            return false;
        }

    }

    @Override
    public int acceptInvitation(String parentId, String targetId) {
        try {
            return jdbcTemplate.update(SqlConstants.USERS_ACCEPT_FRIEND_INIVITAION,
                    parentId, targetId);
        } catch (DataAccessException e) {
            System.err.println("Error occurred while accepting the friend invitation: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public void rejectInvitation(String parentId, String targetId) {
        jdbcTemplate.update(SqlConstants.USERS_REJECT_FRIEND_INVITATION,
                parentId, targetId);
    }

    @Override
    public User getFriendsRelations(User targetUser, String thisUserId) {
        try {
            return jdbcTemplate.queryForObject(SqlConstants.USERS_GET_FRIENDS_RELATIONS,
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
        jdbcTemplate.update(SqlConstants.USERS_REMOVE_FRIEND,
                targetId, thisId, targetId, thisId, targetId, thisId);
    }

    @Override
    public List<User> getPopularCreators() {
        return jdbcTemplate.query(SqlConstants.USERS_GET_ALL_POPULAR_CREATORS,
                new UserMapper());
    }

    @Override
    public List<User> getPrivilegedUsers() {
        return jdbcTemplate.query(SqlConstants.USERS_GET_ALL_PRIVILIGED,
                new UserMapper());
    }

}
