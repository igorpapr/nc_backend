package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.UserAccountService;
import net.dreamfteam.quiznet.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    final private UserService userService;

    final private BCryptPasswordEncoder passwordEncoder;

    public UserAccountServiceImpl(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void changePassword(String userId, String newPassword, String currentPassword) {
        User currentUser = userService.getById(userId);

        if (passwordEncoder.matches(currentPassword, currentUser.getPassword())) {
            currentUser.setPassword(passwordEncoder.encode(newPassword));
            userService.update(currentUser);
        } else throw new ValidationException("Current password don't matches.");

    }
}
