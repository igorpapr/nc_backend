package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.User;

import java.util.List;

public interface UserDao {
    User getByEmail(String email);

    User getByUsername(String username);

    User getById(Long id);

    User save(User user);

    void deleteById(Long id);

    void update(User user);

    List<User> getAll();

    User getByHashedId(String hashedId);
}
