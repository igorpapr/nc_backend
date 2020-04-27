package net.dreamfteam.quiznet.data.entities;

import lombok.*;

import java.util.Date;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class Announcement {

    private String announcementId;

    private String creatorId;

    private String title;

    private String textContent;

    private String image;

    private Date creationDate;

    private Date publicationDate;

    private boolean isPublished;

}
