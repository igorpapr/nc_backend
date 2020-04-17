package net.dreamfteam.quiznet.service;


import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;

import java.util.List;

public interface UserService {

    User save(User user) throws ValidationException;

    User getById(Long id);

    User getByActivationUrl(String hashedId);

    List<User> getAll();

    User getByUsername(String username);

    User getByEmail(String email);

    void deleteById(Long id);

    void update(User user);


}
