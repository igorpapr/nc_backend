package net.dreamfteam.quiznet.service;


import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;

import java.util.List;

public interface UserService {

    User save(User user) throws ValidationException;

    User saveAdmin(User user);

    User getById(String id);

    User getByActivationUrl(String activationUrl);

    User getByRecoverUrl(String recoverUrl);

    List<User> getAllByRole(Role role, String currentUserId);

    User getByUsername(String username);

    User getByEmail(String email);

    void deleteById(String id);

    void update(User user);

    void checkCorrectPassword(User user, String password);

    List<User> getBySubStr(String substr, Role role, String currentUserId);

}
