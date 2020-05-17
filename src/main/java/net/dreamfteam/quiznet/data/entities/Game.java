package net.dreamfteam.quiznet.data.entities;

import lombok.*;

import java.util.Date;


@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class Game {
    private String id;

    private Date startDatetime;

    private int maxUsersCount;

    private int numberOfQuestions;

    private int roundDuration;

    private boolean additionalPoints;

    private int breakTime;

    private String accessId;

    private String quizId;

}
