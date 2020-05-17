package net.dreamfteam.quiznet.web.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class DtoEditAnnouncement {
    private String announcementId;

    private String creatorId;

    private String title;

    private String textContent;

}
