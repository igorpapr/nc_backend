package net.dreamfteam.quiznet.web.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtoChangePassword {

    private String recoverUrl;

    private String password;
}
