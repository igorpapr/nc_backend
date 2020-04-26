package net.dreamfteam.quiznet.configs.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Spring Security wrapper for class {@link User}.
 * That we take from the authentication (Principal)
 *
 * @author Yevhen Khominich
 * @version 1.0
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtUser implements UserDetails {

    private String id;

    private String username;

    private String email;

    private String password;

    public Role getRole() {
        return role;
    }

    private Role role;

    private Collection<? extends GrantedAuthority> authorities;

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public String getId() {
        return id;
    }
}
