package net.dreamfteam.quiznet.web.dto;

import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class DtoUserAchievement {

	private String title;

	private String titleUk;

	private int timesGained;

	private String username; // for links
}
