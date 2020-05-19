package net.dreamfteam.quiznet.data.entities;

import lombok.*;

import java.util.Date;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class QuizLastPlayed {
    private String gameId;

    private String quizId;

    private String title;

    private boolean isWinner;

    private int score;

    private int durationTime;

    private Date datetimeStart;
}
