package net.dreamfteam.quiznet.configs.security;

import net.dreamfteam.quiznet.configs.token.JwtUser;
import net.dreamfteam.quiznet.data.entities.Role;
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

    @Override
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
            return jwtUser.getUsername();
        }

        return null;
    }

    @Override
    public Role getRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
            return jwtUser.getRole();
        }

        return null;
    }

}
