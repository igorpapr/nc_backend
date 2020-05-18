package net.dreamfteam.quiznet.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DtoNotification {
    private String content;
    private String userId;
}
