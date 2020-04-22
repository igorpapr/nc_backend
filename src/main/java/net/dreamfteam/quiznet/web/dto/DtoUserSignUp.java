package net.dreamfteam.quiznet.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dreamfteam.quiznet.data.entities.User;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DtoUserSignUp {

    private String username;

    private String email;

    private String password;

    public User toUser() {
       return User.builder()
                .email(email)
                .password(password)
                .username(username)
                .build();
    }
}
