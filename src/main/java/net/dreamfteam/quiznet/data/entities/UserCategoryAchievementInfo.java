package net.dreamfteam.quiznet.data.entities;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class UserCategoryAchievementInfo {

	private String categoryId;

	private int achievementId;

	private int amountPlayed;


}
