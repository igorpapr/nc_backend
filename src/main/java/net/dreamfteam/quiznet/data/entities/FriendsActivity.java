package net.dreamfteam.quiznet.data.entities;

import lombok.*;

import java.util.Date;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class FriendsActivity {
	private String activityId;

	private String content;

	private Date datetime;

	//Id of the person owner of this activity
	private String userId;

	private String username;

	private byte[] imageContent;
}
