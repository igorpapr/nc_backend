package net.dreamfteam.quiznet.web.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class DtoUser {

    private Long userId;

    private String username;

    private String email;

    private String password;

}
