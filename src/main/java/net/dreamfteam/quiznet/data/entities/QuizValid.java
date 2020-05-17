package net.dreamfteam.quiznet.data.entities;

import lombok.*;

import java.util.Date;
import java.util.List;


@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class QuizValid {

    private String id;

    private String title;

    private String description;

    private Date creationDate;

    private String creatorId;

    private String username;

    private String language;

    private String adminComment;

    private byte[] imageContent;

    private boolean published;

    private boolean activated;
}
