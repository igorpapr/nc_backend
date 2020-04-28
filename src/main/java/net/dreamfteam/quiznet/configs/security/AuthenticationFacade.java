package net.dreamfteam.quiznet.configs.security;

import net.dreamfteam.quiznet.configs.token.JwtUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    // Take user from authentication
    @Override
    public String getUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
            return jwtUser.getId();
        }

        return null;
    }

}
