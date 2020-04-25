package net.dreamfteam.quiznet.data.entities;


import lombok.*;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class QuizView {
    private String quiz_id;

    private String title;

    private String image_ref;
}
