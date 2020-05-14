package net.dreamfteam.quiznet.data.entities;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class UserAchievementInfo {

	private int achievement_id;

	private String category_id;

	private String userId;

	private int amountPlayed;

}
