package net.dreamfteam.quiznet.data.entities;

import lombok.*;

import java.util.Date;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class UserAchievement {

	int achievementId;

	String title;

	String description;

	byte[] imageContent;

	String categoryId;

	String categoryTitle;

	Date datetimeGained;

	int timesGained;

}
