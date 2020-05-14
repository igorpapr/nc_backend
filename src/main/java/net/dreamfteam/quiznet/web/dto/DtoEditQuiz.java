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
public class DtoEditQuiz {
    private String quizId;
    private String newTitle;
    private String newDescription;
    private String newLanguage;
    private List<String> newTagList;
    private List<String> newCategoryList;
}
