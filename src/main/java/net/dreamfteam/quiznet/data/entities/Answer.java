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
    private String answerId;
    private String sessionId;
    private String questionId;
    private int typeId;
    private Date timeOfAnswer;
    private List<String> answer;
}
