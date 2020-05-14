package net.dreamfteam.quiznet.data.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class GameSession {
    private String id;
    private String userId;
    private String username;
    private String gameId;
    private int score;
    private boolean winner;
    private boolean creator;
    private boolean savedByUser;
    private int durationTime;
}
