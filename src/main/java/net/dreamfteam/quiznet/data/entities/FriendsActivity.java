package net.dreamfteam.quiznet.data.entities;

import lombok.*;

import java.util.Date;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class FriendsActivity {
	String activityId;

	String content;

	Date datetime;

	//Id of the person owner of this activity
	String userId;

	String username;

	byte[] imageContent;
}
