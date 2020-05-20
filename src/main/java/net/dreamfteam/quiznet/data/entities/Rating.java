package net.dreamfteam.quiznet.data.entities;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class Rating {
    String quizId;
    String userId;
    int rating;
}
