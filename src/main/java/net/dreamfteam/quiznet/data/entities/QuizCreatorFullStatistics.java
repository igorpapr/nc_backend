package net.dreamfteam.quiznet.data.entities;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class QuizCreatorFullStatistics {
	private String creatorId;

	private int amountGamesPlayedAllQuizzes;
}
