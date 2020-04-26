package net.dreamfteam.quiznet.configs.token;


import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of Factory Method for class {@link JwtUser}.
 *
 * @author Yevhen Khominich
 * @version 1.0
 */

public class JwtUserFactory {

    public static JwtUser userToJwtUser(User user) {
        return JwtUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .authorities(mapToGrantedAuthorities(user.getRole()))
                .password(user.getPassword())
                .build();

    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Role role) {
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(role.toString()));
        return roles;
    }
}
