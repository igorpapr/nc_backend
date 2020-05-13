package net.dreamfteam.quiznet.data.entities;

import lombok.*;

import java.util.Date;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class UserFriendInvitation {
	private String id;

	private String username;

	private Date invitationDatetime;

	private byte[] imageContent;
}
