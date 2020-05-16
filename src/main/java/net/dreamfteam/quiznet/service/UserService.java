package net.dreamfteam.quiznet.service;


import net.dreamfteam.quiznet.data.entities.*;
import net.dreamfteam.quiznet.exception.ValidationException;

import java.util.List;

public interface UserService {

    User save(User user) throws ValidationException;

    User saveAdmin(User user);

    User getById(String id);

    User getByActivationUrl(String activationUrl);

    User getByRecoverUrl(String recoverUrl);

    List<User> getAllByRole(Role role);

    User getByUsername(String username);

    User getByEmail(String email);

    void deleteById(String id);

    void update(User user);

    User getFriendsRelations(User targetUser, String thiUserId);

    void checkCorrectPassword(User user, String password);

    List<User> getBySubStr(String substr, Role role);

    int getFriendsTotalSize(String userId);

    List<UserView> getFriendsListByUserId(int startIndex, int amount, String userId);

    List<UserFriendInvitation> getFriendInvitationsListByUserId(int startIndex, int amount, String userId);

    int getFriendInvitationsTotalSize(String userId);

    void inviteToBecomeFriends(String parentId, String targetId, boolean toInvite);

    void proceedInvitation(String parentId, String targetId, boolean toAccept);

    void removeFriend( String targetId, String thisId);
}
