package net.dreamfteam.quiznet.data.entities;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class QuizMediaView { //Used in suggestions
	private String quizId;

	private String title;

	private String description;

	private byte[] imageContent;
}
