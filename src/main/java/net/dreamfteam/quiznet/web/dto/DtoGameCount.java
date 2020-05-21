package net.dreamfteam.quiznet.web.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class DtoGameCount {
    private Date date;

    private int gamesAmount;
}
