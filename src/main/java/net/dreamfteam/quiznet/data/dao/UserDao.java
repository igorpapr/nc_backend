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

    List<User> getAll();

    User getByActivationUrl(String activationUrl);
}
