package net.dreamfteam.quiznet.web.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DtoAnswer {
    private String sessionId;
    private String questionId;
    private int typeId;
    private Date timeOfAnswer;
    private List<String> answer;
}
