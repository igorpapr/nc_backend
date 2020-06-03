package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.configs.constants.Constants;
import net.dreamfteam.quiznet.configs.token.JwtUserFactory;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    final private UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.getByUsername(username);

        if (user == null) throw new ValidationException(Constants.USER_NOT_FOUND_WITH_USERNAME + username);

        return JwtUserFactory.userToJwtUser(user);
    }

}
