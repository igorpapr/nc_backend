package net.dreamfteam.quiznet.web.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoQuizzesStatuses {
    int total;
    int published;
    int rejected;
    int activated;
    int unvalidated;
}
