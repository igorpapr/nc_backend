package net.dreamfteam.quiznet.web.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class DtoPlayerSession {
    private String game_session_id;
    private String user_id;
    private String username;
    private byte[] image;
    private int score;
    private boolean is_winner;
    private boolean is_creator;
    private int duration_time;
}
