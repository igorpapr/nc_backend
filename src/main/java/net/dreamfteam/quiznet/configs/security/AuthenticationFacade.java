package net.dreamfteam.quiznet.configs.security;

import net.dreamfteam.quiznet.configs.token.JwtUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    // Take user from authentication
    @Override
    public String  getUserId() {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwtUser.getId();
    }
}
