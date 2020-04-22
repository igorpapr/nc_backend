package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.User;

import java.util.List;

public interface UserDao {
    User getByEmail(String email);

    User getByUsername(String username);

    User getById(String id);

    User save(User user);

    void deleteById(String id);

    void update(User user);

    List<User> getAll(String currentUserId);

    User getByActivationUrl(String activationUrl);

    User getByRecoverUrl(String recoverUrl);

    List<User> getAllByRoleUser(String currentUserId);

    List<User> getBySubStr(String str, String currentUserId);

    List<User> getBySubStrAndRoleUser(String str, String currentUserId);
}
