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

    //For edit request
    private String quizId;
    private String newTitle;
    private String newDescription;
    private String newImageRef;
    private String newLanguage;
    private List<String> newTagList;
    private List<String> newCategoryList;

    //For getting quiz or mark as fav requests
    private String userId;

}
