package net.dreamfteam.quiznet.web.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class DtoPopularQuiz {
    private String quizId;

    private String title;

    private int gamesAmount;
}
