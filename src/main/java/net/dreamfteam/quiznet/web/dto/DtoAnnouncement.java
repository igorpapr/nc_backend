package net.dreamfteam.quiznet.web.dto;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DtoAnnouncement {

    private String title;

    private String creatorId;

    private String textContent;

    private Date creationDate;

}
