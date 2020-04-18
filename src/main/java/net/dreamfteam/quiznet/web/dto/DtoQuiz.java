package net.dreamfteam.quiznet.web.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class DtoQuiz {
    private String title;
    private long creatorId;
    private String language;
    private String description;
    private String imageRef;
    private List<Long> tagList; // IDs of linked tags
    private List<Long> categoryList; // IDs of linked categories

    //For edit request
    private long quizId;
    private String newTitle;
    private String newDescription;
    private String newImageRef;
    private String newLanguage;
    private List<Long> newTagList;
    private List<Long> newCategoryList;

    //For getting quiz or mark as fav requests
    private long userId;

}
