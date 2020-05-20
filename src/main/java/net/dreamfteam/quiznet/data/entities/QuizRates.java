package net.dreamfteam.quiznet.data.entities;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class QuizRates {
    private String id;

    private String title;

    private byte[] imageContent;

    private double rating;
}
