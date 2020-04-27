package net.dreamfteam.quiznet.web.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DtoQuizFilter {
    private String quizName; // String from search field. Full or part of the quiz name
    private String userName; // Filter quizzes by username
    private int moreThanRating; // Lower point in rating range
    private int lessThanRating; // Upper point in rating range
    private List<String> tags; // Tags list
    private List<String> categories; // Categories list
    private Boolean orderByRating;
}
