package net.dreamfteam.quiznet.configs.token;


import net.dreamfteam.quiznet.data.entities.User;

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
                .password(user.getPassword())
                .build();

    }

//
//    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles){
//
//    }
}
