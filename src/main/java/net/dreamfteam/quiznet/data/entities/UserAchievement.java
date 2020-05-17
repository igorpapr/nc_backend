package net.dreamfteam.quiznet.data.entities;

import lombok.*;

import java.util.Date;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class UserAchievement {

	private int achievementId;

	private String title;

	private String description;

	private byte[] imageContent;

	private String categoryId;

	private String categoryTitle;

	private Date datetimeGained;

	private int timesGained;

}
