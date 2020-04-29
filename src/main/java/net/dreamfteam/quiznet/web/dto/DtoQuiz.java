package net.dreamfteam.quiznet.web.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class DtoQuiz {
    private String title;
    private String creatorId;
    private String language;
    private String description;
    private String imageRef;
    private List<String> tagList; // IDs of linked tags
    private List<String> categoryList; // IDs of linked categories
    private String quizId;

    //For getting quiz or mark as fav requests
    private String userId;

    //For validating quiz
    private String validator_id;
    private String adminCommentary;
    private boolean validated;

}
