package net.dreamfteam.quiznet.web.dto;

import lombok.*;
import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.User;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DtoAdminActivation {

    private String id;

    private boolean activated;

}
