package net.dreamfteam.quiznet.data.entities;

import lombok.*;

import java.util.List;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class Question {

    private String id;

    private String quizId;

    private String title;

    private String content;

    private String image;

    private int points;

    private int typeId;

    private List<String> rightOptions;

    private List<String> otherOptions;
}
