package net.dreamfteam.quiznet.web.dto;

import lombok.*;
import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.User;

import java.util.Date;

@Builder

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoUser {
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

    public static DtoUser fromUser(User user) {
        return DtoUser.builder()
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
