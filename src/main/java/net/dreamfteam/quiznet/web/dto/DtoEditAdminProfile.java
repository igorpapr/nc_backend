package net.dreamfteam.quiznet.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoEditAdminProfile {

    private String id;

    private String aboutMe;

    private String role;
}
