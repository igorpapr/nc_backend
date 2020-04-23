package net.dreamfteam.quiznet.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.User;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserLoginSuccessResponse {

    private boolean success;

    private String token;

    private String id;

    private String email;

    private String username;

    private Date creationDate;

    private Date lastTimeOnline;

    private String image;

    private String aboutMe;

    private boolean online;

    private boolean activated;

    private boolean verified;

    private Role role;

    public static UserLoginSuccessResponse fromUser(User user) {
        return UserLoginSuccessResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .creationDate(user.getCreationDate())
                .lastTimeOnline(user.getLastTimeOnline())
                .image(user.getImage())
                .aboutMe(user.getAboutMe())
                .online(user.isOnline())
                .activated(user.isActivated())
                .verified(user.isVerified())
                .role(user.getRole()).build();
    }

}
