package net.dreamfteam.quiznet.data.entities;

import lombok.*;

import java.util.Date;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class Quiz {

    private long id;

    private String title;

    private String description;

    private String imageRef;

    private Date creationDate;

    private long creatorId;

    private boolean activated;

    private boolean validated;

    private String language;

    private String adminComment;

    private float rating;

}
