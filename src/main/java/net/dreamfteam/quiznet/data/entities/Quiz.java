package net.dreamfteam.quiznet.data.entities;

import lombok.*;

import java.util.Date;
import java.util.List;

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

    private List<Long> tagIdList;

    private List<Long> categoryIdList;

    private List<String> tagNameList;

    private List<String> categoryNameList;

    private boolean isFavourite;

}
