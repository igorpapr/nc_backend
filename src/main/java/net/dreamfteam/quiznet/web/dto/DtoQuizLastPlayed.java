package net.dreamfteam.quiznet.web.dto;

import lombok.*;

import java.util.Date;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class DtoQuizLastPlayed {
    private String gameId;

    private String quizId;

    private String title;

    private boolean isWinner;

    private int score;

    private int durationTime;

    private Date datetimeStart;
}
