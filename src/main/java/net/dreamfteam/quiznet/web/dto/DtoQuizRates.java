package net.dreamfteam.quiznet.web.dto;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class DtoQuizRates {
    private String id;

    private String title;

    private byte[] imageContent;

    private double rating;
}
