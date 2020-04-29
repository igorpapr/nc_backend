package net.dreamfteam.quiznet.data.entities;


import lombok.*;

import java.util.Date;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class GameHistory {
    private String gameSessionId;
    private String quizId;
    private String gameId;
    private int score;
    private boolean winner;
    private Date datetimeStart;
    private int roundDuration;
    private String quizTitle;
    private byte[] quizImage;
}
