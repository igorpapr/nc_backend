package net.dreamfteam.quiznet.data.entities;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class QuizRating {
    private String quizId;

    private String title;

    private byte[] imageContent;

    private int rating;
}