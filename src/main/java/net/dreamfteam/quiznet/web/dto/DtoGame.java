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
public class DtoGame {

    private String id;

    private int maxUsersCount;

    private int numberOfQuestions;

    private int roundDuration;

    private boolean additionalPoints;

    private int breakTime;

    private String accessId;

    private String quizId;

}
