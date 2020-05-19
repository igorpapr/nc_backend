package net.dreamfteam.quiznet.data.entities;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class UserCategoryAchievementInfo {

	private String categoryId;

	String categoryTitle;

	private int amountPlayed;


}
