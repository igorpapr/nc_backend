package net.dreamfteam.quiznet.data.entities;

import lombok.*;
import net.dreamfteam.quiznet.web.dto.DtoAnswer;

import java.util.Date;
import java.util.List;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class Answer {
    String gameId;
    String questionId;
    Date timeOfAnswer;
    List<String> answer;
}
