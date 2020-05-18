package net.dreamfteam.quiznet.data.entities;

import lombok.*;

import java.util.Date;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class QuizFiltered {

    private String id;

    private String title;

    private byte[] imageContent;
}
