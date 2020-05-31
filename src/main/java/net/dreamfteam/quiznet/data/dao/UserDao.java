package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.data.entities.UserFriendInvitation;
import net.dreamfteam.quiznet.data.entities.UserView;

import java.util.List;

public interface UserDao {

    User getByEmail(String email);

    User getByUsername(String username);

    User getById(String id);

    User save(User user);

    void deleteById(String id);

    void update(User user);

    List<User> getAll();

    User getByActivationUrl(String activationUrl);

    User getByRecoverUrl(String recoverUrl);

    List<User> getAllByRoleUser();

    List<User> getBySubStr(String str);

    List<User> getBySubStrAndRoleUser(String str);

    int deleteIfLinkExpired();

    List<UserView> getFriendsByUserId(int startIndex, int amount, String userId);

    int getFriendsTotalSize(String userId);

    List<UserFriendInvitation> getFriendInvitationsIncomingByUserId(int startIndex, int amount, String userId);

    int getFriendInvitationsIncomingTotalSize(String userId);

    List<UserFriendInvitation> getFriendInvitationsOutgoingByUserId(int startIndex, int amount, String userId);

    int getFriendInvitationsOutgoingTotalSize(String userId);

    boolean processOutgoingFriendInvitation(String parentId, String targetId, boolean toInvite);

    int acceptInvitation(String parentId, String targetId);

    void rejectInvitation(String parentId, String targetId);

    User getFriendsRelations(User targetUser, String thisUser);

    void removeFriend(String targetId, String thisId);

    List<User> getPopularCreators();

    List<User> getPrivilegedUsers();
}
