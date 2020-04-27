package net.dreamfteam.quiznet.web.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class DtoAnnouncement {

    private String title;

    private String creatorId;

    private String textContent;

    private String image;

    private Date creationDate;

}
